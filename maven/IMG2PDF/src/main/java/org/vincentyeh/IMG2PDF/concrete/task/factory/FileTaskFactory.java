package org.vincentyeh.IMG2PDF.concrete.task.factory;

import org.vincentyeh.IMG2PDF.concrete.task.exception.FileTaskException;
import org.vincentyeh.IMG2PDF.concrete.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.WrongFileTypeException;
import org.vincentyeh.IMG2PDF.concrete.util.interfaces.NameFormatter;
import org.vincentyeh.IMG2PDF.framework.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.framework.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.framework.task.factory.TaskListFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileTaskFactory extends TaskListFactory<LineFile> {
    private final File dirlist;
    private final Charset charset;

    public FileTaskFactory(File dirlist, Charset charset, PageArgument pageArgument, DocumentArgument documentArgument, FileFilter imageFilter, Comparator<? super File> fileSorter, NameFormatter<File> formatter) {
        super(new LineTaskFactory(pageArgument, documentArgument, imageFilter, fileSorter, formatter));
        this.dirlist = dirlist;
        this.charset = charset;
    }

    @Override
    protected List<LineFile> getSourceList() throws Exception {
        List<LineFile> sources = new ArrayList<>();

        List<String> lines = readAllLines(dirlist, charset);

        for (int index = 0; index < lines.size(); index++) {
            File raw = new File(lines.get(index));

            File result;
            if (!raw.isAbsolute())
                result = new File(FileUtils.getExistedParentFile(dirlist), lines.get(index)).getAbsoluteFile();
            else
                result = raw;

            sources.add(new LineFile(index,result));
        }

        return sources;
    }


    private static List<String> readAllLines(File file, Charset charset) throws FileTaskException {
        try {
            FileUtils.checkExists(file);
            FileUtils.checkType(file, WrongFileTypeException.Type.FILE);
        } catch (Exception e) {
            throw new FileTaskException(file, e);
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
            throw new FileTaskException(file, e);
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
