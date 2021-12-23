package org.vincentyeh.IMG2PDF.pdf.framework.calculation;

import org.vincentyeh.IMG2PDF.parameter.PageAlign;

public class PositionCalculator {
    private final PageAlign align;

    public PositionCalculator(PageAlign align) {
        if(align==null)
            throw new IllegalArgumentException("align==null");
        this.align = align;
    }

    public Position calculate(Size object_size, Size container_size){
        if (object_size == null)
            throw new IllegalArgumentException("object_size==null");
        if (container_size == null)
            throw new IllegalArgumentException("container_size==null");

        return calculate(object_size.getHeight(), object_size.getWidth(), container_size.getHeight(),container_size.getWidth());
    }

    public Position calculate(float object_height, float object_width, float container_height,
                              float container_width){
        final float x_space;
        final float y_space;
        if (object_height < 0)
            throw new IllegalArgumentException("object_height<0");
        if (object_width < 0)
            throw new IllegalArgumentException("object_width<0");

        if (container_height < 0)
            throw new IllegalArgumentException("container_height<0");
        if (container_width < 0)
            throw new IllegalArgumentException("container_width<0");

        x_space = container_width - object_width;
        if (x_space < 0)
            throw new IllegalArgumentException("The space of x axis is negative.");
        y_space = container_height - object_height;
        if (y_space < 0)
            throw new IllegalArgumentException("The space of y axis is negative.");

        float position_x = 0f, position_y = 0f;
//        水平 X
        PageAlign.HorizontalAlign hori_align = align.getHorizontal();

//        垂直 Y
        PageAlign.VerticalAlign verti_align = align.getVertical();

        switch (hori_align) {
            case LEFT:
                position_x = 0;
                break;
            case RIGHT:
                position_x = x_space;
                break;
            case CENTER:
                position_x = x_space / 2;
                break;
            default:
                break;
        }

        switch (verti_align) {
            case BOTTOM:
                position_y = 0;
                break;
            case TOP:
                position_y = y_space;
                break;
            case CENTER:
                position_y = y_space / 2;
                break;
            default:
                break;

        }

        return new Position(position_x, position_y);
    }

}
