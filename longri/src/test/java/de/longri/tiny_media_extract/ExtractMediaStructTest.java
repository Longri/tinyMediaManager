package de.longri.tiny_media_extract;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.longri.tinymediamanager.AbstractCopy;
import de.longri.tinymediamanager.ExtractMediaStruct;
import de.longri.tinymediamanager.WriteNfoBack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.tinymediamanager.Launcher;

import static org.hamcrest.MatcherAssert.assertThat;

class ExtractMediaStructTest {

    static final boolean extract = false;
    static final boolean writeBack = true;


    @BeforeAll
    static void beforeAll() {

        new Launcher();

        //check Gdx file
        assertThat("Gdx.files must be initial!", Gdx.files != null);

        //dummy resource must exist
        assertThat("dummy.mkv must exist", Gdx.files.internal("dummy.mkv").exists());
    }


    @Test
    void test() {

        FileHandle dummyFile = Gdx.files.internal("dummy.mkv");
        assertThat("Dummy file must exist", dummyFile.exists());
        long dummyLength = dummyFile.length();

        FileHandle source = Gdx.files.absolute(Gdx.files.internal("src/test/resources/mediaStruct").file().getAbsolutePath());
        assertThat("Source must exist", source.exists());

        FileHandle target = Gdx.files.absolute(Gdx.files.internal("testTarget").file().getAbsolutePath());
        if (target.exists()) {
            target.deleteDirectory();
        }
        assertThat("Target must not exist, with start!", !target.exists());


        AbstractCopy extractMediaStruct = new ExtractMediaStruct(source, target);
        assertThat("ExtractMediaStruct must constructable", extractMediaStruct != null);
        extractMediaStruct.extract();

        assertThat("Target folder must exist", target.exists());
        assertThat("Target/TV_Series folder must exist", target.child("TV_Series").exists());
        FileHandle castleFolder = target.child("TV_Series").child("Castle");
        assertThat("Target/TV_Series/Castle folder must exist", castleFolder.exists());

        FileHandle[] castleFileList = castleFolder.list();
        assertThat("Castle folder must include 15 files", castleFileList.length == 15);


        // check if Media file replaced with dummy media file
        assertThat("must", castleFolder.child("Castle_S04_E01_Neuanfang.mkv").length() == dummyLength);
        assertThat("must", castleFolder.child("Castle_S04_E02_Helden und Bosewichter.mkv").length() == dummyLength);
        assertThat("must", castleFolder.child("Castle_S04_E03_Kopflos.mkv").length() == dummyLength);
        assertThat("must", castleFolder.child("Castle_S04_E04_Dem Dreifachmorder auf der Spur.mkv").length() == dummyLength);
        assertThat("must", castleFolder.child("Castle_S04_E05_Im Auge des Betrachters.mkv").length() == dummyLength);

        target.deleteDirectory();
    }

    @Test
    void writeBackTest() {
        FileHandle source = Gdx.files.absolute(Gdx.files.internal("src/test/resources/mediaStruct").file().getAbsolutePath());
        assertThat("Source must exist", source.exists());

        FileHandle target = Gdx.files.absolute(Gdx.files.internal("testTargetWriteBack").file().getAbsolutePath());
        if (target.exists()) {
            target.deleteDirectory();
        }
        assertThat("Target must not exist, with start!", !target.exists());

        AbstractCopy writeBack = new WriteNfoBack(source, target);
        assertThat("ExtractMediaStruct must constructable", writeBack != null);
        writeBack.extract();

        assertThat("Target folder must exist", target.exists());
        assertThat("Target/TV_Series folder must exist", target.child("TV_Series").exists());
        FileHandle castleFolder = target.child("TV_Series").child("Castle");
        assertThat("Target/TV_Series/Castle folder must exist", castleFolder.exists());

        FileHandle[] castleFileList = castleFolder.list();
        assertThat("Castle folder must include 10 files", castleFileList.length == 10);

        target.deleteDirectory();
    }

//    @Test
//    void extractServer() {
//
//        FileHandle source = Gdx.files.absolute("/Volumes/Media");
////        FileHandle source = Gdx.files.absolute("/Volumes/HDD_DATA/test");
//        FileHandle target = Gdx.files.absolute("/Volumes/HDD_DATA/ExtractedMediaDB");
//
//        assertThat("Source must exist", source.exists());
//        assertThat("Target must exist", target.exists());
//
//        if (extract) new ExtractMediaStruct(source, target).extract();
//
//        if (writeBack) new WriteNfoBack(target, source).extract();
//
//    }

}