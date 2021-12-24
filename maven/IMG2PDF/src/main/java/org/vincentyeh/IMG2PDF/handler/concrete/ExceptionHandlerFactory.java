package org.vincentyeh.IMG2PDF.handler.concrete;

import org.vincentyeh.IMG2PDF.handler.framework.ExceptionHandler;

import java.util.ResourceBundle;

public class ExceptionHandlerFactory {
    private static ResourceBundle resourceBundle;

    public static ParameterExceptionHandler getParameterExceptionHandler(ExceptionHandler next){
        return new ParameterExceptionHandler(next,resourceBundle);
    }

    public static PDFConversionExceptionHandler getPDFConversionExceptionHandler(ExceptionHandler  next){
        return new PDFConversionExceptionHandler(next,resourceBundle);
    }

    public static ExceptionHandler getTextFileTaskFactoryExceptionHandler(ExceptionHandler next){
        return new TextFileTaskFactoryExceptionHandler(next,resourceBundle);
    }

    public static void setResourceBundle(ResourceBundle resourceBundle) {
        ExceptionHandlerFactory.resourceBundle = resourceBundle;
    }
}
