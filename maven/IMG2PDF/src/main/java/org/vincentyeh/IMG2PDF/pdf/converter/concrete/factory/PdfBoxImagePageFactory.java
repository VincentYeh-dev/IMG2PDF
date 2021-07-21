package org.vincentyeh.IMG2PDF.pdf.converter.concrete.factory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.objects.PdfBoxDocumentAdaptor;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.objects.PdfBoxPageAdaptor;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.factory.ImagePageFactory;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.factory.Position;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.factory.Size;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfPage;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Page that contain image in PDF File.
 *
 * @author VincentYeh
 */
public class PdfBoxImagePageFactory extends ImagePageFactory {

    @Override
    protected PdfPage<?> createInstance(BufferedImage image, Size page_size, Size image_size, Position position) throws IOException {
        PDDocument document = ((PdfBoxDocumentAdaptor) super.document).get();

        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, image);

        PDPage pdPage = new PDPage(new PDRectangle(page_size.getWidth(), page_size.getHeight()));

        PDPageContentStream contentStream = new PDPageContentStream(document, pdPage);
        contentStream.drawImage(pdImageXObject, position.getX(), position.getY(), image_size.getWidth(), image_size.getHeight());
        contentStream.close();

        return new PdfBoxPageAdaptor(pdPage);
    }

    public PdfBoxImagePageFactory(PageArgument argument, PdfDocument<?> document) {
        super(argument, document);
    }
}
