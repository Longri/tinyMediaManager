package org.tinymediamanager.ui.style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by Longri on 05.05.18.
 */
public interface LookAndFeel {

    Logger LOGGER = LoggerFactory.getLogger(LookAndFeel.class);

    String[] NAMES = new String[]{"Default", "Dark"};

    void setLookAndFeel() throws Exception;

    String getName();

    Color getRowColor1();

    Color getRowColor2();

    Color getTableGridColor();

    Color getTableSelectionForeground();

    Color getTableSelectionBackground();

    String getLinkColorString();

    Color getFontColor();

    Font getFont();
}