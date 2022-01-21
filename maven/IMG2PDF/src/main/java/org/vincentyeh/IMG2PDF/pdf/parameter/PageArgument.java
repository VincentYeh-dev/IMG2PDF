package org.vincentyeh.IMG2PDF.pdf.parameter;


public final class PageArgument {
    private PageAlign align;
    private PageSize size;
    private PageDirection direction;
    private boolean autoRotate;


    public PageAlign getAlign() {
        return align;
    }

    public PageSize getSize() {
        return size;
    }

    public PageDirection getDirection() {
        return direction;
    }

    public boolean autoRotate() {
        return autoRotate;
    }

    public void setAlign(PageAlign align) {
        this.align = align;
    }

    public void setSize(PageSize size) {
        this.size = size;
    }

    public void setDirection(PageDirection direction) {
        this.direction = direction;
    }

    public void setAutoRotate(boolean autoRotate) {
        this.autoRotate = autoRotate;
    }
}
