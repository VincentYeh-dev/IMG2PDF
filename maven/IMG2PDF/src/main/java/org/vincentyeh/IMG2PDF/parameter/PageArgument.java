package org.vincentyeh.IMG2PDF.parameter;


public class PageArgument {
    private PageAlign align;
    private PageSize size;
    private PageDirection direction;
    private boolean autoRotate;


    public final PageAlign getAlign() {
        return align;
    }

    public final PageSize getSize() {
        return size;
    }

    public final PageDirection getDirection() {
        return direction;
    }

    public final boolean autoRotate() {
        return autoRotate;
    }

    public final void setAlign(PageAlign align) {
        this.align = align;
    }

    public final void setSize(PageSize size) {
        this.size = size;
    }

    public final void setDirection(PageDirection direction) {
        this.direction = direction;
    }

    public final void setAutoRotate(boolean autoRotate) {
        this.autoRotate = autoRotate;
    }
}
