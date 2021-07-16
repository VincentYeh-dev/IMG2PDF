package org.vincentyeh.IMG2PDF.task.concrete.factory;

import org.vincentyeh.IMG2PDF.task.concrete.exception.TextException;
import org.vincentyeh.IMG2PDF.task.concrete.exception.EmptyImagesException;
import org.vincentyeh.IMG2PDF.task.framework.*;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskFactory;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.WrongFileTypeException;
import org.vincentyeh.IMG2PDF.util.interfaces.NameFormatter;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

public class TextTaskFactory implements TaskFactory {
    private final FileFilter imageFilter;
    private final Comparator<? super File> fileSorter;
    private final NameFormatter<File> formatter;
    private final PageArgument pageArgument;
    private final DocumentArgument documentArgument;

    public TextTaskFactory(FileFilter imageFilter, Comparator<? super File> fileSorter, NameFormatter<File> formatter, PageArgument pageArgument, DocumentArgument documentArgument) {
        this.imageFilter = imageFilter;
        this.fileSorter = fileSorter;
        this.formatter = formatter;
        this.pageArgument = pageArgument;
        this.documentArgument = documentArgument;
    }


    @Override
    public Task create(Object... objects) {
        File source = (File) objects[0];
        try {
            FileUtils.checkExists(source);
            FileUtils.checkType(source, WrongFileTypeException.Type.FOLDER);

            return createTask(
                    importSortedImagesFiles(source),
                    new File(formatter.format(source)).getAbsoluteFile());

        } catch (Exception e) {
            throw new TextException(e,source);
        }
    }


    private File[] importSortedImagesFiles(File source_directory) throws EmptyImagesException {
        File[] files = source_directory.listFiles(imageFilter);

        if (files == null || files.length == 0)
            throw new EmptyImagesException("No image was found in: " + source_directory);

        Arrays.sort(files, fileSorter);

        return files;
    }

    private Task createTask(File[] images, File pdf_destination) {
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
                return pdf_destination;
            }
        };
    }
}
