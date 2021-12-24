package org.vincentyeh.IMG2PDF.pdf.concrete.converter;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.vincentyeh.IMG2PDF.pdf.concrete.calculation.strategy.StandardImagePageCalculationStrategy;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.pdf.concrete.objects.PdfBoxDocumentAdaptor;
import org.vincentyeh.IMG2PDF.pdf.concrete.objects.PdfBoxPageAdaptor;
import org.vincentyeh.IMG2PDF.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.strategy.ImagePageCalculateStrategy;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.PDFConverter;
import org.vincentyeh.IMG2PDF.pdf.framework.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.pdf.framework.objects.PdfPage;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.MakeDirectoryException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImagePDFConverter extends PDFConverter {
    private final MemoryUsageSetting setting;
    private final ImagePageCalculateStrategy strategy;

    public ImagePDFConverter(long maxMainMemoryBytes, File tempFolder, boolean overwrite,ImagePageCalculateStrategy strategy) throws MakeDirectoryException {
        super(overwrite);
        this.strategy = strategy;
        FileUtils.makeDirectories(tempFolder);
        setting = MemoryUsageSetting.setupMixed(maxMainMemoryBytes).setTempDir(tempFolder);
    }

    @Override
    protected PdfDocument<?> getDocument() {
        return new PdfBoxDocumentAdaptor(new PDDocument(setting));
    }


    @Override
    protected PdfPage<?> generatePage(File file, PageArgument argument, PdfDocument<?> pdfDocument) throws IOException {
        PDDocument document = (PDDocument) pdfDocument.get();
        PdfPage<?> page = new PdfBoxPageAdaptor(new PDPage(), document);
        BufferedImage bufferedImage = readImage(file);
        strategy.study(argument, bufferedImage);
        page.setSize(strategy.getPageSize());

        page.putImage(bufferedImage, strategy.getImagePosition(), strategy.getImageSize());
        return page;
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
