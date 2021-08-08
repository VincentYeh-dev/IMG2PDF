package org.vincentyeh.IMG2PDF.concrete.handler;

import org.vincentyeh.IMG2PDF.concrete.handler.core.FileExceptionHandler;
import org.vincentyeh.IMG2PDF.framework.handler.CantHandleException;
import org.vincentyeh.IMG2PDF.framework.handler.ExceptionHandler;
import org.vincentyeh.IMG2PDF.framework.handler.Handler;
import org.vincentyeh.IMG2PDF.concrete.task.exception.EmptyImagesException;
import org.vincentyeh.IMG2PDF.concrete.task.exception.FileTaskException;
import org.vincentyeh.IMG2PDF.concrete.task.exception.LineException;
import org.vincentyeh.IMG2PDF.concrete.task.exception.FileTaskFactoryException;
import org.vincentyeh.IMG2PDF.concrete.util.file.FileNameFormatter;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.FileException;

import java.util.ResourceBundle;

import static java.lang.String.format;

class TextFileTaskFactoryExceptionHandler extends ExceptionHandler {

    public TextFileTaskFactoryExceptionHandler(Handler<String, Exception> next, ResourceBundle resourceBundle) {
        super(next, "dirlist_task_factory",resourceBundle);
    }

    @Override
    public String handle(Exception data) throws CantHandleException {
        if (data instanceof FileTaskFactoryException) {
            if (data instanceof FileTaskException) {
                return "In dirlist : " + ((ExceptionHandler) new FileExceptionHandler(null,getResourceBundle())).handle((Exception) data.getCause());
            }

            if (data instanceof LineException) {
                LineException ex1 = (LineException) data;
                if (data.getCause() instanceof FileException) {
                    return targetLine(ex1.getLine(), ((ExceptionHandler) new FileExceptionHandler(null,getResourceBundle())).handle((Exception) data.getCause()));
                } else if (ex1.getCause() instanceof EmptyImagesException) {
                    return targetLine(ex1.getLine(),format(getLocaleString("source.empty_image"), ex1.getSource()));
                } else if (ex1.getCause() instanceof FileNameFormatter.NotMappedPattern) {
                    FileNameFormatter.NotMappedPattern ex2 = (FileNameFormatter.NotMappedPattern) ex1.getCause();
                    return targetLine(ex1.getLine(),format(getLocaleString("source.no_map_pattern"), ex1.getSource(), ex2.getPattern()));
                }
            }


        }

        return doNext(data);
    }

    private String targetLine(int line, String msg) {
        return format(getPublicString("at_line") + " : %s",line,msg);
    }
}
