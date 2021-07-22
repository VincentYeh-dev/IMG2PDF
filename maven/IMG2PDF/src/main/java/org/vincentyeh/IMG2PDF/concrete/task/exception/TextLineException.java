package org.vincentyeh.IMG2PDF.concrete.task.exception;

import java.io.File;

public class TextLineException extends TextTaskFactoryException{

    protected final File source;
    private final int line;
    public TextLineException(Throwable cause, File source, int line) {
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
