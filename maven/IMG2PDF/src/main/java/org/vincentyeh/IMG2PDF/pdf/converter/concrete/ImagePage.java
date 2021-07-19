package org.vincentyeh.IMG2PDF.pdf.converter.concrete;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.vincentyeh.IMG2PDF.pdf.converter.core.ImagePageFactory;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.PdfPage;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;

import java.awt.image.BufferedImage;

public class ImagePage implements PdfPage<PDPage> {
    private final PDPage page;

    public ImagePage(PageArgument pageArgument, BufferedImage image, PDDocument document) throws Exception {
        page = new ImagePageFactory(pageArgument.getAlign(), pageArgument.getSize(), pageArgument.getDirection(), pageArgument.autoRotate()).getImagePage(document, image);
    }

    @Override
    public PDPage get() {
        return page;
    }
}
