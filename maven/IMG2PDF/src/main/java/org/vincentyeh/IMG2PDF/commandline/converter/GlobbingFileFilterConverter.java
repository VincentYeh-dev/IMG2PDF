package org.vincentyeh.IMG2PDF.commandline.converter;

import org.vincentyeh.IMG2PDF.commandline.converter.core.BasicConverter;
import org.vincentyeh.IMG2PDF.util.file.GlobbingFileFilter;
import picocli.CommandLine;

public class GlobbingFileFilterConverter extends BasicConverter<GlobbingFileFilter> {

    @Override
    public GlobbingFileFilter convert(String s) throws Exception {
        checkNull(s,getClass().getName()+".s");
        checkEmpty(s,getClass().getName()+".s");
        try {
            return new GlobbingFileFilter(s);
        }catch (Exception e){
            throw new CommandLine.TypeConversionException(e.getMessage());
        }
    }
}
