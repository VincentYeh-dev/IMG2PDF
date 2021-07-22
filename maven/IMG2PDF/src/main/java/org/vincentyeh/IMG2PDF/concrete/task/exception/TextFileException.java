package org.vincentyeh.IMG2PDF.concrete.task.exception;

import java.io.File;

public class TextFileException extends TextTaskFactoryException {

    private final File file;

    public TextFileException(File file, Throwable e) {
        super(e);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
