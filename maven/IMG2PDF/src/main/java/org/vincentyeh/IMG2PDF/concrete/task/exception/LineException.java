package org.vincentyeh.IMG2PDF.concrete.task.exception;

import java.io.File;

public class LineException extends FileTaskFactoryException {

    protected final File source;
    private final int line;
    public LineException(Throwable cause, File source, int line) {
        super(cause);
        this.line = line;
        this.source=source;
    }

    public int getLine() {
        return line;
    }

    public File getSource() {
        return source;
    }
}
