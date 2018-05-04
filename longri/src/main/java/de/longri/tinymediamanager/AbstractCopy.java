package de.longri.tinymediamanager;

import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Longri on 01.05.18.
 */
public abstract class AbstractCopy {
  final FileHandle    dummy;
  final FileHandle    source;
  final FileHandle    target;
  final AtomicBoolean CANCEL = new AtomicBoolean(false);
  Logger log = LoggerFactory.getLogger(AbstractCopy.class);

  public AbstractCopy(FileHandle target, FileHandle source) {
    this.target = target;
    this.source = source;
    this.dummy = Gdx.files.internal("dummy.mkv");
  }

  public void extract() {
    //create target directory
    target.mkdirs();
    copyFolder(source);
  }

  private void copyFolder(FileHandle dir) {

    if (CANCEL.get())
      return;

    if (!dir.exists()) {
      //create this folder
      dir.mkdirs();
    }

    if (!dir.isDirectory()) {
      throw new RuntimeException("Source FileHandle {" + dir.file().getAbsolutePath() + "} must be a directory");
    }

    String absoluteDirPath = dir.file().getAbsolutePath();
    String targetDirPath = absoluteDirPath.replace(this.source.file().getAbsolutePath(), "");

    //create dir on target folder
    FileHandle targetDir = target.child(targetDirPath);
    targetDir.mkdirs();

    FileHandle[] files = dir.list();
    for (FileHandle f : files) {
      if (CANCEL.get())
        return;
      if (f.isDirectory()) {
        copyFolder(f);
      }
      else {
        if (ifIgnorre(f)) {
          log.debug("IGNORE :{}", f.name());
          continue;
        }
        if (ifMediaFile(f)) {
          //copy dummy media file
          FileHandle copyTarget = targetDir.child(f.name());
          this.dummy.copyTo(copyTarget);
          log.debug("Write Dummy: {}", f.name());
        }
        else {
          //copy
          FileHandle copyTarget = targetDir.child(f.name());
          try {
            f.copyTo(copyTarget);
            log.debug("Copy file: {}", f.name());
          }
          catch (Exception e) {
            log.error("FAILED to copy: {}", f.name());
          }
        }
      }
    }
  }

  protected abstract boolean ifMediaFile(FileHandle file);

  protected abstract boolean ifIgnorre(FileHandle file);

  public void cancel() {
    CANCEL.set(true);
  }
}
