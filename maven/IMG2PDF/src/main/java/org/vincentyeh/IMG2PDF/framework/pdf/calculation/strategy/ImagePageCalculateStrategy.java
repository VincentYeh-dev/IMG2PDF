package org.vincentyeh.IMG2PDF.framework.pdf.calculation.strategy;

import org.vincentyeh.IMG2PDF.framework.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.framework.pdf.calculation.Position;
import org.vincentyeh.IMG2PDF.framework.pdf.calculation.Size;

import java.awt.image.BufferedImage;

public interface ImagePageCalculateStrategy {

    Position getImagePosition();

    Size getPageSize();

    Size getImageSize();

    void study(PageArgument pageArgument, BufferedImage image);
}
