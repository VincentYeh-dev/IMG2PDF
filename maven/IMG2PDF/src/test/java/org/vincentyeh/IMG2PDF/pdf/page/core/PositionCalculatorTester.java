package org.vincentyeh.IMG2PDF.pdf.page.core;

import org.junit.Assert;
import org.junit.Test;
import org.vincentyeh.IMG2PDF.pdf.page.PageAlign;

public class PositionCalculatorTester {

    @Test
    public void test() {

        PositionCalculator calculator = PositionCalculator.getInstance();
        PositionCalculator.init(1024, 1024, 2048, 2048);

        Position pos = calculator.calculate(PageAlign.valueOf("TOP-CENTER"));
        EqualsPosition(new Position(2048-512-1024,1024),pos);


        pos = calculator.calculate(PageAlign.valueOf("TOP-LEFT"));
        EqualsPosition(new Position(0,1024),pos);

        pos = calculator.calculate(PageAlign.valueOf("TOP-RIGHT"));
        EqualsPosition(new Position(1024,1024),pos);


    }

    void EqualsPosition(Position excepted,Position a){
        String str_a=String.format("(%d,%d)",(int)a.getX(),(int)a.getY());
        String str_excepted=String.format("(%d,%d)",(int)excepted.getX(),(int)excepted.getY());
        Assert.assertEquals(str_excepted,str_a);
    }
}
