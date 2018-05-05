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

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

/**
 * Created by Longri on 05.05.18.
 */
public class DefaultStyle extends AbstractStyle {


    public DefaultStyle() {
        super();
        fontSize = 12;
        fontFamily = "Dialog";
    }

    @Override
    public void setLookAndFeel() throws Exception {

        Globals.settings.setStyle(this);

        try {
            // sanity check
            fontFamily = Font.decode(fontFamily).getFamily();
        } catch (Exception e) {
            fontFamily = "Dialog";
        }

        if (fontSize < 12) {
            fontSize = 12;
        }

        String fontString = fontFamily + " " + fontSize;

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

        // Install the look and feel
        UIManager.setLookAndFeel(laf);
    }
}
