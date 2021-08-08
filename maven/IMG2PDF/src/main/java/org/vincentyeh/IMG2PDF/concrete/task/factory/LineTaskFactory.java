package org.vincentyeh.IMG2PDF.concrete.task.factory;

import org.vincentyeh.IMG2PDF.concrete.task.exception.EmptyImagesException;
import org.vincentyeh.IMG2PDF.concrete.task.exception.LineException;
import org.vincentyeh.IMG2PDF.concrete.util.interfaces.NameFormatter;
import org.vincentyeh.IMG2PDF.framework.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.framework.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.framework.task.Task;
import org.vincentyeh.IMG2PDF.framework.task.factory.TaskFactory;
import org.vincentyeh.IMG2PDF.concrete.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.WrongFileTypeException;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

public class LineTaskFactory implements TaskFactory<LineFile> {
    private final PageArgument pageArgument;
    private final DocumentArgument documentArgument;
    private final FileFilter filter;
    private final Comparator<? super File> sorter;
    private final NameFormatter<File> formatter;

    public LineTaskFactory(PageArgument pageArgument, DocumentArgument documentArgument, FileFilter filter, Comparator<? super File> sorter, NameFormatter<File> formatter) {
        this.pageArgument = pageArgument;
        this.documentArgument = documentArgument;
        this.filter = filter;
        this.sorter = sorter;
        this.formatter = formatter;
    }

    private File[] importSortedImagesFiles(File source_directory) throws EmptyImagesException {

        File[] files = source_directory.listFiles(filter);

        if (files == null || files.length == 0)
            throw new EmptyImagesException("No image was found in: " + source_directory);

        Arrays.sort(files,sorter);

        return files;
    }

    @Override
    public Task create(LineFile source) throws Exception {
        File file=source.getFile();
        try {
            FileUtils.checkExists(file);
            FileUtils.checkType(file, WrongFileTypeException.Type.FOLDER);

            File[] images = importSortedImagesFiles(file);
            File destination = new File(formatter.format(file)).getAbsoluteFile();

            return new Task() {
                @Override
                public DocumentArgument getDocumentArgument() {
                    return documentArgument;
                }

                @Override
                public PageArgument getPageArgument() {
                    return pageArgument;
                }

                @Override
                public File[] getImages() {
                    return images;
                }

                @Override
                public File getPdfDestination() {
                    return destination;
                }
            };

        } catch (Exception e) {
            throw new LineException(e,file,source.getLine());
        }
    }
}
