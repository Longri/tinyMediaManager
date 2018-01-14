/*
 * Copyright 2012 - 2017 Manuel Laggner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinymediamanager.core.tvshow.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.core.Message;
import org.tinymediamanager.core.Message.MessageLevel;
import org.tinymediamanager.core.MessageManager;
import org.tinymediamanager.core.entities.Person;
import org.tinymediamanager.core.threading.TmmTask;
import org.tinymediamanager.core.threading.TmmTaskManager;
import org.tinymediamanager.core.threading.TmmThreadPool;
import org.tinymediamanager.core.tvshow.TvShowList;
import org.tinymediamanager.core.tvshow.TvShowModuleManager;
import org.tinymediamanager.core.tvshow.TvShowScraperMetadataConfig;
import org.tinymediamanager.core.tvshow.TvShowSearchAndScrapeOptions;
import org.tinymediamanager.core.tvshow.entities.TvShow;
import org.tinymediamanager.core.tvshow.entities.TvShowEpisode;
import org.tinymediamanager.scraper.MediaMetadata;
import org.tinymediamanager.scraper.MediaScrapeOptions;
import org.tinymediamanager.scraper.MediaScraper;
import org.tinymediamanager.scraper.MediaSearchResult;
import org.tinymediamanager.scraper.entities.MediaArtwork;
import org.tinymediamanager.scraper.entities.MediaArtwork.MediaArtworkType;
import org.tinymediamanager.scraper.entities.MediaCastMember;
import org.tinymediamanager.scraper.entities.MediaType;
import org.tinymediamanager.scraper.mediaprovider.ITvShowArtworkProvider;
import org.tinymediamanager.scraper.mediaprovider.ITvShowMetadataProvider;
import org.tinymediamanager.scraper.trakttv.SyncTraktTvTask;
import org.tinymediamanager.ui.UTF8Control;

/**
 * The class TvShowScrapeTask. This starts scraping of TV shows
 * 
 * @author Manuel Laggner
 */
public class TvShowScrapeTask extends TmmThreadPool {
  private final static Logger          LOGGER = LoggerFactory.getLogger(TvShowScrapeTask.class);
  private static final ResourceBundle  BUNDLE = ResourceBundle.getBundle("messages", new UTF8Control()); //$NON-NLS-1$

  private List<TvShow>                 tvShowsToScrape;
  private boolean                      doSearch;
  private TvShowSearchAndScrapeOptions options;

  /**
   * Instantiates a new tv show scrape task.
   * 
   * @param tvShowsToScrape
   *          the tv shows to scrape
   * @param doSearch
   *          the do search
   * @param options
   *          the options
   */
  public TvShowScrapeTask(List<TvShow> tvShowsToScrape, boolean doSearch, TvShowSearchAndScrapeOptions options) {
    super(BUNDLE.getString("tvshow.scraping"));
    this.tvShowsToScrape = tvShowsToScrape;
    this.doSearch = doSearch;
    this.options = options;
  }

  @Override
  protected void doInBackground() {
    LOGGER.debug("start scraping tv shows...");
    start();

    initThreadPool(3, "scrape");
    for (TvShow tvShow : tvShowsToScrape) {
      submitTask(new Worker(tvShow));
    }

    waitForCompletionOrCancel();

    if (TvShowModuleManager.SETTINGS.getSyncTrakt()) {
      TmmTask task = new SyncTraktTvTask(null, tvShowsToScrape);
      TmmTaskManager.getInstance().addUnnamedTask(task);
    }

    LOGGER.debug("done scraping tv shows...");
  }

  private class Worker implements Runnable {
    private TvShowList tvShowList = TvShowList.getInstance();
    private TvShow     tvShow;

    public Worker(TvShow tvShow) {
      this.tvShow = tvShow;
    }

    @Override
    public void run() {
      try {
        // set up scrapers
        TvShowScraperMetadataConfig scraperMetadataConfig = options.getScraperMetadataConfig();
        MediaScraper mediaMetadataScraper = options.getMetadataScraper();
        List<MediaScraper> artworkScrapers = options.getArtworkScrapers();

        // scrape tv show

        // search for tv show
        MediaSearchResult result1 = null;
        if (doSearch) {
          List<MediaSearchResult> results = tvShowList.searchTvShow(tvShow.getTitle(), tvShow, mediaMetadataScraper);
          if (results != null && !results.isEmpty()) {
            result1 = results.get(0);
            // check if there is an other result with 100% score
            if (results.size() > 1) {
              MediaSearchResult result2 = results.get(1);
              // if both results have 100% score - do not take any result
              if (result1.getScore() == 1 && result2.getScore() == 1) {
                LOGGER.info("two 100% results, can't decide whitch to take - ignore result");
                MessageManager.instance.pushMessage(new Message(MessageLevel.ERROR, tvShow, "tvshow.scrape.nomatchfound"));
                return;
              }
              // create a treshold of 0.75 - to minimize false positives
              if (result1.getScore() < 0.75) {
                LOGGER.info("score is lower than 0.75 (" + result1.getScore() + ") - ignore result");
                MessageManager.instance.pushMessage(new Message(MessageLevel.ERROR, tvShow, "tvshow.scrape.nomatchfound"));
                return;
              }
            }
          }
          else {
            LOGGER.info("no result found for " + tvShow.getTitle());
            MessageManager.instance.pushMessage(new Message(MessageLevel.ERROR, tvShow, "tvshow.scrape.nomatchfound"));
          }
        }

        // get metadata and artwork
        if ((doSearch && result1 != null) || !doSearch) {
          try {
            MediaScrapeOptions options = new MediaScrapeOptions(MediaType.TV_SHOW);
            options.setResult(result1);
            options.setLanguage(LocaleUtils.toLocale(TvShowModuleManager.SETTINGS.getScraperLanguage().name()));
            options.setCountry(TvShowModuleManager.SETTINGS.getCertificationCountry());

            // we didn't do a search - pass imdbid and tmdbid from movie
            // object
            if (!doSearch) {
              for (Entry<String, Object> entry : tvShow.getIds().entrySet()) {
                options.setId(entry.getKey(), entry.getValue().toString());
              }
            }

            // override scraper with one from search result
            mediaMetadataScraper = tvShowList.getMediaScraperById(result1.getProviderId());
            // scrape metadata if wanted
            MediaMetadata md = null;

            if (scraperMetadataConfig.isCast() || scraperMetadataConfig.isCertification() || scraperMetadataConfig.isGenres()
                || scraperMetadataConfig.isAired() || scraperMetadataConfig.isPlot() || scraperMetadataConfig.isRating()
                || scraperMetadataConfig.isRuntime() || scraperMetadataConfig.isStatus() || scraperMetadataConfig.isTitle()
                || scraperMetadataConfig.isYear()) {
              LOGGER.info("=====================================================");
              LOGGER.info("Scraper metadata with scraper: " + mediaMetadataScraper.getMediaProvider().getProviderInfo().getId() + ", "
                  + mediaMetadataScraper.getMediaProvider().getProviderInfo().getVersion());
              LOGGER.info(options.toString());
              LOGGER.info("=====================================================");
              md = ((ITvShowMetadataProvider) mediaMetadataScraper.getMediaProvider()).getMetadata(options);
              tvShow.setMetadata(md, scraperMetadataConfig);
            }

            if (scraperMetadataConfig.isEpisodeList()) {
              List<TvShowEpisode> episodes = new ArrayList<>();
              try {
                for (MediaMetadata me : ((ITvShowMetadataProvider) mediaMetadataScraper.getMediaProvider()).getEpisodeList(options)) {
                  TvShowEpisode ep = new TvShowEpisode();
                  ep.setEpisode(me.getEpisodeNumber());
                  ep.setSeason(me.getSeasonNumber());
                  ep.setDvdEpisode(me.getDvdEpisodeNumber());
                  ep.setDvdSeason(me.getDvdSeasonNumber());
                  ep.setTitle(me.getTitle());
                  ep.setOriginalTitle(me.getOriginalTitle());
                  ep.setPlot(me.getPlot());

                  List<Person> actors = new ArrayList<>();
                  List<Person> directors = new ArrayList<>();
                  List<Person> writers = new ArrayList<>();

                  for (MediaCastMember member : me.getCastMembers()) {
                    switch (member.getType()) {
                      case ACTOR:
                        actors.add(new Person(member));
                        break;

                      case DIRECTOR:
                        directors.add(new Person(member));
                        break;

                      case WRITER:
                        writers.add(new Person(member));
                        break;

                      default:
                        break;
                    }
                  }
                  ep.setActors(actors);
                  ep.setDirectors(directors);
                  ep.setWriters(writers);

                  episodes.add(ep);
                }
              }
              catch (Exception e) {
                LOGGER.error(e.getMessage());
              }
              tvShow.setDummyEpisodes(episodes);
            }

            // scrape episodes
            if (scraperMetadataConfig.isEpisodes()) {
              List<TvShowEpisode> episodesToScrape = tvShow.getEpisodesToScrape();
              // scrape episodes in a task
              if (!episodesToScrape.isEmpty()) {
                TvShowEpisodeScrapeTask task = new TvShowEpisodeScrapeTask(episodesToScrape, mediaMetadataScraper);
                TmmTaskManager.getInstance().addUnnamedTask(task);
              }
            }

            // scrape artwork if wanted
            if (scraperMetadataConfig.isArtwork()) {
              tvShow.setArtwork(getArtwork(tvShow, md, artworkScrapers), scraperMetadataConfig);
            }

          }
          catch (Exception e) {
            LOGGER.error("tvShow.setMetadata", e);
            MessageManager.instance.pushMessage(new Message(MessageLevel.ERROR, tvShow, "message.scrape.metadatatvshowfailed"));
          }
        }
      }

      catch (Exception e) {
        LOGGER.error("Thread crashed", e);
        MessageManager.instance.pushMessage(
            new Message(MessageLevel.ERROR, "TvShowScraper", "message.scrape.threadcrashed", new String[] { ":", e.getLocalizedMessage() }));
      }
    }

    /**
     * Gets the artwork.
     * 
     * @param metadata
     *          the metadata
     * @return the artwork
     */
    public List<MediaArtwork> getArtwork(TvShow tvShow, MediaMetadata metadata, List<MediaScraper> artworkScrapers) {
      List<MediaArtwork> artwork = new ArrayList<>();

      MediaScrapeOptions options = new MediaScrapeOptions(MediaType.TV_SHOW);
      options.setArtworkType(MediaArtworkType.ALL);
      options.setMetadata(metadata);
      options.setLanguage(LocaleUtils.toLocale(TvShowModuleManager.SETTINGS.getScraperLanguage().name()));
      options.setCountry(TvShowModuleManager.SETTINGS.getCertificationCountry());
      for (Entry<String, Object> entry : tvShow.getIds().entrySet()) {
        options.setId(entry.getKey(), entry.getValue().toString());
      }

      // scrape providers till one artwork has been found
      for (MediaScraper artworkScraper : artworkScrapers) {
        ITvShowArtworkProvider artworkProvider = (ITvShowArtworkProvider) artworkScraper.getMediaProvider();
        try {
          artwork.addAll(artworkProvider.getArtwork(options));
        }
        catch (Exception e) {
          LOGGER.error("getArtwork", e);
          MessageManager.instance.pushMessage(new Message(MessageLevel.ERROR, tvShow, "message.scrape.tvshowartworkfailed"));
        }
      }
      return artwork;
    }
  }

  @Override
  public void callback(Object obj) {
    // do not publish task description here, because with different workers the text is never right
    publishState(progressDone);
  }
}
