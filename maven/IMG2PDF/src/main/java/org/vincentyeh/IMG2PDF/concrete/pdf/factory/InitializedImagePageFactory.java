package org.vincentyeh.IMG2PDF.concrete.pdf.factory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.vincentyeh.IMG2PDF.concrete.pdf.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.concrete.pdf.objects.PdfBoxPageAdaptor;
import org.vincentyeh.IMG2PDF.concrete.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.framework.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.framework.pdf.calculation.strategy.ImagePageCalculateStrategy;
import org.vincentyeh.IMG2PDF.framework.pdf.factory.InitializedPageFactory;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class InitializedImagePageFactory implements InitializedPageFactory {
    private final PageArgument argument;
    private final File[] files;
    private int index = 0;
    private final PdfDocument<PDDocument> document;
    private final ImagePageCalculateStrategy strategy;
    public InitializedImagePageFactory(PageArgument argument, File[] files, PdfDocument<PDDocument> document, ImagePageCalculateStrategy strategy) {
        this.argument = argument;
        this.files = files;
        this.document = document;
        this.strategy = strategy;
    }

    @Override
    public boolean hasNext() {
        return index < files.length;
    }

    @Override
    public PdfPage<?> generateAndNext() throws Exception {
        PdfPage<?> page= new PdfBoxPageAdaptor(new PDPage(),document.get());
        BufferedImage bufferedImage=readImage(files[index++]);
        strategy.study(argument,bufferedImage);
        page.setSize(strategy.getPageSize());

        page.putImage(bufferedImage,strategy.getImagePosition(),strategy.getImageSize());
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
}
