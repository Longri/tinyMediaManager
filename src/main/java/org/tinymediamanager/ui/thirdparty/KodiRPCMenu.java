package org.tinymediamanager.ui.thirdparty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tinymediamanager.thirdparty.KodiRPC;

public class KodiRPCMenu {

  /**
   * Adds Kodi RPC menu structure
   * 
   * @return
   */
  public static JMenu KodiMenu() {
    String version = KodiRPC.getInstance().getVersion();
    JMenu m = new JMenu(version);
    m.add(Application());
    m.add(System());
    return m;
  }

  private static JMenu Application() {
    JMenu m = new JMenu("Application");

    JMenuItem i = new JMenuItem("Quit");
    i.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        KodiRPC.getInstance().ApplicationQuit();
      }
    });
    m.add(i);

    i = new JMenuItem("Mute / UnMute");
    i.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        KodiRPC.getInstance().ApplicationMute();
      }
    });
    m.add(i);

    m.add(Volume());

    return m;
  }

  private static JMenu Volume() {
    JMenu m = new JMenu("Volume");

    JMenuItem i = new JMenuItem("100%");
    i.addActionListener(new ApplicationVolumeListener(100));
    m.add(i);
    i = new JMenuItem(" 90%");
    i.addActionListener(new ApplicationVolumeListener(90));
    m.add(i);
    i = new JMenuItem(" 80%");
    i.addActionListener(new ApplicationVolumeListener(80));
    m.add(i);
    i = new JMenuItem(" 70%");
    i.addActionListener(new ApplicationVolumeListener(70));
    m.add(i);
    i = new JMenuItem(" 60%");
    i.addActionListener(new ApplicationVolumeListener(60));
    m.add(i);
    i = new JMenuItem(" 50%");
    i.addActionListener(new ApplicationVolumeListener(50));
    m.add(i);
    i = new JMenuItem(" 40%");
    i.addActionListener(new ApplicationVolumeListener(40));
    m.add(i);
    i = new JMenuItem(" 30%");
    i.addActionListener(new ApplicationVolumeListener(30));
    m.add(i);
    i = new JMenuItem(" 20%");
    i.addActionListener(new ApplicationVolumeListener(20));
    m.add(i);
    i = new JMenuItem(" 10%");
    i.addActionListener(new ApplicationVolumeListener(10));
    m.add(i);

    return m;
  }

  private static class ApplicationVolumeListener implements ActionListener {
    private int vol;

    public ApplicationVolumeListener(int vol) {
      this.vol = vol;
    }

    public void actionPerformed(ActionEvent e) {
      KodiRPC.getInstance().ApplicationVolume(vol);
    }
  }

  private static JMenu System() {
    JMenu m = new JMenu("System");

    JMenuItem i = new JMenuItem("Hibernate");
    i.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        KodiRPC.getInstance().SystemHibernate();
      }
    });
    m.add(i);

    i = new JMenuItem("Reboot");
    i.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        KodiRPC.getInstance().SystemReboot();
      }
    });
    m.add(i);

    i = new JMenuItem("Shutdown");
    i.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        KodiRPC.getInstance().SystemShutdown();
      }
    });
    m.add(i);

    i = new JMenuItem("Suspend");
    i.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        KodiRPC.getInstance().SystemSuspend();
      }
    });
    m.add(i);

    return m;
  }

}
