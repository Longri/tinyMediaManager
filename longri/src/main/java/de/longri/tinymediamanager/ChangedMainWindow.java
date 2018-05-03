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

import org.tinymediamanager.ui.MainWindow;
import org.tinymediamanager.ui.dialogs.LogDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Longri on 03.05.2018.
 */
public class ChangedMainWindow extends MainWindow {
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
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JDialog logDialog = new LogDialog();
                logDialog.setLocationRelativeTo(MainWindow.getActiveInstance());
                logDialog.setVisible(true);
            }
        });

        JMenuItem tmmWriteBackToServer = new JMenuItem("WriteBack to Server"); //$NON-NLS-1$
        tmmWriteBackToServer.setMnemonic(KeyEvent.VK_L);
        tools.add(tmmWriteBackToServer);
        tmmWriteBackToServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JDialog logDialog = new LogDialog();
                logDialog.setLocationRelativeTo(MainWindow.getActiveInstance());
                logDialog.setVisible(true);
            }
        });

    }
}
