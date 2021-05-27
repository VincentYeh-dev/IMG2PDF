package org.vincentyeh.IMG2PDF.pdf.page.core;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vincentyeh.IMG2PDF.pdf.page.PageAlign;
import org.vincentyeh.IMG2PDF.task.PageArgument;
import org.vincentyeh.IMG2PDF.pdf.page.PageDirection;
import org.vincentyeh.IMG2PDF.pdf.page.PageSize;

import static org.vincentyeh.IMG2PDF.pdf.page.PageDirection.Landscape;
import static org.vincentyeh.IMG2PDF.pdf.page.PageDirection.Portrait;

/**
 * Page that contain image in PDF File.
 *
 * @author VincentYeh
 */
public class ImagePageFactory {

    public static PDPage getImagePage(PDDocument document, PageArgument argument, BufferedImage rawImage) throws Exception {
        ProxyImage proxyImage = new ProxyImage(rawImage);

        ProxyPage page=new ProxyPage(new PDPage(), getPageSize(getPageDirection(argument.getSize(), proxyImage.getSize(), argument.getDirection(), argument.getAutoRotate()), argument.getSize(), proxyImage.getSize()));

        ProxyImage outProxyImage = getCalculatedImage(proxyImage,page.getSize());
        final Position position = calculatePosition(outProxyImage.getSize(),page.getSize(), argument.getAlign());

        return drawImageToPage(document,outProxyImage,page,position);
    }

    private static PDPage drawImageToPage(PDDocument document, ProxyImage outImage, ProxyPage page, Position position) throws IOException {
        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, outImage.get());
        PDPageContentStream contentStream = new PDPageContentStream(document, page.get());
        contentStream.drawImage(pdImageXObject, position.getX(), position.getY(), outImage.getSize().getWidth(), outImage.getSize().getHeight());
        contentStream.close();
        return page.get();
    }

    private static ProxyImage getCalculatedImage(ProxyImage proxyImage, Size page_size) {
        final Size img_size = calculateImageSize(proxyImage.getSize(), page_size);
        return new ProxyImage(proxyImage.get(), img_size);
    }

    private static Size calculateImageSize(Size image_size, Size page_size) {
        return SizeCalculator.getInstance().scaleUpToMax(image_size, page_size);
    }

    private static Position calculatePosition(Size img_size, Size page_size, PageAlign align) {
        PositionCalculator positionCalculator = PositionCalculator.getInstance();
        PositionCalculator.init(img_size.getHeight(), img_size.getWidth(), page_size.getHeight(), page_size.getWidth());
        return positionCalculator.calculate(align);
    }

    private static PageDirection getPageDirection(PageSize page_size, Size image_size, PageDirection direction, boolean autoRotate) {
        if (page_size == PageSize.DEPEND_ON_IMG) {
            return Portrait;
        }
        if (autoRotate) {
            return detectDirection(image_size);
        } else {
            return direction;
        }
    }


    private static Size getPageSize(PageDirection direction, PageSize pageSize, Size imageSize) {
        if (pageSize == PageSize.DEPEND_ON_IMG) {
            return new Size(imageSize.getHeight(), imageSize.getWidth());
        } else {
            PDRectangle rect = pageSize.getPdrectangle();
            if (direction == Landscape)
                return new Size(rect.getWidth(), rect.getHeight());
            else
                return new Size(rect.getHeight(), rect.getWidth());
        }
    }

    private static PageDirection detectDirection(Size size) {
        return PageDirection.detectDirection(size.getHeight(), size.getWidth());
    }

}
