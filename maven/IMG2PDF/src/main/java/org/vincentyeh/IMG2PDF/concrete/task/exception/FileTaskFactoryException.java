package org.vincentyeh.IMG2PDF.concrete.task.exception;

public abstract class FileTaskFactoryException extends Exception{
    public FileTaskFactoryException(String message) {
        super(message);
    }

    public FileTaskFactoryException(Throwable cause) {
        super(cause);
    }

}
