package org.vincentyeh.IMG2PDF.pdf.concrete.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.Size;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.SizeCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SizeCalculatorTest {
    @Test
    public void testNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new SizeCalculator().scaleUpToMax(null, null)
        );
    }

    @Test
    public void testNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new SizeCalculator().scaleUpToMax(new Size(-1, -1), new Size(-1, -1))
        );
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void test(Size img_size, Size page_size) {
        assertEquals(new SizeCalculator().scaleUpToMax(img_size, page_size),calculate(img_size,page_size));
    }

    private Size calculate(Size img_size, Size page_size) {
        float img_height = img_size.getHeight();
        float img_width = img_size.getWidth();
        float page_height = page_size.getHeight();
        float page_width = page_size.getWidth();

        float out_width = (img_width / img_height) * page_height;
        float out_height = (img_height / img_width) * page_width;
        if (out_height <= page_height) {
//			width fill
            out_width = page_width;
//			out_height = (img_height / img_width) * page_width;
        } else if (out_width <= page_width) {
//			height fill
            out_height = page_height;
//			out_width = (img_width / img_height) * page_height;
        }

        return new Size(out_width, out_height);
    }

    private static Stream<Arguments> provider() {
        List<Arguments> list=new ArrayList<>();
        for(int i=0;i<100;i++){
            Size container=new Size(new Random().nextInt(4096)+1, new Random().nextInt(4096)+1);
            Size object=new Size(new Random().nextInt((int)container.getWidth()), new Random().nextInt((int)container.getHeight()));
            list.add(arguments(object,container));
        }
        return list.stream();
    }
}
