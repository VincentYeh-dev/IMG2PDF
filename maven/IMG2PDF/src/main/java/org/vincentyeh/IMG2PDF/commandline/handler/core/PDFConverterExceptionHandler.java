package org.vincentyeh.IMG2PDF.commandline.handler.core;


import org.vincentyeh.IMG2PDF.commandline.handler.FileExceptionHandler;
import org.vincentyeh.IMG2PDF.pattern.Handler;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.PDFConversionException;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.PDFConverterException;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.SaveException;
import org.vincentyeh.IMG2PDF.task.framework.Task;
import org.vincentyeh.IMG2PDF.util.file.exception.FileException;

import static java.lang.String.format;

public class PDFConverterExceptionHandler extends ExceptionHandler {

    public PDFConverterExceptionHandler(Handler<String, Exception> next) {
        super(next, "conversion");
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
        return ((ExceptionHandler) new FileExceptionHandler(null)).handle(e);
    }

    private String handleReadImageException(ReadImageException e, Task task) throws CantHandleException {
        Exception innerCause = (Exception) e.getCause();

        if (innerCause instanceof FileException)
            return format(getLocaleString("read_image"), task.getPdfDestination().getName(), e.getErrorImageFile(), handleFileException((FileException) innerCause));
        else
            return format(getLocaleString("read_image"), task.getPdfDestination().getName(), e.getErrorImageFile(), innerCause.getMessage());
    }
}
