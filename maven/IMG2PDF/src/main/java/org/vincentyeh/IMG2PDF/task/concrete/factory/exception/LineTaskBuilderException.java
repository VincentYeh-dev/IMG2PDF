package org.vincentyeh.IMG2PDF.task.concrete.factory.exception;

import org.vincentyeh.IMG2PDF.task.framework.factory.exception.TaskBuilderException;

import java.io.File;

public class LineTaskBuilderException extends TaskBuilderException {

    protected final File source;
    private final int line;

    public LineTaskBuilderException(Throwable cause, File source, int line) {
        super(cause);
        this.line = line;
        this.source = source;
    }

    public int getLine() {
        return line;
    }

    public File getSource() {
        return source;
    }
}
