package org.vincentyeh.IMG2PDF.concrete.util.file;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;


public class FileNameFormatterTest {
    private static File normal;

    @BeforeAll
    static void initialize() {
        normal = new File("E:\\aa\\bb\\cc\\dd.txt");
    }

    @Test
    public void testNormalInputFormat() {
        try {
            FileNameFormatter fileNameFormatter = new FileNameFormatter("<NAME>");
            Assertions.assertEquals(fileNameFormatter.format(normal), "dd");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileNameFormatter fileNameFormatter = new FileNameFormatter("<PARENT{0}>");
            Assertions.assertEquals(fileNameFormatter.format(normal), "cc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileNameFormatter fileNameFormatter = new FileNameFormatter("<PARENT{1}>");
            Assertions.assertEquals(fileNameFormatter.format(normal), "bb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileNameFormatter fileNameFormatter = new FileNameFormatter("<PARENT{2}>");
            Assertions.assertEquals(fileNameFormatter.format(normal), "aa");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileNameFormatter fileNameFormatter = new FileNameFormatter("<ROOT>");
            Assertions.assertEquals(fileNameFormatter.format(normal), "E:\\");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void usingNotMappedParent() {
        FileNameFormatter fileNameFormatter = new FileNameFormatter("<PARENT{3}>");
        Assertions.assertThrows(FileNameFormatter.NotMappedPattern.class,
                ()->fileNameFormatter.format(normal));

    }


}
