/*
 * Copyright 2012 - 2018 Manuel Laggner
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
package org.tinymediamanager.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.Globals;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class IconManager {
  private final static Logger              LOGGER            = LoggerFactory.getLogger(IconManager.class);
  private final static Map<URI, ImageIcon> ICON_CACHE        = new HashMap<>();
  public final static ImageIcon            EMPTY_IMAGE       = new ImageIcon(IconManager.class.getResource("images/empty.png"));

  public static ImageIcon            APPLY;
  public static ImageIcon            ARROW_UP;
  public static ImageIcon            ARROW_DOWN;
  public static ImageIcon            BUG;
  public static ImageIcon            CANCEL;
  public static ImageIcon            CHECK_ALL;
  public static ImageIcon            CHECKMARK;
  public static ImageIcon            CLAPBOARD;
  public static ImageIcon            COPY;
  public static ImageIcon            CROSS;
  public static ImageIcon            DATE_PICKER;
  public static ImageIcon            DELETE;
  public static ImageIcon            DOWNLOAD;
  public static ImageIcon            DOWNLOAD_DISABLED;
  public static ImageIcon            EDIT;
  public static ImageIcon            ERROR;
  public static ImageIcon            EXIT;
  public static ImageIcon            EXPORT;
  public static ImageIcon            FEEDBACK;
  public static ImageIcon            FILE_OPEN;
  public static ImageIcon            FILTER;
  public static ImageIcon            FILTER_ACTIVE;
  public static ImageIcon            HINT;
  public static ImageIcon            IMAGE;
  public static ImageIcon            INFO;
  public static ImageIcon            LIST_ADD;
  public static ImageIcon            LIST_REMOVE;
  public static ImageIcon            LOADING;
  public static ImageIcon            NEW;
  public static ImageIcon            PLAY_SMALL;
  public static ImageIcon            PLAY;
  public static ImageIcon            PROCESS_STOP;
  public static ImageIcon            REFRESH;
  public static ImageIcon            REGISTER;
  public static ImageIcon            SEARCH;
  public static ImageIcon            SETTINGS_SMALL;
  public static ImageIcon            STAR_FILLED;
  public static ImageIcon            STAR_EMPTY;
  public static ImageIcon            SUBTITLE;
  public static ImageIcon            SYNC;
  public static ImageIcon            UNCHECK_ALL;
  public static ImageIcon            UNWATCHED;

  private static ImageIcon loadImage(String name, String lookAndFeelName) {
    //first try to load LookAndFeel depended image
    URL file = null;

    if(!lookAndFeelName.equals("default")){
      //from resource lib
//      file = IconManager.class.getResource("/images/" + lookAndFeelName + "/ui/" + name);

      // from included resources
      file = IconManager.class.getResource("/org/tinymediamanager/ui/images/" + lookAndFeelName + "/ui/" + name);
      if(file==null){
        LOGGER.debug("Cant load LookAndFeel ({}) depended image: {}! Use default",lookAndFeelName,name);
        file = IconManager.class.getResource("/images/ui/" + name);
      }
    }else{
      file = IconManager.class.getResource("/images/ui/" + name);
    }
    if (file != null) {
      return new ImageIcon(file);
    }

    return EMPTY_IMAGE;
  }

  public static void reloadImages(){
    String lookAndFeelName = Globals.settings.getStyle().getLookAndFeelName().toLowerCase();
    
    APPLY             = loadImage("apply.png", lookAndFeelName);
    ARROW_UP          = loadImage("arrow-up.png", lookAndFeelName);
    ARROW_DOWN        = loadImage("arrow-down.png", lookAndFeelName);
    BUG               = loadImage("bug.png", lookAndFeelName);
    CANCEL            = loadImage("cancel.png", lookAndFeelName);
    CHECK_ALL         = loadImage("check-all.png", lookAndFeelName);
    CHECKMARK         = loadImage("checkmark.png", lookAndFeelName);
    CLAPBOARD         = loadImage("clapboard.png", lookAndFeelName);
    COPY              = loadImage("copy.png", lookAndFeelName);
    CROSS             = loadImage("cross.png", lookAndFeelName);
    DATE_PICKER       = loadImage("datepicker.png", lookAndFeelName);
    DELETE            = loadImage("delete.png", lookAndFeelName);
    DOWNLOAD          = loadImage("download.png", lookAndFeelName);
    DOWNLOAD_DISABLED = loadImage("download-disabled.png", lookAndFeelName);
    EDIT              = loadImage("edit.png", lookAndFeelName);
    ERROR             = loadImage("error.png", lookAndFeelName);
    EXIT              = loadImage("exit.png", lookAndFeelName);
    EXPORT            = loadImage("export.png", lookAndFeelName);
    FEEDBACK          = loadImage("feedback.png", lookAndFeelName);
    FILE_OPEN         = loadImage("file-open.png", lookAndFeelName);
    FILTER            = loadImage("filter.png", lookAndFeelName);
    FILTER_ACTIVE     = loadImage("filter_active.png", lookAndFeelName);
    HINT              = loadImage("hint.png", lookAndFeelName);
    IMAGE             = loadImage("image.png", lookAndFeelName);
    INFO              = loadImage("info.png", lookAndFeelName);
    LIST_ADD          = loadImage("list-add.png", lookAndFeelName);
    LIST_REMOVE       = loadImage("list-remove.png", lookAndFeelName);
    LOADING           = loadImage("loading.gif", lookAndFeelName);
    NEW               = loadImage("new.png", lookAndFeelName);
    PLAY_SMALL        = loadImage("play-small.png", lookAndFeelName);
    PLAY              = loadImage("play.png", lookAndFeelName);
    PROCESS_STOP      = loadImage("process-stop.png", lookAndFeelName);
    REFRESH           = loadImage("refresh.png", lookAndFeelName);
    REGISTER          = loadImage("register.png", lookAndFeelName);
    SEARCH            = loadImage("search.png", lookAndFeelName);
    SETTINGS_SMALL    = loadImage("settings-small.png", lookAndFeelName);
    STAR_FILLED       = loadImage("star-filled.png", lookAndFeelName);
    STAR_EMPTY        = loadImage("star-empty.png", lookAndFeelName);
    SUBTITLE          = loadImage("subtitle.png", lookAndFeelName);
    SYNC              = loadImage("sync.png", lookAndFeelName);
    UNCHECK_ALL       = loadImage("uncheck-all.png", lookAndFeelName);
    UNWATCHED         = loadImage("unwatched.png", lookAndFeelName);
  }

  /**
   * loads an image from the given url
   * 
   * @param url
   *          the url pointing to the image
   * @return the image or an empty image (1x1 px transparent) if it is not loadable
   */
  public static ImageIcon loadImageFromURL(URL url) {
    URI uri = null;

    if (url == null) {
      return EMPTY_IMAGE;
    }

    try {
      uri = url.toURI();
      if (uri == null) {
        return EMPTY_IMAGE;
      }
    }
    catch (Exception e) {
      return EMPTY_IMAGE;
    }

    // read cache
    ImageIcon icon = ICON_CACHE.get(uri);

    if (icon == null) {
      try {
        icon = new ImageIcon(url);
      }
      catch (Exception ignored) {
      }
      finally {
        if (icon == null) {
          icon = EMPTY_IMAGE;
        }
      }
      ICON_CACHE.put(uri, icon);
    }

    return icon;
  }
}
