package org.vincentyeh.IMG2PDF.pdf.parameter;


public interface PageArgument {
    PageAlign getAlign();
    PageSize getSize();
    PageDirection getDirection();
    boolean autoRotate();

}
