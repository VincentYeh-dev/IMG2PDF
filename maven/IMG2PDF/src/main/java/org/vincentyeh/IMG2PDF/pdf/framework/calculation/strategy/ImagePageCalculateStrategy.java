package org.vincentyeh.IMG2PDF.pdf.framework.calculation.strategy;

import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.Position;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.Size;

import java.awt.image.BufferedImage;

public interface ImagePageCalculateStrategy {

    Position getImagePosition();

    Size getPageSize();

    Size getImageSize();

    void execute(PageArgument pageArgument, BufferedImage image);
}
