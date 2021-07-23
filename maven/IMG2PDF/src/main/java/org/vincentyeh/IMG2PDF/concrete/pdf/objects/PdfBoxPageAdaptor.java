package org.vincentyeh.IMG2PDF.concrete.pdf.objects;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vincentyeh.IMG2PDF.framework.pdf.factory.Size;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.Image;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfPage;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class PdfBoxPageAdaptor implements PdfPage<PDPage> {
    private final PDPage page;

    public PdfBoxPageAdaptor(PDPage page) {
        this.page = page;
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
        PDRectangle rectangle=page.getMediaBox();
        return new Size(rectangle.getWidth(),rectangle.getHeight());
    }

    @Override
    public void putImage(Image<?> image, PdfDocument<?> document) throws IOException {

        PDDocument doc = ((PdfBoxDocumentAdaptor) document).get();
        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(doc, (BufferedImage) image.get());

        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        contentStream.drawImage(pdImageXObject, image.getPosition().getX(), image.getPosition().getY(), image.getSize().getWidth(), image.getSize().getHeight());
        contentStream.close();

    }


}
