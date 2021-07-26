package org.vincentyeh.IMG2PDF.framework.pdf.calculation;


/**
 * https://www.vinaysingh.info/negative-zero/
 */
public class Position {
    private final float x;
    private final float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    @Override
    public String toString() {
        return String.format("(%.2f,%.2f)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Float.compare(position.x, x) == 0 && Float.compare(position.y, y) == 0;
    }
}
