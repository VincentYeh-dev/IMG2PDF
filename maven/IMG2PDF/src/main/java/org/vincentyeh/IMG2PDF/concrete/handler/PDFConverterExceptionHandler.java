package org.vincentyeh.IMG2PDF.concrete.handler;


import org.vincentyeh.IMG2PDF.framework.handler.ExceptionHandler;
import org.vincentyeh.IMG2PDF.concrete.handler.core.FileExceptionHandler;
import org.vincentyeh.IMG2PDF.framework.handler.CantHandleException;
import org.vincentyeh.IMG2PDF.framework.handler.Handler;
import org.vincentyeh.IMG2PDF.concrete.pdf.exception.PDFConversionException;
import org.vincentyeh.IMG2PDF.concrete.pdf.exception.PDFConverterException;
import org.vincentyeh.IMG2PDF.concrete.pdf.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.concrete.pdf.exception.SaveException;
import org.vincentyeh.IMG2PDF.framework.task.Task;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.FileException;

import java.util.ResourceBundle;

import static java.lang.String.format;

class PDFConverterExceptionHandler extends ExceptionHandler {

    public PDFConverterExceptionHandler(Handler<String, Exception> next, ResourceBundle resourceBundle) {
        super(next, "conversion",resourceBundle);
    }

    @Override
    public String handle(Exception data) throws CantHandleException {
        if (data instanceof PDFConverterException) {
            PDFConverterException ex1 = (PDFConverterException) data;
            Throwable cause = ex1.getCause();
            Task task = ex1.getTask();
            if (cause instanceof ReadImageException) {
                return handleReadImageException((ReadImageException) cause, task);
            } else if (cause instanceof PDFConversionException) {
                return format(getLocaleString("conversion"), task.getPdfDestination().getPath(), cause.getCause().getMessage());
            } else if (cause instanceof SaveException) {
                Exception innerCause = (Exception) cause.getCause();
                if (innerCause instanceof FileException)
                    return handleFileException((FileException) innerCause);
            }

        }
        return doNext(data);
    }

    private String handleFileException(FileException e) throws CantHandleException {
        return ((ExceptionHandler) new FileExceptionHandler(null,getResourceBundle())).handle(e);
    }

    private String handleReadImageException(ReadImageException e, Task task) throws CantHandleException {
        Exception innerCause = (Exception) e.getCause();

        if (innerCause instanceof FileException)
            return format(getLocaleString("read_image"), task.getPdfDestination().getName(), e.getErrorImageFile(), handleFileException((FileException) innerCause));
        else
            return format(getLocaleString("read_image"), task.getPdfDestination().getName(), e.getErrorImageFile(), innerCause.getMessage());
    }
}
