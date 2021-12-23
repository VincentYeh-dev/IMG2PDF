package org.vincentyeh.IMG2PDF.concrete.commandline.converter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.vincentyeh.IMG2PDF.commandline.converter.PermissionConverter;
import picocli.CommandLine;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PermissionConverterTest {
    @ParameterizedTest
    @NullAndEmptySource
    public void testNullAndEmptyConversion(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> new PermissionConverter().convert(input));
    }

    @ParameterizedTest
    @MethodSource("range")
    public void testInRangeValue(String input) {
        assertDoesNotThrow(() -> new PermissionConverter().convert(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"256","512","1024","-1","-256","-512","-1024"})
    public void testOutOfRangeValue(String input) {
        assertThrows(CommandLine.TypeConversionException.class,
                () -> new PermissionConverter().convert(input));
    }

    public static Stream<String> range() {
        List<String> a = IntStream.range(0, 256).skip(1).mapToObj(i -> (i + "")).collect(Collectors.toList());
        return a.stream();
    }
}
