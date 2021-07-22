package org.vincentyeh.IMG2PDF.concrete.handler;

import org.vincentyeh.IMG2PDF.framework.handler.ExceptionHandler;

import java.util.ResourceBundle;

public class ExceptionHandlerFactory {
    private static ResourceBundle resourceBundle;

    public static ParameterExceptionHandler getParameterExceptionHandler(ExceptionHandler next){
        return new ParameterExceptionHandler(next,resourceBundle);
    }

    public static PDFConverterExceptionHandler getPDFConverterExceptionHandler(ExceptionHandler  next){
        return new PDFConverterExceptionHandler(next,resourceBundle);
    }

    public static ExceptionHandler getTextFileTaskFactoryExceptionHandler(ExceptionHandler next){
        return new TextFileTaskFactoryExceptionHandler(next,resourceBundle);
    }

    public static void setResourceBundle(ResourceBundle resourceBundle) {
        ExceptionHandlerFactory.resourceBundle = resourceBundle;
    }
}
