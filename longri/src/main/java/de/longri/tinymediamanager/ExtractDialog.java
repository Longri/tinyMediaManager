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
package de.longri.tinymediamanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.ui.UTF8Control;
import org.tinymediamanager.ui.dialogs.TmmDialog;

import com.badlogic.gdx.files.FileHandle;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ExtractDialog extends TmmDialog implements ActionListener {
  private static final long           serialVersionUID = -5054005564554148578L;
  /**
   * @wbp.nls.resourceBundle messages
   */
  private static final ResourceBundle BUNDLE           = ResourceBundle.getBundle("messages", new UTF8Control()); //$NON-NLS-1$
  private static final Logger         log              = LoggerFactory.getLogger(ExtractDialog.class);
  private static final int            REFRESH_PERIOD   = 200;

  private JTextArea taLogs;

  private       int   logByteCount = 0;
  private final Timer timerRefresh;

  private final ExtractMediaStruct extractMediaStruct;
  private       JButton            btnClose;

  public ExtractDialog(FileHandle source, FileHandle target) {
    super("Extract NFO from Server", "log"); //$NON-NLS-1$
    this.extractMediaStruct = new ExtractMediaStruct(source, target);
    setBounds(5, 5, 1000, 590);

    timerRefresh = new Timer(REFRESH_PERIOD, this);
    timerRefresh.setInitialDelay(0);

    getContentPane().setLayout(
        new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, },
            new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, }));

    JScrollPane scrollPane = new JScrollPane();
    getContentPane().add(scrollPane, "2, 2, fill, fill");

    taLogs = new JTextArea();
    scrollPane.setViewportView(taLogs);
    taLogs.setEditable(false);
    taLogs.setWrapStyleWord(true);
    taLogs.setLineWrap(true);

    taLogs.setText(LogCollector.instance.getLogOutput().getContent());
    {
      btnClose = new JButton(BUNDLE.getString("Button.cancel")); //$NON-NLS-1$
      btnClose.addActionListener(new ActionListener() {
        @Override public void actionPerformed(ActionEvent arg0) {
          setVisible(false);
          extractMediaStruct.cancel();
        }
      });
      getContentPane().add(btnClose, "2, 4, right, default");
    }
    timerRefresh.start();

  }

  boolean isStarted = false;

  @Override public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == timerRefresh) {
      if (isStarted) {
        updateApplicationLog();
      }
      else {
        //start async
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
          @Override protected Void doInBackground() throws Exception {
            long start = System.currentTimeMillis();
            extractMediaStruct.extract();
            long elapsed = System.currentTimeMillis() - start;
            log.info(" ");
            log.info("-----------------------------------------------------------------------------");
            log.info("Extraction ready after {} sec", elapsed / 1000);
            log.info("-----------------------------------------------------------------------------");
            btnClose.setText(BUNDLE.getString("Button.close"));
            return null;
          }
        };
        worker.execute();
        isStarted = true;
      }
    }
  }

  @Override public void pack() {
    // do not let it pack - it looks weird
  }

  private void updateApplicationLog() {
    final boolean append = (logByteCount > 0) ? true : false;
    final LogAppender.LogOutput logOutput = LogCollector.instance.getLogOutput(this.logByteCount);
    logByteCount = logOutput.getByteCount();
    final String content = logOutput.getContent();

    if (content.length() > 0) {
      if (append) {
        final Document doc = taLogs.getDocument();
        try {
          doc.insertString(doc.getLength(), content, null);
        }
        catch (BadLocationException ble) {
          log.error("bad location: ", ble);
        }
      }
      else {
        taLogs.setText(content);
      }
      // scroll to the end of the textarea
      taLogs.setCaretPosition(taLogs.getText().length());
    }
  }
}
