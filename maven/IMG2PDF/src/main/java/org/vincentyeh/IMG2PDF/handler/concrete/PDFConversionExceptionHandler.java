package org.vincentyeh.IMG2PDF.handler.concrete;


import org.vincentyeh.IMG2PDF.handler.framework.ExceptionHandler;
import org.vincentyeh.IMG2PDF.handler.concrete.core.FileExceptionHandler;
import org.vincentyeh.IMG2PDF.handler.framework.CantHandleException;
import org.vincentyeh.IMG2PDF.handler.framework.Handler;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.exception.PDFConversionException;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.exception.SaveException;
import org.vincentyeh.IMG2PDF.task.framework.Task;
import org.vincentyeh.IMG2PDF.util.file.exception.FileException;

import java.io.IOException;
import java.util.ResourceBundle;

import static java.lang.String.format;

public class PDFConversionExceptionHandler extends ExceptionHandler {

    public PDFConversionExceptionHandler(Handler<String, Exception> next, ResourceBundle resourceBundle) {
        super(next, "conversion", resourceBundle);
    }

    @Override
    public String handle(Exception data) throws CantHandleException {
        if (data instanceof PDFConversionException) {
            PDFConversionException ex1 = (PDFConversionException) data;
            Task task = ex1.getTask();
            Throwable cause = ex1.getCause();
            if (cause instanceof ReadImageException) {
                return handleReadImageException((ReadImageException) cause, task);
            } else if (cause instanceof SaveException) {
                Exception innerCause = (Exception) cause.getCause();
                if (innerCause instanceof FileException)
                    return handleFileException((FileException) innerCause);
                else if(innerCause instanceof IOException)
                    return "Unable to save file:"+innerCause.getMessage();
            }
        }
        return doNext(data);
    }

    private String handleFileException(FileException e) throws CantHandleException {
        return ((ExceptionHandler) new FileExceptionHandler(null, getResourceBundle())).handle(e);
    }

    private String handleReadImageException(ReadImageException e, Task task) throws CantHandleException {
        Exception innerCause = (Exception) e.getCause();

        if (innerCause instanceof FileException)
            return format(getLocaleString("read_image"), task.getPdfDestination().getName(), e.getErrorImageFile(), handleFileException((FileException) innerCause));
        else
            return format(getLocaleString("read_image"), task.getPdfDestination().getName(), e.getErrorImageFile(), innerCause.getMessage());
    }
}
