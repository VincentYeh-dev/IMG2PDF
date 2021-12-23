package org.vincentyeh.IMG2PDF.commandline.converter;

import org.vincentyeh.IMG2PDF.commandline.converter.core.BasicConverter;
import org.vincentyeh.IMG2PDF.util.BytesSize;
import picocli.CommandLine;

public class ByteSizeConverter extends BasicConverter<BytesSize> {
    @Override
    public BytesSize convert(String s) throws Exception {

        checkNull(s,getClass().getName()+".s");
        checkEmpty(s,getClass().getName()+".s");

        try {
            return BytesSize.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new CommandLine.TypeConversionException(e.getMessage());
        }
    }
}
