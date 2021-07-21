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
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfPage;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageAlign;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageDirection;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageSize;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.vincentyeh.IMG2PDF.pdf.parameter.PageDirection.Landscape;
import static org.vincentyeh.IMG2PDF.pdf.parameter.PageDirection.Portrait;

/**
 * Page that contain image in PDF File.
 *
 * @author VincentYeh
 */
public class PdfBoxImagePageFactory extends ImagePageFactory {


    private final PageArgument argument;

    public PdfBoxImagePageFactory(PageArgument argument, PdfDocument<?> document) {
        super(document);
        this.argument = argument;

        if (document == null)
            throw new IllegalArgumentException("document==null");
    }

    @Override
    public PdfPage<?> create(BufferedImage image) throws IOException {

        if (image == null)
            throw new IllegalArgumentException("rawImage==null");

        PageDirection direction= getSuitableDirection(image);
        Size new_page_size = getSuitablePageSize(direction,argument.getSize(), image);
        Size new_image_size = getMaxScaleImageSize(image, new_page_size);
        final Position new_position = calculateImagePosition(new_image_size, new_page_size,argument.getAlign());

        return new PdfBoxPageAdaptor(createPDPage(((PdfBoxDocumentAdaptor)document).get(),image,new_image_size,new_position,new_page_size));
    }


    private PDPage createPDPage(PDDocument document, BufferedImage image, Size image_size, Position position, Size page_size) throws IOException {
        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, image);

        PDPage pdPage = new PDPage(new PDRectangle(page_size.getWidth(), page_size.getHeight()));

        PDPageContentStream contentStream = new PDPageContentStream(document, pdPage);
        contentStream.drawImage(pdImageXObject, position.getX(), position.getY(), image_size.getWidth(), image_size.getHeight());
        contentStream.close();

        return pdPage;
    }

    private Size getMaxScaleImageSize(BufferedImage rawImage, Size page_size) {
        return new SizeCalculator().scaleUpToMax(new Size(rawImage.getWidth(), rawImage.getHeight()), page_size);
    }

    private Position calculateImagePosition(Size img_size, Size page_size, PageAlign align) {
        PositionCalculator calculator=new PositionCalculator(align);
        return calculator.calculate(img_size,page_size);
    }

    private PageDirection getSuitableDirection(BufferedImage image) {
        if (argument.getSize()== PageSize.DEPEND_ON_IMG) {
            return Portrait;
        }
        if (argument.autoRotate()) {
            return detectDirection(image.getHeight(),image.getWidth());
        } else {
            return argument.getDirection();
        }
    }

    private Size getSuitablePageSize(PageDirection direction, PageSize pageSize, BufferedImage image) {
        if (pageSize == PageSize.DEPEND_ON_IMG) {
            return getImageSize(image,false);
        } else {
            PDRectangle rect = pageSize.getPdrectangle();
            return getPageSize(rect,direction==Landscape);
        }
    }

    private Size getImageSize(BufferedImage image,boolean reverse){
        return new Size(reverse? image.getHeight():image.getWidth(),reverse?image.getWidth():image.getHeight());
    }

    private Size getPageSize(PDRectangle rectangle,boolean reverse){
        return new Size(reverse? rectangle.getHeight():rectangle.getWidth(),reverse?rectangle.getWidth():rectangle.getHeight());
    }

    private PageDirection detectDirection(float height,float width) {
        return PageDirection.detectDirection(height,width);
    }

}
