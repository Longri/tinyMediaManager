package de.longri.tinymediamanager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tinymediamanager.Launcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Longri on 04.05.2018.
 */
class ChangedMainWindowTest {

  ChangedMainWindow testClass;

  @BeforeEach void setUp() {
    //initial Gdx.files
    if (Gdx.files == null)
      new Launcher();

    //check Gdx file
    assertThat("Gdx.files must be initial!", Gdx.files != null);

    testClass = new ChangedMainWindow("TestClass");
  }

  @AfterEach void tearDown() {
  }

  @Test void extractTargetPath() {

    List<String> source1 = new ArrayList<>();
    source1.add("Test/dir1/dir2/dir3/dir4/");
    source1.add("Test/dir1/dir2/dir3/dir5/");
    source1.add("Test/dir1/dir2/");
    source1.add("Test/dir1/dir2/dir3/dir5/dir6");

    List<String> source2 = new ArrayList<>();
    source2.add("Test/dir1/dir2/dir3/dir4/");
    source2.add("Test/dir1/dir2/dir3/dir5/");
    source2.add("Test/dir1/dir2/dir3/dir5/dir6");


    FileHandle fh = testClass.extractTargetPath(source1,source2);
    assertEquals("Test/dir1/dir2", fh.path());

  }

  @Test void getCombinedPath() {

    List<String> source = new ArrayList<>();

    source.add("Test/dir1/dir2/dir3/dir4/");
    source.add("Test/dir1/dir2/dir3/dir5/");
    source.add("Test/dir1/dir2/");
    source.add("Test/dir1/dir2/dir3/dir5/dir6");

    FileHandle fh = testClass.getCombinedPath(source);
    assertEquals("Test/dir1/dir2", fh.path());

    source.clear();

    source.add("Test/dir1/dir2/dir3/dir4/");
    source.add("Test/dir1/dir2/dir3/dir5/");
    source.add("Test/dir1/dir2/dir3/dir5/dir6");

    fh = testClass.getCombinedPath(source);
    assertEquals("Test/dir1/dir2/dir3", fh.path());

  }
}