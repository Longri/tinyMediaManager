package de.longri.tinymediamanager;

import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Longri on 30.04.18.
 */
public class ExtractMediaStruct extends AbstractCopy {

    String[] mediaFiles = new String[]{"mkv", "avi", "ts", "mp4", "vob"};
    String[] ignoreFiles = new String[]{"m2ts","rar"};


    public ExtractMediaStruct(FileHandle source, FileHandle target) {
        super(target, source);
        if (!this.dummy.exists()) throw new RuntimeException("Dummy file must exist");
    }


    @Override
    protected boolean ifMediaFile(FileHandle file) {
        for (String ext : mediaFiles) {
            if (file.extension().toLowerCase().equals(ext)) return true;
        }
        return false;
    }


    @Override
    protected boolean ifIgnorre(FileHandle file) {
        String ext=file.extension();

        if(ext.toLowerCase().startsWith("r")){
            try {
                int i=Integer.parseInt(ext.replaceAll("r",""));
                //is integer/ like rar archive
                log.debug("ignore RAR");
                return true;
            } catch (NumberFormatException e) {

            }
        }

        for (String ignore : ignoreFiles) {
            if (ext.toLowerCase().equals(ignore)) return true;
        }
        return false;
    }
}
