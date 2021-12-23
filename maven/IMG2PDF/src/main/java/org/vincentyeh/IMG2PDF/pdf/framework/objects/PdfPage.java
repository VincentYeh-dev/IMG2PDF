package org.vincentyeh.IMG2PDF.pdf.framework.objects;

import org.vincentyeh.IMG2PDF.pdf.framework.calculation.Position;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.Size;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface PdfPage<PAGE> {
    PAGE get();

    void setSize(Size size);

    Size getSize();

    void putImage(BufferedImage image, Position position, Size size) throws IOException;
}
