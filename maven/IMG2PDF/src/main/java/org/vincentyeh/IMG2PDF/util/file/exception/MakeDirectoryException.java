package org.vincentyeh.IMG2PDF.util.file.exception;

import java.io.File;

public class MakeDirectoryException extends FileException{

    public MakeDirectoryException(Throwable cause, File file) {
        super("Fail to create directories:"+file,cause, file);
    }
}
