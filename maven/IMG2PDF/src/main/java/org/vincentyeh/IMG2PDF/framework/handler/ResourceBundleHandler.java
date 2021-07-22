package org.vincentyeh.IMG2PDF.framework.handler;


import org.vincentyeh.IMG2PDF.concrete.util.file.exception.WrongFileTypeException;

import java.util.ResourceBundle;

public abstract class ResourceBundleHandler<DATA> extends Handler<String,DATA> {

    private final ResourceBundle resourceBundle;
    private final String tag;

    public ResourceBundleHandler(Handler<String, DATA> next, String tag, ResourceBundle resourceBundle) {
        super(next);
        this.resourceBundle = resourceBundle;
        this.tag = tag;
    }


    protected String getPublicString(String key){
        return resourceBundle.getString("public."+key);
    }

    protected String getFileTypeString(WrongFileTypeException.Type type) {
        return getPublicString("file.type." + type.toString().toLowerCase());
    }

    protected String getLocaleString(String key){
        return resourceBundle.getString("handler."+tag+"."+key);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
