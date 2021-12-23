package org.vincentyeh.IMG2PDF.pdf.framework.calculation;

public class Size {
    private final float height;
    private final float width;

    public Size(float width, float height){
        if(height<0)
            throw new IllegalArgumentException("height<0");
        if(width<0)
            throw new IllegalArgumentException("width<0");

        this.height=height;
        this.width=width;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Size size = (Size) o;
        return Float.compare(size.height, height) == 0 && Float.compare(size.width, width) == 0;
    }

    @Override
    public String toString() {
        return String.format("(%.2f,%.2f)", width, height);
    }
}
