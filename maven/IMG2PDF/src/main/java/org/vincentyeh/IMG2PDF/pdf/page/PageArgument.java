package org.vincentyeh.IMG2PDF.pdf.page;

import org.jdom2.Element;


public class PageArgument {


    public static class Builder {
        private PageAlign align;
        private PageSize size;
        private PageDirection direction;
        private boolean auto_rotate;

        public Builder setAlign(PageAlign align) {
            this.align = align;
            return this;
        }

        public Builder setSize(PageSize size) {
            this.size = size;
            return this;
        }

        public Builder setDirection(PageDirection direction) {
            this.direction = direction;
            return this;
        }

        public Builder setAutoRotate(boolean auto_rotate) {
            this.auto_rotate = auto_rotate;
            return this;
        }

        public PageArgument build() {
            return new PageArgument(align, size, direction, auto_rotate);
        }

        public PageArgument buildFrom(Element element) {
            setAlign(new PageAlign(element.getChild("align").getValue()));
            setSize(PageSize.valueOf(element.getChild("size").getValue()));
            setDirection(PageDirection.valueOf(element.getChild("default-direction").getValue()));
            setAutoRotate(Boolean.parseBoolean(element.getChild("auto-rotate").getValue()));
            return build();
        }
    }

    private final PageAlign align;
    private final PageSize size;
    private final PageDirection direction;
    private final boolean auto_rotate;

    private PageArgument(PageAlign align, PageSize size, PageDirection direction, boolean auto_rotate) {
        this.align = align;
        this.size = size;
        this.direction = direction;
        this.auto_rotate = auto_rotate;
    }

    public PageAlign getAlign() {
        return align;
    }

    public PageDirection getDirection() {
        return direction;
    }

    public PageSize getSize() {
        return size;
    }

    public boolean getAutoRotate() {
        return auto_rotate;
    }

    public Element toElement() {
        Element element = new Element("PageArgument");
        element.addContent(new Element("size").addContent(size.toString()));
        element.addContent(new Element("align").addContent(align.toString()));
        element.addContent(new Element("default-direction").addContent(direction.toString()));
        element.addContent(new Element("auto-rotate").addContent(Boolean.toString(auto_rotate)));
        return element;

    }

    @Override
    public String toString() {
        return "PageArgument{" +
                "align=" + align +
                ", size=" + size +
                ", direction=" + direction +
                ", auto_rotate=" + auto_rotate +
                '}';
    }
}
