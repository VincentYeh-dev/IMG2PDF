package org.vincentyeh.IMG2PDF.concrete.pdf.objects;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vincentyeh.IMG2PDF.framework.pdf.calculation.Position;
import org.vincentyeh.IMG2PDF.framework.pdf.calculation.Size;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfPage;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class PdfBoxPageAdaptor implements PdfPage<PDPage> {
    private final PDPage page;
    private final PDDocument document;

    public PdfBoxPageAdaptor(PDPage page, PDDocument document) {
        this.page = page;
        this.document = document;
    }

    @Override
    public PDPage get() {
        return page;
    }

    @Override
    public void setSize(Size size) {
        page.setMediaBox(new PDRectangle(size.getWidth(), size.getHeight()));
    }

    @Override
    public Size getSize() {
        PDRectangle rectangle = page.getMediaBox();
        return new Size(rectangle.getWidth(), rectangle.getHeight());
    }

    @Override
    public void putImage(BufferedImage image, Position position, Size size) throws IOException {

        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, image);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(pdImageXObject, position.getX(), position.getY(), size.getWidth(), size.getHeight());
        contentStream.close();

    }


}
