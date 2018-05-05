/*
 * Copyright 2018 Longri
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
package org.tinymediamanager.ui.style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.core.AbstractModelObject;
import org.tinymediamanager.ui.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Longri on 05.05.18.
 */
public class Style extends AbstractModelObject {

    Logger LOGGER = LoggerFactory.getLogger(Style.class);

    private final static String UNKNOWN_STYLE = "unknown";

    private String lookAndFeelName = UNKNOWN_STYLE;
    private LookAndFeel lookAndFeel = new DefaultLookAndFeel();
    private int fontSize = 12;
    private String fontFamily = "Dialog";

    public Style() {
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int newValue) {
        int oldValue = fontSize;
        fontSize = newValue;
        firePropertyChange("fontSize", oldValue, newValue);
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String newValue) {
        String oldValue = this.fontFamily;
        this.fontFamily = newValue;
        firePropertyChange("fontFamily", oldValue, newValue);
    }

    public String getLookAndFeelName() {
        return lookAndFeelName;
    }

    public void setLookAndFeelName(String newValue) {
        String oldValue = this.lookAndFeelName;
        this.lookAndFeelName = newValue;
        firePropertyChange("lookAndFeelName", oldValue, newValue);
        if (oldValue.equals(UNKNOWN_STYLE)) {
            // set from Settings loader, so set LookAndFeel
            setLookAndFeel(null, this.lookAndFeelName);
        }
    }

    public void setLookAndFeel(final Component component) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Style.this.lookAndFeel.setLookAndFeel();
                    if (MainWindow.getActiveInstance() != null)
                        SwingUtilities.updateComponentTreeUI(MainWindow.getActiveInstance());
                    if (component != null) SwingUtilities.updateComponentTreeUI(component);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LookAndFeel getLookAndFeel() {
        return lookAndFeel;
    }

    public void setLookAndFeel(final Component component, String newLookandFeel) {
        switch (newLookandFeel) {
            case "Default":
                lookAndFeel = new DefaultLookAndFeel();
                break;
            case "Dark":
                lookAndFeel = new DarkLookAndFeel();
                break;
        }
        setLookAndFeelName(lookAndFeel.getName());
        LOGGER.info("Change LookAndFeel to {}", lookAndFeel.getName());

        try {
            setLookAndFeel(component);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
