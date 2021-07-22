package org.vincentyeh.IMG2PDF.concrete.util.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileUtilTest {
    private static File normal_file, normal_folder;
    private static File not_exists_file;

    @TempDir
    static Path tempDir;

    @BeforeAll
    public static void initializeAllTestRequire() throws IOException {
        normal_file = Files.createTempFile(tempDir, "testFile", null).toFile();
        normal_folder = Files.createTempDirectory(tempDir,"testFolder").toFile();
        not_exists_file = new File(tempDir.toFile(), "EEEE");
    }


    @Test
    public void TestNullArgumentInput() {
        assertThrows(IllegalArgumentException.class,
                () -> FileUtils.checkExists(null));

        assertThrows(IllegalArgumentException.class,
                () -> FileUtils.isRoot(null));

        assertThrows(IllegalArgumentException.class,
                () -> FileUtils.checkType(null, null));

        assertThrows(IllegalArgumentException.class,
                () -> FileUtils.checkOverwrite(null, null));

        assertThrows(IllegalArgumentException.class,
                () -> FileUtils.makeDirectories(null));
        assertThrows(IllegalArgumentException.class,
                ()->FileUtils.checkFileValidity(null));
    }

    @Test
    public void TestNotExistsFile() {
        assertThrows(FileNotExistsException.class,
                () -> FileUtils.checkType(not_exists_file, WrongFileTypeException.Type.FILE));

        assertThrows(FileNotExistsException.class,
                () -> FileUtils.checkExists(not_exists_file));

        assertDoesNotThrow(() -> FileUtils.checkOverwrite(not_exists_file, "No no no"));
    }

    @Test
    public void checkExistTest() {
        assertDoesNotThrow(() -> FileUtils.checkExists(normal_folder));
        assertDoesNotThrow(() -> FileUtils.checkExists(normal_file));
    }

    @Test
    public void testCheckFileType() {
        Assertions.assertDoesNotThrow(() -> FileUtils.checkType(normal_file, WrongFileTypeException.Type.FILE));
        Assertions.assertThrows(WrongFileTypeException.class,
                () -> FileUtils.checkType(normal_file, WrongFileTypeException.Type.FOLDER));

        Assertions.assertDoesNotThrow(() -> FileUtils.checkType(normal_folder, WrongFileTypeException.Type.FOLDER));
        Assertions.assertThrows(WrongFileTypeException.class,
                () -> FileUtils.checkType(normal_folder, WrongFileTypeException.Type.FILE));
    }

    @Test
    public void makeDirectories() throws IOException {
        File folder = FileUtils.makeDirectories(new File(tempDir.toFile(), "cccc"));
        Assertions.assertFalse(Files.notExists(folder.toPath()));
        Files.delete(folder.toPath());

        Files.createFile(folder.toPath());
        Assertions.assertThrows(MakeDirectoryException.class,
                () -> FileUtils.makeDirectories(folder)
        );

        Files.delete(folder.toPath());
    }

    @Test
    public void checkRoot() {
        File file = new File("E:\\aaa\\bb\\cc");
        Assertions.assertFalse(FileUtils.isRoot(file));
        Assertions.assertTrue(FileUtils.isRoot(file.toPath().getRoot().toFile()));
    }

    @Test
    public void checkGetParent() throws IOException {
        File file = new File(tempDir.toFile(), "gggg");
        Assertions.assertDoesNotThrow(
                () -> FileUtils.getParentFile(file));
        Assertions.assertEquals(file.getParentFile().getName(), FileUtils.getParentFile(file).getName());
        Assertions.assertThrows(FileNotExistsException.class,
                () -> FileUtils.getExistedParentFile(file));

        Files.createFile(file.toPath());
        Assertions.assertDoesNotThrow(
                () -> FileUtils.getExistedParentFile(file));
        Files.delete(file.toPath());

        File root = file.toPath().getRoot().toFile();

        Assertions.assertThrows(NoParentException.class,
                () -> FileUtils.getParentFile(root)
        );

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "|","*","\"","?","<",">","\0",":",
            "Hello|World",
            "\"Hello World\""
    })
    public void checkInvalidFilename(String str) {
        File invalid = new File(str);
        assertThrows(InvalidFileException.class,
                () -> FileUtils.checkFileValidity(invalid));
    }

}
