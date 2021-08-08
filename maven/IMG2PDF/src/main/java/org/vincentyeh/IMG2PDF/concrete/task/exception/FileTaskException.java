package org.vincentyeh.IMG2PDF.concrete.task.exception;

import java.io.File;

public class FileTaskException extends FileTaskFactoryException {

    private final File file;

    public FileTaskException(File file, Throwable e) {
        super(e);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
