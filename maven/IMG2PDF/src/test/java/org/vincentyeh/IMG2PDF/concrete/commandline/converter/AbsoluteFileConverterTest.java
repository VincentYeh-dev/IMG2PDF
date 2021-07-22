package org.vincentyeh.IMG2PDF.concrete.commandline.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbsoluteFileConverterTest {
    @ParameterizedTest
    @NullAndEmptySource
    public void testNullAndEmpty(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> new AbsoluteFileConverter().convert(input));
    }

    @Test
    public void testAbsolute() throws Exception {
        assertTrue(new AbsoluteFileConverter().convert("aaa").isAbsolute());
    }


}
