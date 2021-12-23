package org.vincentyeh.IMG2PDF.parameter;


public interface PageArgument {
    PageAlign getAlign();
    PageSize getSize();
    PageDirection getDirection();
    boolean autoRotate();

}
