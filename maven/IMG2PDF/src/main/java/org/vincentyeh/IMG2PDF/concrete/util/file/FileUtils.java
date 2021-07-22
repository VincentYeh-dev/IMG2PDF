package org.vincentyeh.IMG2PDF.concrete.util.file;

import org.vincentyeh.IMG2PDF.concrete.util.file.exception.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class FileUtils {
    public static File getExistedParentFile(File file) throws NoParentException, InvalidFileException, FileNotExistsException {
        checkNull(file);
        checkExists(file);
        return getParentFile(file);
    }

    public static File getParentFile(File file) throws NoParentException, InvalidFileException {
        checkFileValidity(file);

        if (isRoot(file)) {
            throw new TargetRootParentException(file);
        }

        File parent = file.getParentFile();

        if (isNull(parent)) {
            throw new NoParentException(file);
        }

        return parent;
    }

    public static void checkExists(File file) throws FileNotExistsException, InvalidFileException {
        checkNull(file);
        checkFileValidity(file);
        if (Files.notExists(file.toPath()))
            throw new FileNotExistsException(file);
    }

    public static void checkType(File file, WrongFileTypeException.Type excepted) throws WrongFileTypeException, InvalidFileException, FileNotExistsException {
        checkFileValidity(file);
        checkExists(file);
        checkNull(excepted);

        Path p = file.toPath();
        WrongFileTypeException.Type value = file.isFile() ? WrongFileTypeException.Type.FILE : WrongFileTypeException.Type.FOLDER;
        if (Files.isRegularFile(p))
            value = WrongFileTypeException.Type.FILE;
        else if (Files.isDirectory(p))
            value = WrongFileTypeException.Type.FOLDER;

        if (value != excepted) {
            throw new WrongFileTypeException(excepted, value,file);
        }

    }

    public static void checkOverwrite(File file, String reason) throws OverwriteException {
        checkNull(file);
        checkNull(reason);
        if (file.exists())
            throw new OverwriteException(reason, file);
    }

    public static File makeDirectories(File directory) throws MakeDirectoryException {
        checkNull(directory);
        try {
            return Files.createDirectories(directory.toPath()).toFile();
        } catch (IOException e) {
            throw new MakeDirectoryException(e, directory);
        }
    }

    public static boolean isRoot(File file) {
        checkNull(file);
        return file.toPath().getNameCount() == 0;
    }

    public static void checkFileValidity(File file) throws InvalidFileException {
        checkNull(file);
        try {
            file.toPath().toFile();
        } catch (InvalidPathException e) {
            throw new InvalidFileException(e, file);
        }
    }

    private static boolean isNull(Object o) {
        return o == null;
    }

    private static void checkNull(Object obj) {
        if (isNull(obj))
            throw new IllegalArgumentException();
    }
}
