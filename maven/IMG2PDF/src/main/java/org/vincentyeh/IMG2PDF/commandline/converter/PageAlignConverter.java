package org.vincentyeh.IMG2PDF.commandline.converter;

import org.vincentyeh.IMG2PDF.commandline.converter.core.BasicConverter;
import org.vincentyeh.IMG2PDF.parameter.PageAlign;
import picocli.CommandLine;

public class PageAlignConverter extends BasicConverter<PageAlign> {
    @Override
    public PageAlign convert(String s) throws Exception {
        checkNull(s, getClass().getName() + ".s");
        checkEmpty(s, getClass().getName() + ".s");
        try {
            return PageAlign.valueOf(s);
        } catch (Exception e) {
            throw new CommandLine.TypeConversionException(e.getMessage());
        }
    }

}
