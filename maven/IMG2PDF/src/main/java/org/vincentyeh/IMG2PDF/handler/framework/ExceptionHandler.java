package org.vincentyeh.IMG2PDF.handler.framework;


import java.util.ResourceBundle;

public abstract class ExceptionHandler extends ResourceBundleHandler<Exception> {
    private final String exception_name;

    public ExceptionHandler(Handler<String, Exception> next, String exception_name, ResourceBundle resourceBundle) {
        super(next, "exception",resourceBundle);
        this.exception_name = exception_name;
    }

    @Override
    protected String getLocaleString(String key) {
        return super.getLocaleString(exception_name+"."+key);
    }
}
