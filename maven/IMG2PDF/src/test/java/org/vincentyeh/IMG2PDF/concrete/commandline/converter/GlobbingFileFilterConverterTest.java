package org.vincentyeh.IMG2PDF.concrete.commandline.converter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.vincentyeh.IMG2PDF.commandline.converter.GlobbingFileFilterConverter;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GlobbingFileFilterConverterTest {
    @ParameterizedTest
    @NullAndEmptySource
    public void testNullAndEmpty(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> new GlobbingFileFilterConverter().convert(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\\"})
    public void testIllegal(String input){
        assertThrows(CommandLine.TypeConversionException.class,
                ()-> new GlobbingFileFilterConverter().convert(input));
    }
}
