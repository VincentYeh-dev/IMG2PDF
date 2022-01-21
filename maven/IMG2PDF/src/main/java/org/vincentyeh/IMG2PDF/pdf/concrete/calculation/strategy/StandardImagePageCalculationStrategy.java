package org.vincentyeh.IMG2PDF.pdf.concrete.calculation.strategy;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageAlign;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageDirection;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageSize;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.Position;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.PositionCalculator;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.Size;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.SizeCalculator;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.strategy.ImagePageCalculateStrategy;

import java.awt.image.BufferedImage;

import static org.vincentyeh.IMG2PDF.pdf.parameter.PageDirection.Landscape;
import static org.vincentyeh.IMG2PDF.pdf.parameter.PageDirection.Portrait;

public class StandardImagePageCalculationStrategy implements ImagePageCalculateStrategy {
    private Size pageSize;
    private Size imageSize;
    private Position imagePosition;

    @Override
    public Position getImagePosition() {
        return imagePosition;
    }

    @Override
    public Size getPageSize() {
        return pageSize;
    }

    @Override
    public Size getImageSize() {
        return imageSize;
    }

    @Override
    public void study(PageArgument argument, BufferedImage bufferedImage) {
        PageDirection direction = getSuitableDirection(argument,bufferedImage);
        pageSize = getSuitablePageSize(direction, argument.getSize(), bufferedImage);
        imageSize = getMaxScaleImageSize(bufferedImage, pageSize);
        imagePosition = calculateImagePosition(getMaxScaleImageSize(bufferedImage, pageSize), pageSize, argument.getAlign());
    }

    private Size getMaxScaleImageSize(BufferedImage rawImage, Size page_size) {
        return new SizeCalculator().scaleUpToMax(new Size(rawImage.getWidth(), rawImage.getHeight()), page_size);
    }

    private Position calculateImagePosition(Size img_size, Size page_size, PageAlign align) {
        PositionCalculator calculator = new PositionCalculator(align);
        return calculator.calculate(img_size, page_size);
    }

    private PageDirection getSuitableDirection(PageArgument argument,BufferedImage image) {
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
