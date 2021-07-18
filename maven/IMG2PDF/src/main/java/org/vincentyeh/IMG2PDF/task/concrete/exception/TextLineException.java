package org.vincentyeh.IMG2PDF.task.concrete.exception;

import java.io.File;

public class TextLineException extends TextException {

    private final int line;

    public TextLineException(Throwable cause, File source, int line) {
        super(cause, source);
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
