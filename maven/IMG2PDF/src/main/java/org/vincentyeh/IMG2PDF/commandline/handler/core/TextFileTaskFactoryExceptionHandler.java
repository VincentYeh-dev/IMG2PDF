package org.vincentyeh.IMG2PDF.commandline.handler.core;

import org.vincentyeh.IMG2PDF.commandline.handler.FileExceptionHandler;
import org.vincentyeh.IMG2PDF.pattern.Handler;
import org.vincentyeh.IMG2PDF.task.concrete.exception.EmptyImagesException;
import org.vincentyeh.IMG2PDF.task.concrete.exception.TextFileException;
import org.vincentyeh.IMG2PDF.task.concrete.exception.TextTaskFactoryException;
import org.vincentyeh.IMG2PDF.task.concrete.factory.TextFileTaskListFactory;
import org.vincentyeh.IMG2PDF.util.file.FileNameFormatter;
import org.vincentyeh.IMG2PDF.util.file.exception.FileException;

import static java.lang.String.format;

public class TextFileTaskFactoryExceptionHandler extends ExceptionHandler {

    public TextFileTaskFactoryExceptionHandler(Handler<String, Exception> next) {
        super(next, "dirlist_task_factory");
    }

    @Override
    public String handle(Exception data) throws CantHandleException {
        if (data instanceof TextTaskFactoryException) {
            if (data instanceof TextFileException) {
                return "In dirlist : " + ((ExceptionHandler) new FileExceptionHandler(null)).handle((Exception) data.getCause());
            }

            if (data instanceof TextFileTaskListFactory.TextLineException) {
                TextFileTaskListFactory.TextLineException ex1 = (TextFileTaskListFactory.TextLineException) data;
                if (data.getCause() instanceof FileException) {
                    return targetLine(ex1.getLine(), ((ExceptionHandler) new FileExceptionHandler(null)).handle((Exception) data.getCause()));
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
