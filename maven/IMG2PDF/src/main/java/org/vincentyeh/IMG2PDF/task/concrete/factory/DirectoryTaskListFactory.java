package org.vincentyeh.IMG2PDF.task.concrete.factory;

import org.vincentyeh.IMG2PDF.task.framework.factory.exception.TaskListFactoryException;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.WrongFileTypeException;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskBuilder;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskListFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DirectoryTaskListFactory extends TaskListFactory<LineFile,File> {
    private final Charset charset;

    public DirectoryTaskListFactory(Charset charset, TaskBuilder<LineFile> builder) {
        super(builder);
        this.charset = charset;
    }

    @Override
    protected List<LineFile> getSources(File directoryList) throws TaskListFactoryException {
        try {
            List<LineFile> sources = new ArrayList<>();

            List<String> lines = readAllLines(directoryList, charset);

            for (int index = 0; index < lines.size(); index++) {
                File raw = new File(lines.get(index));

                File result;
                if (!raw.isAbsolute())
                    result = new File(FileUtils.getExistedParentFile(directoryList), lines.get(index)).getAbsoluteFile();
                else
                    result = raw;

                sources.add(new LineFile(index, result));
            }

            return sources;
        } catch (Exception e) {
            throw new TaskListFactoryException(directoryList, e);
        }
    }

    private static List<String> readAllLines(File file, Charset charset) throws IOException {
        FileUtils.checkExists(file);
        FileUtils.checkType(file, WrongFileTypeException.Type.FILE);

        BufferedReader reader = Files.newBufferedReader(file.toPath(), charset);
        List<String> result = new ArrayList<>();
        for (; ; ) {
            String line = reader.readLine();
            if (line == null || getFixedLine(line) == null)
                break;
            result.add(getFixedLine(line));
        }
        return result;
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
