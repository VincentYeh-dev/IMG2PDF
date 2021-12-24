package org.vincentyeh.IMG2PDF.handler.concrete;

import org.vincentyeh.IMG2PDF.handler.concrete.core.FileExceptionHandler;
import org.vincentyeh.IMG2PDF.handler.framework.CantHandleException;
import org.vincentyeh.IMG2PDF.handler.framework.ExceptionHandler;
import org.vincentyeh.IMG2PDF.handler.framework.Handler;
import org.vincentyeh.IMG2PDF.task.concrete.factory.exception.EmptyImagesException;
import org.vincentyeh.IMG2PDF.task.framework.factory.exception.TaskListFactoryException;
import org.vincentyeh.IMG2PDF.task.concrete.factory.exception.LineTaskBuilderException;
import org.vincentyeh.IMG2PDF.task.framework.factory.exception.TaskBuilderException;
import org.vincentyeh.IMG2PDF.util.file.FileNameFormatter;
import org.vincentyeh.IMG2PDF.util.file.exception.FileException;

import java.util.ResourceBundle;

import static java.lang.String.format;

public class LineFileTaskFactoryExceptionHandler extends ExceptionHandler {

    public LineFileTaskFactoryExceptionHandler(Handler<String, Exception> next, ResourceBundle resourceBundle) {
        super(next, "dirlist_task_factory", resourceBundle);
    }

    @Override
    public String handle(Exception data) throws CantHandleException {
        if (data instanceof TaskListFactoryException) {
            return "In dirlist : " + ((ExceptionHandler) new FileExceptionHandler(null, getResourceBundle())).handle((Exception) data.getCause());
        }

        if (data instanceof TaskBuilderException) {
            if (data instanceof LineTaskBuilderException) {
                LineTaskBuilderException ex1 = (LineTaskBuilderException) data;
                if (data.getCause() instanceof FileException) {
                    return targetLine(ex1.getLine(), ((ExceptionHandler) new FileExceptionHandler(null, getResourceBundle())).handle((Exception) data.getCause()));
                } else if (ex1.getCause() instanceof EmptyImagesException) {
                    return targetLine(ex1.getLine(), format(getLocaleString("source.empty_image"), ex1.getSource()));
                } else if (ex1.getCause() instanceof FileNameFormatter.NotMappedPattern) {
                    FileNameFormatter.NotMappedPattern ex2 = (FileNameFormatter.NotMappedPattern) ex1.getCause();
                    return targetLine(ex1.getLine(), format(getLocaleString("source.no_map_pattern"), ex1.getSource(), ex2.getPattern()));
                }
            }

        }

        return doNext(data);
    }

    private String targetLine(int line, String msg) {
        return format(getPublicString("at_line") + " : %s", line, msg);
    }
}
