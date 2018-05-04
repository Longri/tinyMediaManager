package de.longri.tinymediamanager;

import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Longri on 30.04.18.
 */
public class ExtractMediaStruct extends AbstractCopy {

  String[] mediaFiles      = new String[] { "mkv", "avi", "ts", "mp4", "vob", "mov" };
  String[] ignoreFiles     = new String[] { "m2ts", "rar", "ds_store", "bup", "ifo", "sub", "idx" };
  String[] ignoreFileNames = new String[] {};

  public ExtractMediaStruct(FileHandle source, FileHandle target) {
    super(target, source);
    if (!this.dummy.exists())
      throw new RuntimeException("Dummy file must exist");
  }

  @Override protected boolean ifMediaFile(FileHandle file) {
    final String fileExt = file.extension().toLowerCase();
    for (String ext : mediaFiles) {
      if (fileExt.equals(ext))
        return true;
    }
    return false;
  }

  @Override protected boolean ifIgnorre(FileHandle file) {
    final String nameWithoutExt = file.nameWithoutExtension().toLowerCase();
    final String ext = file.extension().toLowerCase();
    for (String ignore : ignoreFileNames) {
      if (nameWithoutExt.equals(ignore))
        return true;
    }

    if (ext.startsWith("r")) {
      try {
        int i = Integer.parseInt(ext.replaceAll("r", ""));
        //is integer/ like rar archive
        log.debug("ignore RAR");
        return true;
      }
      catch (NumberFormatException e) {

      }
    }

    for (String ignore : ignoreFiles) {
      if (ext.equals(ignore))
        return true;
    }
    return false;
  }
}
