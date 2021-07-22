package org.vincentyeh.IMG2PDF.framework.parameter;


public interface PageArgument {
    PageAlign getAlign();
    PageSize getSize();
    PageDirection getDirection();
    boolean autoRotate();

}
