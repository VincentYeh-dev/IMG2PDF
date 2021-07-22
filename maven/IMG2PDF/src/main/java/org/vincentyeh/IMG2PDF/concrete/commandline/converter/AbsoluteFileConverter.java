package org.vincentyeh.IMG2PDF.concrete.commandline.converter;

import org.vincentyeh.IMG2PDF.concrete.commandline.converter.core.BasicConverter;

import java.io.File;

public class AbsoluteFileConverter extends BasicConverter<File> {
    @Override
    public File convert(String s) throws Exception {
        checkNull(s,getClass().getName()+".s");
        checkEmpty(s,getClass().getName()+".s");
        return new File(s).getAbsoluteFile();
    }


}
