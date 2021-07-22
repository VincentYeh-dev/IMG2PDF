package org.vincentyeh.IMG2PDF.concrete.task.exception;

public abstract class TextTaskFactoryException extends Exception{
    public TextTaskFactoryException(String message) {
        super(message);
    }

    public TextTaskFactoryException(Throwable cause) {
        super(cause);
    }

}
