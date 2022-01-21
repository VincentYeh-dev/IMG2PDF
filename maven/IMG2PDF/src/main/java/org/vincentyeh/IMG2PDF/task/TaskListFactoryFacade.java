package org.vincentyeh.IMG2PDF.task;

import org.vincentyeh.IMG2PDF.pdf.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.task.concrete.factory.DirectoryTaskListFactory;
import org.vincentyeh.IMG2PDF.task.concrete.factory.LineTaskBuilder;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskListFactory;
import org.vincentyeh.IMG2PDF.util.file.FileNameFormatter;

import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.util.Comparator;

public class TaskListFactoryFacade {
    private TaskListFactoryFacade() {

    }

    public static TaskListFactory<?, File> createDirectoryTaskListFactory(Charset dir_list_read_charset, DocumentArgument documentArgument, PageArgument pageArgument, FileFilter filter, Comparator<File> fileSorter, String fileNamePattern) {
        return new DirectoryTaskListFactory(dir_list_read_charset, new LineTaskBuilder(documentArgument, pageArgument, filter, fileSorter, new FileNameFormatter(fileNamePattern)));
    }
}
