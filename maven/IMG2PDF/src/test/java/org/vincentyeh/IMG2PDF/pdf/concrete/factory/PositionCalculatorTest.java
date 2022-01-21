package org.vincentyeh.IMG2PDF.pdf.concrete.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.Position;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.PositionCalculator;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.Size;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageAlign;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PositionCalculatorTest {

    @Test
    public void testNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new PositionCalculator(null));

        assertThrows(IllegalArgumentException.class,
                () -> new PositionCalculator(PageAlign.valueOf("CENTER-CENTER")).calculate(null,null)
        );

    }

    @Test
    public void testZero() {
        PositionCalculator calculator = new PositionCalculator(PageAlign.valueOf("CENTER-CENTER"));
        Position pos1 = calculator.calculate(new Size(0f, 0f), new Size(0f, 0f));
        Position excepted = new Position(0f, 0f);
        assertEquals(pos1, excepted);
    }

    @Test
    public void testNegative() {
        PositionCalculator calculator=new PositionCalculator(PageAlign.valueOf("CENTER-CENTER"));
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(-1, -1, -1, -1)
        );
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(new Size(-1, -1), new Size(-1, -1))
        );
    }
    @Test
    public void testNegativeSpace() {
        PositionCalculator calculator=new PositionCalculator(PageAlign.valueOf("CENTER-CENTER"));
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(300, 300, 200, 200)
        );

        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(300, 200, 200, 200)
        );
    }
    @ParameterizedTest
    @MethodSource("provider")
    public void testHori(Size object,Size container){
        assertEquals(new PositionCalculator(PageAlign.valueOf("CENTER-CENTER")).calculate(object,container), center_center(object, container));
        assertEquals(new PositionCalculator(PageAlign.valueOf("CENTER-LEFT")).calculate(object,container), center_left(object, container));
        assertEquals(new PositionCalculator(PageAlign.valueOf("CENTER-RIGHT")).calculate(object,container), center_right(object, container));
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void testVeri(Size object,Size container){
        assertEquals(new PositionCalculator(PageAlign.valueOf("TOP-CENTER")).calculate(object,container), top_center(object, container));
        assertEquals(new PositionCalculator(PageAlign.valueOf("BOTTOM-CENTER")).calculate(object,container), bottom_center(object, container));
    }

    private Position top_center(Size object, Size container) {
        return new Position((container.getWidth()-object.getWidth())/2,(container.getHeight()-object.getHeight()));
    }
    private Position bottom_center(Size object, Size container){
        return new Position((container.getWidth()-object.getWidth())/2,0);
    }
    private Position center_center(Size object, Size container){
        return new Position((container.getWidth()-object.getWidth())/2,(container.getHeight()-object.getHeight())/2);
    }
    private Position center_right(Size object, Size container) {
        return new Position((container.getWidth()-object.getWidth()),(container.getHeight()-object.getHeight())/2);
    }
    private Position center_left(Size object, Size container){
        return new Position(0,(container.getHeight()-object.getHeight())/2);
    }

    private static  Stream<Arguments> provider() {
        List<Arguments> list=new ArrayList<>();
        for(int i=0;i<100;i++){
            Size container=new Size(new Random().nextInt(4096)+1, new Random().nextInt(4096)+1);
            Size object=new Size(new Random().nextInt((int)container.getWidth()), new Random().nextInt((int)container.getHeight()));
            list.add(arguments(object,container));
        }
        return list.stream();
    }

}
