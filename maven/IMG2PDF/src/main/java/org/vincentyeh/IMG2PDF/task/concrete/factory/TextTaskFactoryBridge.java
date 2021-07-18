package org.vincentyeh.IMG2PDF.task.concrete.factory;

import org.vincentyeh.IMG2PDF.task.concrete.exception.EmptyImagesException;
import org.vincentyeh.IMG2PDF.task.framework.DocumentArgument;
import org.vincentyeh.IMG2PDF.task.framework.PageArgument;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.FileNotExistsException;
import org.vincentyeh.IMG2PDF.util.file.exception.InvalidFileException;
import org.vincentyeh.IMG2PDF.util.file.exception.WrongFileTypeException;
import org.vincentyeh.IMG2PDF.util.interfaces.NameFormatter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class TextTaskFactoryBridge extends StandardTaskFactoryBridge{
    public TextTaskFactoryBridge(PageArgument argument, DocumentArgument argument1, File source, FileFilter filter, Comparator<? super File> sorter, NameFormatter<File> formatter) throws EmptyImagesException, IOException, NameFormatter.FormatException{
        super(argument, argument1,importSortedImagesFiles(source,filter,sorter),new File(formatter.format(source)).getAbsoluteFile());
    }

    private static File[] importSortedImagesFiles(File source_directory, FileFilter imageFilter, Comparator<? super File> fileSorter) throws EmptyImagesException, WrongFileTypeException, InvalidFileException, FileNotExistsException {
        FileUtils.checkExists(source_directory);
        FileUtils.checkType(source_directory, WrongFileTypeException.Type.FOLDER);

        File[] files = source_directory.listFiles(imageFilter);

        if (files == null || files.length == 0)
            throw new EmptyImagesException("No image was found in: " + source_directory);

        Arrays.sort(files, fileSorter);

        return files;
    }
}
