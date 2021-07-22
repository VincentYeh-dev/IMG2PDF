package org.vincentyeh.IMG2PDF.concrete.handler.core;


import org.vincentyeh.IMG2PDF.framework.handler.CantHandleException;
import org.vincentyeh.IMG2PDF.framework.handler.Handler;
import org.vincentyeh.IMG2PDF.framework.handler.ResourceBundleHandler;

import java.util.ResourceBundle;

import static java.lang.String.format;

public class ErrorHandler extends ResourceBundleHandler<Error> {
    public ErrorHandler(Handler<String, Error> next, ResourceBundle resourceBundle) {
        super(next, "error",resourceBundle);
    }

    @Override
    public String handle(Error data) throws CantHandleException {
         if(data instanceof OutOfMemoryError){
            return format(getLocaleString("OutOfMemoryError"),data.getMessage());
         }
        return null;
    }
}
