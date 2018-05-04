package de.longri.tinymediamanager;

import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Longri on 02.05.18.
 */
public class WriteNfoBack extends AbstractCopy {

  String[] imageFiles = new String[] { "jpg", "png" };

  public WriteNfoBack(FileHandle source, FileHandle target) {
    super(target, source);
  }

  @Override protected boolean ifMediaFile(FileHandle file) {
    return false;
  }

  @Override protected boolean ifIgnorre(FileHandle file) {
    //ignore all without *.nfo and image files files
    String ext = file.extension().toLowerCase();

    if (ext.equals("nfo"))
      return false;

    boolean ignore = true;
    for (String image : imageFiles) {
      if (ext.equals(image)) {
        ignore = false;
        break;
      }
    }
    return ignore;
  }
}
