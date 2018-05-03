package de.longri.tinymediamanager;

import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Longri on 02.05.18.
 */
public class WriteNfoBack extends AbstractCopy {

    String[] imageFiles = new String[]{"jpg", "png"};

    public WriteNfoBack(FileHandle source, FileHandle target) {
        super(target, source);
    }

    @Override
    protected boolean ifMediaFile(FileHandle file) {
        return false;
    }

    @Override
    protected boolean ifIgnorre(FileHandle file) {
        //ignore all without *.nfo and image files files
        String ext = file.extension();
        boolean ignore = true;

        if (ext.toLowerCase().equals("nfo")) ignore = false;

        for (String image : imageFiles) {
            if (ext.toLowerCase().equals(image)) {
                ignore = false;
                break;
            }
        }


        return ignore;
    }
}
