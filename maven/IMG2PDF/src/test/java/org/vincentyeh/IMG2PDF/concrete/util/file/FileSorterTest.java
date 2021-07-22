package org.vincentyeh.IMG2PDF.concrete.util.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class FileSorterTest {
    @TempDir
    static Path tempDir;

    @Test
    public void testNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new FileSorter(null, null));
        assertThrows(IllegalArgumentException.class,
                () -> new FileSorter(FileSorter.Sortby.NAME, FileSorter.Sequence.INCREASE).compare(null, null));

    }

    @ParameterizedTest
    @MethodSource("provider")
    public void testNumeric(int n1, int n2, File f1, File f2) {
        FileSorter sorter = new FileSorter(FileSorter.Sortby.NUMERIC, FileSorter.Sequence.INCREASE);
        assertEquals(sorter.compare(f1, f2), n1 - n2);
        FileSorter sorter2 = new FileSorter(FileSorter.Sortby.NUMERIC, FileSorter.Sequence.DECREASE);
        assertEquals(sorter2.compare(f1, f2), n2 - n1);
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void testName(int n1, int n2, File f1, File f2) {
        FileSorter sorter = new FileSorter(FileSorter.Sortby.NAME, FileSorter.Sequence.INCREASE);
        assertEquals(sorter.compare(f1, f2), f1.getName().compareTo(f2.getName()));
        FileSorter sorter2 = new FileSorter(FileSorter.Sortby.NAME, FileSorter.Sequence.DECREASE);
        assertEquals(sorter2.compare(f1, f2), f2.getName().compareTo(f1.getName()));
    }

    @ParameterizedTest
    @MethodSource("DateFileProvider")
    public void testDate(long n1, long n2, File f1, File f2) {
        FileSorter sorter = new FileSorter(FileSorter.Sortby.DATE, FileSorter.Sequence.INCREASE);
        assertEquals(sorter.compare(f1, f2), (int) (n1 - n2));
        FileSorter sorter2 = new FileSorter(FileSorter.Sortby.DATE, FileSorter.Sequence.DECREASE);
        assertEquals(sorter2.compare(f1, f2), (int) (n2 - n1));
    }


    private static Stream<Arguments> provider() {
        List<Arguments> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int n1 = new Random().nextInt(99999);
            int n2 = new Random().nextInt(99999);

            File f1 = new File(n1 + ".txt");
            File f2 = new File(n2 + ".txt");
            list.add(arguments(n1, n2, f1, f2));
        }
        return list.stream();
    }

    private static Stream<Arguments> DateFileProvider() throws IOException {
        List<Arguments> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            File f1;
            long t1;
            do {
                t1=generateRandomDate().getTime();
                f1 = new File(tempDir.toFile(),  t1+ ".txt");
            } while (f1.exists());
            Files.createFile(f1.toPath());
            boolean a = f1.setLastModified(t1);

            File f2;
            long t2;
            do {
                t2=generateRandomDate().getTime();
                f2 = new File(tempDir.toFile(),  t2+ ".txt");
            } while (f2.exists());
            Files.createFile(f2.toPath());
            boolean b = f2.setLastModified(t2);

            list.add(arguments(t1,t2, f1, f2));
        }
        return list.stream();
    }

    private static Date generateRandomDate() {
        Random r = new Random();
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(java.util.Calendar.MONTH, Math.abs(r.nextInt()) % 12);
        c.set(java.util.Calendar.DAY_OF_MONTH, Math.abs(r.nextInt()) % 30);
        c.setLenient(true);
        return c.getTime();
    }
}
