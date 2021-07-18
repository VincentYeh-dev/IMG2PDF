package org.vincentyeh.IMG2PDF.task.concrete.factory;

import org.vincentyeh.IMG2PDF.task.concrete.exception.TextFileException;
import org.vincentyeh.IMG2PDF.task.concrete.exception.TextLineException;
import org.vincentyeh.IMG2PDF.task.framework.DocumentArgument;
import org.vincentyeh.IMG2PDF.task.framework.PageArgument;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskFactory;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskListFactory;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.WrongFileTypeException;
import org.vincentyeh.IMG2PDF.util.interfaces.NameFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TextFileFactory extends TaskListFactory {
    private final File dirlist;
    private final Charset charset;
    private final PageArgument pageArgument;
    private final DocumentArgument documentArgument;
    private final FileFilter imageFilter;
    private final Comparator<? super File> fileSorter;
    private final NameFormatter<File> formatter;

    public TextFileFactory(File dirlist, Charset charset, PageArgument argument, DocumentArgument argument1, FileFilter imageFilter, Comparator<? super File> fileSorter, NameFormatter<File> formatter) {
        this.dirlist = dirlist;
        this.charset = charset;
        pageArgument = argument;
        documentArgument = argument1;
        this.imageFilter = imageFilter;
        this.fileSorter = fileSorter;
        this.formatter = formatter;
    }

    @Override
    protected List<TaskFactory> generateList() throws Exception {
        List<TaskFactory> modules = new ArrayList<>();

        List<String> lines = readAllLines(dirlist, charset);
        for (int index = 0; index < lines.size(); index++) {
            try {
                File raw = new File(lines.get(index));

                File result;
                if (!raw.isAbsolute())
                    result = new File(FileUtils.getExistedParentFile(dirlist), lines.get(index)).getAbsoluteFile();
                else
                    result = raw;

                modules.add(new TextTaskFactory(pageArgument, documentArgument, result, imageFilter, fileSorter, formatter));
            } catch (Exception e) {
                throw new TextLineException(e, dirlist, index + 1);
            }
        }

        return modules;
    }


    private static List<String> readAllLines(File file, Charset charset) throws TextFileException {
        try {
            FileUtils.checkExists(file);
            FileUtils.checkType(file, WrongFileTypeException.Type.FILE);
        } catch (Exception e) {
            throw new TextFileException(file, e);
        }

        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {
            List<String> result = new ArrayList<>();
            for (; ; ) {
                String line = reader.readLine();
                if (line == null || getFixedLine(line) == null)
                    break;
                result.add(getFixedLine(line));
            }
            return result;
        } catch (Exception e) {
            throw new TextFileException(file, e);
        }
    }

    private static String getFixedLine(String raw) {
        if (raw == null || raw.isEmpty() || raw.trim().isEmpty())
            return null;
        else {
//            remove BOM header in UTF8 line
            String result = raw.replace("\uFEFF", "");
            return result.trim();
        }
    }


}
