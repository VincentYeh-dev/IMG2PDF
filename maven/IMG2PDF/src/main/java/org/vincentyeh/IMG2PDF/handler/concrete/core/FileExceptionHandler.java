package org.vincentyeh.IMG2PDF.handler.concrete.core;

import org.vincentyeh.IMG2PDF.handler.framework.CantHandleException;
import org.vincentyeh.IMG2PDF.handler.framework.ExceptionHandler;
import org.vincentyeh.IMG2PDF.handler.framework.Handler;
import org.vincentyeh.IMG2PDF.util.file.exception.*;

import java.io.File;
import java.util.ResourceBundle;

import static java.lang.String.format;

public class FileExceptionHandler extends ExceptionHandler {
    public FileExceptionHandler(Handler<String, Exception> next, ResourceBundle resourceBundle) {
        super(next, "io.file", resourceBundle);
    }

    @Override
    public String handle(Exception data) throws CantHandleException {
        if (data instanceof FileException) {
            if (data instanceof FileNotExistsException) {
                String a = getLocaleString("FileNotExistsException");
                return format(a, getExceptionFile(data));
            } else if (data instanceof InvalidFileException) {
                return format(getLocaleString("InvalidFileException"), getExceptionFile(data));
            } else if (data instanceof WrongFileTypeException) {
                WrongFileTypeException ex1 = (WrongFileTypeException) data;
                return format(getLocaleString("WrongFileTypeException"), getFileTypeString(ex1.getExpected()), getFileTypeString(ex1.getValue()), getExceptionFile(ex1));
            } else if (data instanceof NoParentException) {
                return format(getLocaleString("NoParentException"), getExceptionFile(data));
            } else if (data instanceof OverwriteException) {
                return format(getLocaleString("OverwriteException"), getExceptionFile(data));
            }
        }
        return doNext(data);
    }

    private File getExceptionFile(Exception e) {
        return ((FileException) e).getFile();
    }
}
