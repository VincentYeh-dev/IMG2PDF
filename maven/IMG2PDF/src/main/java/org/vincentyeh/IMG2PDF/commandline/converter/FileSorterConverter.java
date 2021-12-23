package org.vincentyeh.IMG2PDF.commandline.converter;

import org.vincentyeh.IMG2PDF.commandline.converter.core.BasicConverter;
import org.vincentyeh.IMG2PDF.util.file.FileSorter;
import picocli.CommandLine;

public class FileSorterConverter extends BasicConverter<FileSorter> {

    @Override
    public FileSorter convert(String s) throws Exception {

        checkNull(s,getClass().getName()+".s");
        checkEmpty(s,getClass().getName()+".s");
        String[] split =s.split("-");
        try {
            return new FileSorter(FileSorter.Sortby.valueOf(split[0]),FileSorter.Sequence.valueOf(split[1]));
        }catch (Exception e){
            throw new CommandLine.TypeConversionException(e.getMessage());
        }
    }
}
