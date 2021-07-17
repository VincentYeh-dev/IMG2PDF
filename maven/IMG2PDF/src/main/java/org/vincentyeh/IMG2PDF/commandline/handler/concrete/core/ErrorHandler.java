package org.vincentyeh.IMG2PDF.commandline.handler.concrete.core;


import org.vincentyeh.IMG2PDF.commandline.handler.framework.CantHandleException;
import org.vincentyeh.IMG2PDF.commandline.handler.framework.Handler;
import org.vincentyeh.IMG2PDF.commandline.handler.framework.ResourceBundleHandler;

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
