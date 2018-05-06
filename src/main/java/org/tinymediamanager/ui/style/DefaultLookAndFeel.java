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

import org.tinymediamanager.Globals;
import org.tinymediamanager.ui.IconManager;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

/**
 * Created by Longri on 05.05.18.
 */
public class DefaultLookAndFeel implements LookAndFeel {

    Font font;
    Color fontColor;

    public DefaultLookAndFeel() {
        super();
    }


    @Override
    public void setLookAndFeel() throws Exception {

        String fontFamily = Globals.settings.getStyle().getFontFamily();
        try {
            // sanity check
            fontFamily = Font.decode(fontFamily).getFamily();
        } catch (Exception e) {
            fontFamily = "Dialog";
        }

        int fontSize = Globals.settings.getStyle().getFontSize();
        if (fontSize < 12) {
            fontSize = 12;
        }

        String fontString = fontFamily + " " + fontSize;
        font = new Font(fontFamily, 0, fontSize);
        fontColor = new Color(0,0,0);

        // Get the native look and feel class name
        // String laf = UIManager.getSystemLookAndFeelClassName();
        Properties props = new Properties();
        props.setProperty("controlTextFont", fontString);
        props.setProperty("systemTextFont", fontString);
        props.setProperty("userTextFont", fontString);
        props.setProperty("menuTextFont", fontString);
        // props.setProperty("windowTitleFont", "Dialog bold 20");

        fontSize = Math.round((float) (fontSize * 0.833));
        fontString = fontFamily + " " + fontSize;

        props.setProperty("subTextFont", fontString);
        props.setProperty("backgroundColor", "237 237 237");
        props.setProperty("menuBackgroundColor", "237 237 237");
        props.setProperty("controlBackgroundColor", "237 237 237");
        props.setProperty("menuColorLight", "237 237 237");
        props.setProperty("menuColorDark", "237 237 237");
        props.setProperty("toolbarColorLight", "237 237 237");
        props.setProperty("toolbarColorDark", "237 237 237");
        props.setProperty("tooltipBackgroundColor", "255 255 255");


        props.put("windowDecoration", "system");
        props.put("logoString", "");

        // Get the look and feel class name
        com.jtattoo.plaf.luna.LunaLookAndFeel.setTheme(props);
        String laf = "com.jtattoo.plaf.luna.LunaLookAndFeel";

        LOGGER.debug("Set LookAndFeel => Dark");

        // Install the look and feel
        UIManager.setLookAndFeel(laf);

        //reload images
        IconManager.reloadImages();
    }

    @Override
    public String getName() {
        return NAMES[0];
    }

    @Override
    public Color getRowColor1() {
        return java.awt.Color.white;
    }

    @Override
    public Color getRowColor2() {
        return new Color(241, 245, 250);
    }

    @Override
    public Color getTableGridColor() {
        return new Color(0xd9d9d9);
    }

    @Override
    public Color getTableSelectionForeground() {
        return new Color(0, 0, 0);
    }

    @Override
    public Color getTableSelectionBackground() {
        return new Color(194, 208, 243);
    }

    @Override
    public String getLinkColorString() {
        return "#0000CF";
    }

    @Override
    public Color getFontColor() {
        return fontColor;
    }

    @Override
    public Font getFont() {
        return font;
    }
}
