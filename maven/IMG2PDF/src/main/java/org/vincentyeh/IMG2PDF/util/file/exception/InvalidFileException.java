package org.vincentyeh.IMG2PDF.util.file.exception;

import java.io.File;
import java.nio.file.InvalidPathException;

public class InvalidFileException extends FileException{
    public InvalidFileException(File file) {
        super("Invalid file:"+file,file);
    }

    public InvalidFileException(InvalidPathException cause, File file) {
        super(cause.getMessage(), file);
    }
}
