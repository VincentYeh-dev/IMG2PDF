package org.vincentyeh.IMG2PDF.task.concrete.exception;


import java.io.File;

class TextException extends TextTaskFactoryException {

    protected final File source;
    public TextException(Throwable cause, File source) {
        super(cause);
        this.source = source;
    }

    public final File getSource() {
        return source;
    }

}
