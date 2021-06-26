package org.vincentyeh.IMG2PDF.commandline.converter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PageAlignConverterTest {
    @ParameterizedTest
    @NullAndEmptySource
    public void testNullAndEmpty(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> new PageAlignConverter().convert(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaa","bbbb","cccc","$$$$"})
    public void testIllegal(String input){
        assertThrows(CommandLine.TypeConversionException.class,
                ()-> new PageAlignConverter().convert(input));
    }

}
