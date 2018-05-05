package org.tinymediamanager.ui.style;

import java.awt.*;

/**
 * Created by Longri on 05.05.18.
 */
public interface LookAndFeel {

    public final static String[] NAMES = new String[]{"Default", "Dark"};

    void setLookAndFeel() throws Exception;

    String getName();

    Color getRowColor1();

    Color getRowColor2();

    Color getTableGridColor();

    Color getTableSelectionForeground();

    Color getTableSelectionBackground();

    String getLinkColorString();
}
