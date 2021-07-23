package org.vincentyeh.IMG2PDF.concrete.pdf.objects;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.vincentyeh.IMG2PDF.concrete.pdf.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.framework.parameter.PageAlign;
import org.vincentyeh.IMG2PDF.framework.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.framework.parameter.PageDirection;
import org.vincentyeh.IMG2PDF.framework.parameter.PageSize;
import org.vincentyeh.IMG2PDF.framework.pdf.factory.*;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.Image;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PageStepGenerator;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfPage;
import org.vincentyeh.IMG2PDF.concrete.util.file.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.vincentyeh.IMG2PDF.framework.parameter.PageDirection.Landscape;
import static org.vincentyeh.IMG2PDF.framework.parameter.PageDirection.Portrait;

public class ImagePageStepGenerator implements PageStepGenerator {
    private final PageArgument argument;
    private final File[] files;
    private int index = 0;
    private final PdfDocument<?> document;

    public ImagePageStepGenerator(PageArgument argument,File[] files, PdfDocument<?> document) {
        this.argument = argument;
        this.files = files;
        this.document = document;
    }

    @Override
    public boolean hasNext() {
        return index < files.length;
    }

    @Override
    public PdfPage<?> generateAndNext() throws Exception {
        PdfPage<?> page= new PdfBoxPageAdaptor(new PDPage());
        BufferedImage bufferedImage=readImage(files[index++]);

        final PageDirection direction = getSuitableDirection(bufferedImage);
        final Size new_page_size = getSuitablePageSize(direction, argument.getSize(),bufferedImage);

        Image<BufferedImage> image =new Image<BufferedImage>() {
            @Override
            public BufferedImage get() {
                return bufferedImage;
            }

            @Override
            public Size getSize() {
                return getMaxScaleImageSize(bufferedImage, new_page_size);
            }

            @Override
            public Position getPosition() {
                return calculateImagePosition(getMaxScaleImageSize(bufferedImage, new_page_size), new_page_size, argument.getAlign());
            }
        };

        page.setSize(new_page_size);
        page.putImage(image,document);

        return page;
    }

    @Override
    public int getIndex() {
        return index;
    }

    private BufferedImage readImage(File file) throws ReadImageException {
        try {
            FileUtils.checkExists(file);
            InputStream is = new FileInputStream(file);

            BufferedImage image = ImageIO.read(is);
            if (image == null)
                throw new RuntimeException("image==null");

            return image;
        } catch (Exception e) {
            throw new ReadImageException(e, file);
        }
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
