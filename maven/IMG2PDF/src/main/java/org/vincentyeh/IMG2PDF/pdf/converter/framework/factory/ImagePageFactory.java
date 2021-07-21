package org.vincentyeh.IMG2PDF.pdf.converter.framework.factory;

import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfPage;

import java.awt.image.BufferedImage;

public abstract class ImagePageFactory {
    public abstract PdfPage<?> create(BufferedImage image) throws Exception;
    protected final PdfDocument<?> document;

    protected ImagePageFactory(PdfDocument<?> document) {
        this.document = document;
    }

}
