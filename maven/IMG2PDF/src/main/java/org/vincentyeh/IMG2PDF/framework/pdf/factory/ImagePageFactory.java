package org.vincentyeh.IMG2PDF.framework.pdf.factory;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfPage;
import org.vincentyeh.IMG2PDF.framework.parameter.PageAlign;
import org.vincentyeh.IMG2PDF.framework.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.framework.parameter.PageDirection;
import org.vincentyeh.IMG2PDF.framework.parameter.PageSize;

import java.awt.image.BufferedImage;

import static org.vincentyeh.IMG2PDF.framework.parameter.PageDirection.Landscape;
import static org.vincentyeh.IMG2PDF.framework.parameter.PageDirection.Portrait;

public abstract class ImagePageFactory {

    private final PageArgument argument;
    protected final PdfDocument<?> document;

    protected abstract PdfPage<?> createInstance(BufferedImage image, Size new_page_size, Size new_image_size, Position new_position) throws Exception;

    protected ImagePageFactory(PageArgument argument, PdfDocument<?> document) {
        if (document == null)
            throw new IllegalArgumentException("document==null");

        this.argument = argument;
        this.document = document;
    }

    public final PdfPage<?> create(BufferedImage image) throws Exception {
        if (image == null)
            throw new IllegalArgumentException("rawImage==null");

        final PageDirection direction = getSuitableDirection(image);
        final Size new_page_size = getSuitablePageSize(direction, argument.getSize(), image);
        final Size new_image_size = getMaxScaleImageSize(image, new_page_size);
        final Position new_position = calculateImagePosition(new_image_size, new_page_size, argument.getAlign());

        return createInstance(image, new_page_size, new_image_size, new_position);
    }


    private Size getMaxScaleImageSize(BufferedImage rawImage, Size page_size) {
        return new SizeCalculator().scaleUpToMax(new Size(rawImage.getWidth(), rawImage.getHeight()), page_size);
    }

    private Position calculateImagePosition(Size img_size, Size page_size, PageAlign align) {
        PositionCalculator calculator = new PositionCalculator(align);
        return calculator.calculate(img_size, page_size);
    }

    private PageDirection getSuitableDirection(BufferedImage image) {
        if (argument.getSize() == PageSize.DEPEND_ON_IMG) {
            return Portrait;
        }
        if (argument.autoRotate()) {
            return detectDirection(image.getHeight(), image.getWidth());
        } else {
            return argument.getDirection();
        }
    }

    private Size getSuitablePageSize(PageDirection direction, PageSize pageSize, BufferedImage image) {
        if (pageSize == PageSize.DEPEND_ON_IMG) {
            return getImageSize(image, false);
        } else {
            PDRectangle rect = pageSize.getPdrectangle();
            return getPageSize(rect, direction == Landscape);
        }
    }

    private Size getImageSize(BufferedImage image, boolean reverse) {
        return new Size(reverse ? image.getHeight() : image.getWidth(), reverse ? image.getWidth() : image.getHeight());
    }

    private Size getPageSize(PDRectangle rectangle, boolean reverse) {
        return new Size(reverse ? rectangle.getHeight() : rectangle.getWidth(), reverse ? rectangle.getWidth() : rectangle.getHeight());
    }

    private PageDirection detectDirection(float height, float width) {
        return PageDirection.detectDirection(height, width);
    }
}
