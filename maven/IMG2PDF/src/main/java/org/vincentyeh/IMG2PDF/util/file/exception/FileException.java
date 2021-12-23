package org.vincentyeh.IMG2PDF.util.file.exception;

import java.io.File;
import java.io.IOException;

public abstract class FileException extends IOException {
    protected File file;

    public FileException(File file) {
        this.file = file;
    }

    public FileException(String message, File file) {
        super(message);
        this.file = file;
    }

    public FileException(String message, Throwable cause, File file) {
        super(message, cause);
        this.file = file;
    }

    public FileException(Throwable cause, File file) {
        super(cause);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
