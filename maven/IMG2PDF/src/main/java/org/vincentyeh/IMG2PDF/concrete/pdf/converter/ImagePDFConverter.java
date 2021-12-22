package org.vincentyeh.IMG2PDF.concrete.pdf.converter;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.vincentyeh.IMG2PDF.concrete.pdf.calculation.strategy.StandardImagePageCalculationStrategy;
import org.vincentyeh.IMG2PDF.concrete.pdf.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.concrete.pdf.objects.PdfBoxDocumentAdaptor;
import org.vincentyeh.IMG2PDF.concrete.pdf.objects.PdfBoxPageAdaptor;
import org.vincentyeh.IMG2PDF.framework.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.framework.pdf.calculation.strategy.ImagePageCalculateStrategy;
import org.vincentyeh.IMG2PDF.framework.pdf.converter.PDFConverter;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfPage;
import org.vincentyeh.IMG2PDF.concrete.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.MakeDirectoryException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImagePDFConverter extends PDFConverter {
    private final MemoryUsageSetting setting;
    private final ImagePageCalculateStrategy strategy;

    public ImagePDFConverter(long maxMainMemoryBytes, File tempFolder, boolean overwrite) throws MakeDirectoryException {
        super(overwrite);
        this.strategy = new StandardImagePageCalculationStrategy();
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
