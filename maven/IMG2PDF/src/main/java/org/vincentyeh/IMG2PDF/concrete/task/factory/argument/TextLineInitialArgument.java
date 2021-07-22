package org.vincentyeh.IMG2PDF.concrete.task.factory.argument;

import org.vincentyeh.IMG2PDF.framework.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.framework.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.framework.task.factory.InitialArgument;
import org.vincentyeh.IMG2PDF.concrete.util.interfaces.NameFormatter;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

public class TextLineInitialArgument implements InitialArgument {
    private final PageArgument pageArgument;
    private final DocumentArgument documentArgument;
    private final FileFilter filter;
    private final Comparator<? super File> sorter;
    private final NameFormatter<File> formatter;

    public TextLineInitialArgument(PageArgument argument, DocumentArgument argument1, FileFilter filter, Comparator<? super File> sorter, NameFormatter<File> formatter) {
        pageArgument = argument;
        documentArgument = argument1;
        this.filter = filter;
        this.sorter = sorter;
        this.formatter = formatter;
    }

    public PageArgument getPageArgument() {
        return pageArgument;
    }

    public DocumentArgument getDocumentArgument() {
        return documentArgument;
    }

    public FileFilter getFilter() {
        return filter;
    }

    public Comparator<? super File> getSorter() {
        return sorter;
    }

    public NameFormatter<File> getFormatter() {
        return formatter;
    }
}
