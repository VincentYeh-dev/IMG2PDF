package org.vincentyeh.IMG2PDF.task.framework;


import org.vincentyeh.IMG2PDF.pdf.parameter.PageAlign;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageDirection;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageSize;

public interface PageArgument {
    PageAlign getAlign();
    PageSize getSize();
    PageDirection getDirection();
    boolean autoRotate();

}
