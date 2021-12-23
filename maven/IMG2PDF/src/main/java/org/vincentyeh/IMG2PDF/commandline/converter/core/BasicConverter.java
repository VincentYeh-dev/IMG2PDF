package org.vincentyeh.IMG2PDF.commandline.converter.core;

import picocli.CommandLine;

public abstract class BasicConverter<T> implements CommandLine.ITypeConverter<T> {
    protected void checkNull(String string,String name){
        if(string==null)
            throw new IllegalArgumentException(name+" is null.");
    }
    protected void checkEmpty(String string,String name){
        if(string!=null&&string.isEmpty())
            throw new IllegalArgumentException(name+" is empty.");
    }

}
