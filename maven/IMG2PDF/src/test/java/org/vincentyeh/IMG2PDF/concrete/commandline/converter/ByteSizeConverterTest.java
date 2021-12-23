package org.vincentyeh.IMG2PDF.concrete.commandline.converter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.vincentyeh.IMG2PDF.commandline.converter.ByteSizeConverter;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ByteSizeConverterTest {
    @ParameterizedTest
    @NullAndEmptySource
    public void testNullAndEmptyConversion(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> new ByteSizeConverter().convert(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc","aaa","bbb","MBA"})
    public void testIllegal(String input){
        assertThrows(CommandLine.TypeConversionException.class,
                () -> new ByteSizeConverter().convert(input));
    }
}
