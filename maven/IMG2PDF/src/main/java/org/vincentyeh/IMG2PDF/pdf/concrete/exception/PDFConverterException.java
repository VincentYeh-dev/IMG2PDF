package org.vincentyeh.IMG2PDF.pdf.concrete.exception;

import org.vincentyeh.IMG2PDF.task.framework.Task;

public class PDFConverterException extends Exception{
    protected final Task task;
    public PDFConverterException(Task task) {
        this.task = task;
    }

    public PDFConverterException(String message, Task task) {
        super(message);
        this.task = task;
    }

    public PDFConverterException(Throwable cause, Task task) {
        super(cause);
        this.task = task;
    }

    public PDFConverterException(String message, Throwable cause, Task task) {
        super(message, cause);
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
