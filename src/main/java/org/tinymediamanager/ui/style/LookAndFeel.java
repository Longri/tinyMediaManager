package org.tinymediamanager.ui.style;

/**
 * Created by Longri on 05.05.18.
 */
public interface LookAndFeel {

    public final static String[] NAMES = new String[]{"Default","Dark"};

    void setLookAndFeel() throws Exception;

    String getName();
}
