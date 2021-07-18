package org.vincentyeh.IMG2PDF.commandline.handler.concrete;

import org.vincentyeh.IMG2PDF.commandline.handler.concrete.core.FileExceptionHandler;
import org.vincentyeh.IMG2PDF.commandline.handler.framework.CantHandleException;
import org.vincentyeh.IMG2PDF.commandline.handler.framework.ExceptionHandler;
import org.vincentyeh.IMG2PDF.commandline.handler.framework.Handler;
import org.vincentyeh.IMG2PDF.task.concrete.exception.EmptyImagesException;
import org.vincentyeh.IMG2PDF.task.concrete.exception.TextFileException;
import org.vincentyeh.IMG2PDF.task.concrete.exception.TextLineException;
import org.vincentyeh.IMG2PDF.task.concrete.exception.TextTaskFactoryException;
import org.vincentyeh.IMG2PDF.util.file.FileNameFormatter;
import org.vincentyeh.IMG2PDF.util.file.exception.FileException;

import java.util.ResourceBundle;

import static java.lang.String.format;

class TextFileTaskFactoryExceptionHandler extends ExceptionHandler {

    public TextFileTaskFactoryExceptionHandler(Handler<String, Exception> next, ResourceBundle resourceBundle) {
        super(next, "dirlist_task_factory",resourceBundle);
    }

    @Override
    public String handle(Exception data) throws CantHandleException {
        if (data instanceof TextTaskFactoryException) {
            if (data instanceof TextFileException) {
                return "In dirlist : " + ((ExceptionHandler) new FileExceptionHandler(null,getResourceBundle())).handle((Exception) data.getCause());
            }

            if (data instanceof TextLineException) {
                TextLineException ex1 = (TextLineException) data;
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
