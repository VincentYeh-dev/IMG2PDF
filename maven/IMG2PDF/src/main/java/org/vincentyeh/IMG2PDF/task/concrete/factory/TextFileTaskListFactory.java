package org.vincentyeh.IMG2PDF.task.concrete.factory;

import org.vincentyeh.IMG2PDF.task.concrete.exception.TextException;
import org.vincentyeh.IMG2PDF.task.concrete.exception.TextFileException;
import org.vincentyeh.IMG2PDF.task.framework.Task;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskFactory;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskListFactory;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.WrongFileTypeException;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TextFileTaskListFactory implements TaskListFactory {


    private final File dirlist;
    private final Charset charset;
    private final TaskFactory factory;

    public TextFileTaskListFactory(TaskFactory factory, File dirlist, Charset charset) {
        this.factory = factory;
        this.dirlist = dirlist;
        this.charset = charset;
    }

    @Override
    public List<Task> create() throws Exception {
        List<Task> tasks = new ArrayList<>();
        List<String> lines = readAllLines(dirlist, charset);
        for (int index = 0; index < lines.size(); index++) {
            try {
                File raw = new File(lines.get(index));

                File result;
                if (!raw.isAbsolute())
                    result = new File(FileUtils.getExistedParentFile(dirlist), lines.get(index)).getAbsoluteFile();
                else
                    result = raw;

                tasks.add(factory.create(result));
            }catch (Exception e){
                throw new TextLineException(e,dirlist,index+1);
            }
        }
        return tasks;
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
