package org.vincentyeh.IMG2PDF.task.concrete.factory;

import org.vincentyeh.IMG2PDF.task.concrete.factory.exception.EmptyImagesException;
import org.vincentyeh.IMG2PDF.task.concrete.factory.exception.LineTaskBuilderException;
import org.vincentyeh.IMG2PDF.task.framework.factory.exception.TaskBuilderException;
import org.vincentyeh.IMG2PDF.util.interfaces.NameFormatter;
import org.vincentyeh.IMG2PDF.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskBuilder;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

public class LineTaskBuilder extends TaskBuilder<LineFile> {
    private final FileFilter filter;
    private final Comparator<? super File> sorter;
    private final NameFormatter<File> formatter;
    private final PageArgument pageArgument;
    private final DocumentArgument documentArgument;

    public LineTaskBuilder(DocumentArgument argument1, PageArgument argument, FileFilter filter, Comparator<? super File> sorter, NameFormatter<File> formatter) {
        documentArgument = argument1;
        pageArgument = argument;
        this.filter = filter;
        this.sorter = sorter;
        this.formatter = formatter;
    }

    @Override
    protected PageArgument getPageArgumentFromSource(LineFile lineFile) throws TaskBuilderException {
        return pageArgument;
    }

    @Override
    protected DocumentArgument getDocumentArgumentFromSource(LineFile lineFile) throws TaskBuilderException {
        return documentArgument;
    }

    @Override
    protected File getPdfDestinationFromSource(LineFile lineFile) throws TaskBuilderException {
        try {
            return new File(formatter.format(lineFile.getFile())).getAbsoluteFile();
        } catch (NameFormatter.FormatException e) {
            e.printStackTrace();
            throw new LineTaskBuilderException(new RuntimeException("Format Exception"), lineFile.getFile(), lineFile.getLine());
        }
    }

    @Override
    protected File[] getImagesFromSource(LineFile lineFile) throws TaskBuilderException {
        File[] files = lineFile.getFile().listFiles(filter);
        if (files == null || files.length == 0)
            throw new LineTaskBuilderException(new EmptyImagesException(), lineFile.getFile(), lineFile.getLine());
        Arrays.sort(files, sorter);
        return files;
    }
}
