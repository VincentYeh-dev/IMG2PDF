package org.vincentyeh.IMG2PDF.framework.pdf.objects;

import org.vincentyeh.IMG2PDF.framework.pdf.calculation.Size;

import java.io.IOException;

public interface PdfPage<PAGE> {
    PAGE get();
    void setSize(Size size);
    Size getSize();
    void putImage(Image<?> image, PdfDocument<?> document) throws IOException;
}
