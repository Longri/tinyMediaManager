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

import com.jtattoo.plaf.noire.NoireLookAndFeel;
import org.tinymediamanager.Globals;
import org.tinymediamanager.ui.IconManager;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

/**
 * Created by Longri on 05.05.18.
 */
public class DarkLookAndFeel implements LookAndFeel {


    public DarkLookAndFeel() {
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

        Properties props = new Properties();
        props.setProperty("controlTextFont", fontString);
        props.setProperty("systemTextFont", fontString);
        props.setProperty("userTextFont", fontString);
        props.setProperty("menuTextFont", fontString);
        // props.setProperty("windowTitleFont", "Dialog bold 20");

        fontSize = Math.round((float) (fontSize * 0.833));
        fontString = fontFamily + " " + fontSize;

        props.setProperty("subTextFont", fontString);
        props.setProperty("backgroundColor", "30 30 30");
        props.setProperty("menuBackgroundColor", "30 30 30");
        props.setProperty("controlBackgroundColor", "30 30 30");
        props.setProperty("menuColorLight", "30 30 30");
        props.setProperty("menuColorDark", "30 30 30");
        props.setProperty("toolbarColorLight", "30 30 30");
        props.setProperty("toolbarColorDark", "30 30 30");
        props.setProperty("tooltipBackgroundColor", "30 10 10");

        props.setProperty("buttonForegroundColor", "255 255 200");
        props.setProperty("buttonBackgroundColor", "0 0 0");
        props.setProperty("buttonColorLight", "30 30 30");
        props.setProperty("buttonColorDark", "10 30 30");


//        try
        props.setProperty("tooltipForegroundColor", "255 0 0");
        props.setProperty("tooltipBackgroundColor", "0 0 255");
        props.setProperty("tooltipCastShadow", "0 0 255");

        props.setProperty("frameColor", "0 0 0");












//        value: red green blue. Sample: "255 128 255"
//        tooltipCastShadow

//        value: on if you want a cast shadow instead of the default shadow.
//                tooltipBorderSize
//                tooltipShadowSize

        props.put("windowDecoration", "system");
        props.put("logoString", "");

        // Get the look and feel class name
        com.jtattoo.plaf.noire.NoireLookAndFeel.setCurrentTheme(props);

        String noire = "com.jtattoo.plaf.noire.NoireLookAndFeel";

        // Install the look and feel
        UIManager.setLookAndFeel(noire);

        //reload images
        IconManager.reloadImages();
    }

    @Override
    public String getName() {
        return NAMES[1];
    }

    @Override
    public Color getRowColor1() {
        return Color.black;
    }

    @Override
    public Color getRowColor2() {
        return new Color(70, 70, 70);
    }

    @Override
    public Color getTableGridColor() {
        return new Color(100, 100, 100);
    }

    @Override
    public Color getTableSelectionForeground() {
        return new Color(255, 200, 200);
    }

    @Override
    public Color getTableSelectionBackground() {
        return new Color(60, 10, 10);
    }

    @Override
    public String getLinkColorString() {
        return "#6097bb"; //96 151 187
    }
}
