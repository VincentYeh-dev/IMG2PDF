package org.vincentyeh.IMG2PDF.task.concrete.factory;

import org.vincentyeh.IMG2PDF.task.concrete.exception.TextException;
import org.vincentyeh.IMG2PDF.task.concrete.exception.TextFileException;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskFactory;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskListTemplateFactory;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.WrongFileTypeException;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextFileTaskTemplateTemplateFactory extends TaskListTemplateFactory<File> {


    private final File dirlist;
    private final Charset charset;

    public TextFileTaskTemplateTemplateFactory(TaskFactory factory, File dirlist, Charset charset) {
        super(factory);
        this.dirlist = dirlist;
        this.charset = charset;
    }

    @Override
    protected List<File> getList() {
        return readAllLines(dirlist,charset).stream().map(File::new).collect(Collectors.toList());
    }

    private static List<String> readAllLines(File file, Charset charset) {
        try {
            FileUtils.checkExists(file);
            FileUtils.checkType(file, WrongFileTypeException.Type.FILE);
        } catch (Exception e) {
            throw new TextFileException(file,e);
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
            throw new TextFileException(file,e);
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


    public static class TextLineException extends TextException {

        private final int line;

        public TextLineException(Throwable cause, File source, int line) {
            super(cause, source);
            this.line = line;
        }

        public int getLine() {
            return line;
        }
    }
}
