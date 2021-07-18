package org.vincentyeh.IMG2PDF.task.concrete.factory;

import org.vincentyeh.IMG2PDF.task.concrete.exception.EmptyImagesException;
import org.vincentyeh.IMG2PDF.task.framework.DocumentArgument;
import org.vincentyeh.IMG2PDF.task.framework.PageArgument;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskFactoryBridge;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.FileNotExistsException;
import org.vincentyeh.IMG2PDF.util.file.exception.InvalidFileException;
import org.vincentyeh.IMG2PDF.util.file.exception.WrongFileTypeException;
import org.vincentyeh.IMG2PDF.util.interfaces.NameFormatter;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

public class StandardTaskFactoryBridge extends TaskFactoryBridge {

    private final PageArgument pageArgument;
    private final DocumentArgument documentArgument;
    private final File[] images;
    private final File destination;

    public StandardTaskFactoryBridge(PageArgument argument, DocumentArgument argument1, File source, FileFilter imageFilter, Comparator<? super File> fileSorter, NameFormatter<File> formatter) throws EmptyImagesException, NameFormatter.FormatException, WrongFileTypeException, InvalidFileException, FileNotExistsException {
        pageArgument = argument;
        documentArgument = argument1;

        FileUtils.checkExists(source);
        FileUtils.checkType(source, WrongFileTypeException.Type.FOLDER);
        this.images = importSortedImagesFiles(source, imageFilter, fileSorter);
        this.destination = new File(formatter.format(source)).getAbsoluteFile();
    }


    private File[] importSortedImagesFiles(File source_directory, FileFilter imageFilter, Comparator<? super File> fileSorter) throws EmptyImagesException {
        File[] files = source_directory.listFiles(imageFilter);

        if (files == null || files.length == 0)
            throw new EmptyImagesException("No image was found in: " + source_directory);

        Arrays.sort(files, fileSorter);

        return files;
    }

    @Override
    public PageArgument generatePageArgument() {
        return pageArgument;
    }

    @Override
    public DocumentArgument generateDocumentArgument() {
        return documentArgument;
    }

    @Override
    public File[] generateImages() {
        return images;
    }

    @Override
    public File generateDestination() {
        return destination;
    }
}
