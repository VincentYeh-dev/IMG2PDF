package org.vincentyeh.IMG2PDF.pdf.concrete.converter;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.vincentyeh.IMG2PDF.image.reader.framework.ImageReader;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.pdf.concrete.objects.PdfBoxDocumentAdaptor;
import org.vincentyeh.IMG2PDF.pdf.concrete.objects.PdfBoxPageAdaptor;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.pdf.framework.calculation.strategy.ImagePageCalculateStrategy;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.PDFConverter;
import org.vincentyeh.IMG2PDF.pdf.framework.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.pdf.framework.objects.PdfPage;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.MakeDirectoryException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePDFConverter extends PDFConverter {
    private final MemoryUsageSetting setting;
    private final ImagePageCalculateStrategy strategy;
    private final ImageReader reader;

    public ImagePDFConverter(long maxMainMemoryBytes, File tempFolder, boolean overwrite, ImagePageCalculateStrategy strategy, ImageReader reader) throws MakeDirectoryException {
        super(overwrite);
        this.strategy = strategy;
        this.reader = reader;
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
            BufferedImage image = reader.read(file);
            if (image == null)
                throw new RuntimeException("image==null");
            return image;
        } catch (Exception e) {
            throw new ReadImageException(e, file);
        }
    }
}
