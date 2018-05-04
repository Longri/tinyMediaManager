/*
 * Copyright (C) 2018 team-cachebox.de
 *
 * Licensed under the : GNU General Public License (GPL);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/gpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.longri.tinymediamanager;

import org.tinymediamanager.core.TmmProperties;
import org.tinymediamanager.core.movie.MovieModuleManager;
import org.tinymediamanager.core.movie.MovieSettings;
import org.tinymediamanager.core.tvshow.TvShowModuleManager;
import org.tinymediamanager.core.tvshow.TvShowSettings;
import org.tinymediamanager.ui.MainWindow;
import org.tinymediamanager.ui.TmmUIHelper;
import org.tinymediamanager.ui.UTF8Control;
import org.tinymediamanager.ui.dialogs.LogDialog;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * Created by Longri on 03.05.2018.
 */
public class ChangedMainWindow extends MainWindow {

  private static final String SERVER_PATH_PROPERTY = "longri.server.path";

  private static final ResourceBundle BUNDLE          = ResourceBundle.getBundle("messages", new UTF8Control());
  private              MovieSettings  Movie_Settings  = MovieModuleManager.MOVIE_SETTINGS;
  private              TvShowSettings TvShow_Settings = TvShowModuleManager.SETTINGS;

  /**
   * Create the application.
   *
   * @param name the name
   */
  public ChangedMainWindow(String name) {
    super(name);

    JMenuBar menuBar = getJMenuBar();

    JMenu tools = menuBar.getMenu(3);//tools menu

    tools.addSeparator();

    JMenuItem tmmExtractFromServer = new JMenuItem("Extract NFO from Server"); //$NON-NLS-1$
    tmmExtractFromServer.setMnemonic(KeyEvent.VK_L);
    tools.add(tmmExtractFromServer);
    tmmExtractFromServer.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent arg0) {
        //                JDialog logDialog = new LogDialog();
        //                logDialog.setLocationRelativeTo(MainWindow.getActiveInstance());
        //                logDialog.setVisible(true);
        extractFromServer();
      }
    });

    JMenuItem tmmWriteBackToServer = new JMenuItem("WriteBack to Server"); //$NON-NLS-1$
    tmmWriteBackToServer.setMnemonic(KeyEvent.VK_L);
    tools.add(tmmWriteBackToServer);
    tmmWriteBackToServer.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent arg0) {
        JDialog logDialog = new LogDialog();
        logDialog.setLocationRelativeTo(MainWindow.getActiveInstance());
        logDialog.setVisible(true);
      }
    });

  }

  void extractFromServer() {

    //check if DataSource set
    List<String> movieSource = Movie_Settings.getMovieDataSource();
    List<String> tvShowSource = TvShow_Settings.getTvShowDataSource();

    if (movieSource.isEmpty() || tvShowSource.isEmpty()) {
      throw new NullPointerException("Media Source must not be null");
    }

    FileHandle target = extractTargetPath(movieSource, tvShowSource);

    String path = TmmProperties.getInstance().getProperty(SERVER_PATH_PROPERTY);
    Path file = TmmUIHelper.selectDirectory("Select Server Path", path); //$NON-NLS-1$
    if (file != null && Files.isDirectory(file)) {
      //      settings.addMovieDataSources(file.toAbsolutePath().toString());
      TmmProperties.getInstance().putProperty(SERVER_PATH_PROPERTY, file.toAbsolutePath().toString());
    }
  }

   FileHandle extractTargetPath(List<String> movieSource, List<String> tvShowSource) {

    FileHandle combinedMovie = getCombinedPath(movieSource);
    FileHandle combinedTvShow = getCombinedPath(tvShowSource);

    List<String> source = new ArrayList<>();
    source.add(combinedMovie.path());
    source.add(combinedTvShow.path());

    return getCombinedPath(source);
  }

  FileHandle getCombinedPath(List<String> source) {
    Array<String[]> arr = new Array<>();

    int minLength = Integer.MAX_VALUE;
    for (String path : source) {
      path = path.replace("\\", "/");
      String[] pathArray = path.split("/");
      arr.add(pathArray);
      minLength = Integer.min(minLength, pathArray.length);
    }

    int lastEqualsIndex = -1;
    for (int i = 0; i < minLength; i++) {
      if (lastEqualsIndex >= 0)
        break;
      for (int j = 0; j < arr.size - 1; j++) {
        if (!arr.get(j)[i].equals(arr.get(j + 1)[i])) {
          lastEqualsIndex = i;
          break;
        }
      }
    }

    if (lastEqualsIndex == -1)
      lastEqualsIndex = minLength;

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < lastEqualsIndex; i++) {
      sb.append(arr.get(0)[i]).append('/');
    }

    FileHandle path = Gdx.files.absolute(sb.toString());

    return path;
  }

}
