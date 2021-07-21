package org.vincentyeh.IMG2PDF.pdf.converter.concrete.objects;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.factory.ImagePageFactory;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PageGenerator;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfPage;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.task.framework.Task;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class PdfBoxPageGenerator implements PageGenerator {
    private final PDDocument document;
    private final File[] files;
    private final ImagePageFactory factory;
    private int index=0;

    public PdfBoxPageGenerator(Task task, PDDocument document) {
        this.document = document;
        PageArgument pageArgument=task.getPageArgument();
        files = task.getImages();
        factory=new ImagePageFactory(pageArgument.getAlign(), pageArgument.getSize(), pageArgument.getDirection(), pageArgument.autoRotate());
    }

    @Override
    public boolean hasNext() {
        return index<files.length;
    }

    @Override
    public PdfPage<?> generateAndNext() throws Exception{
        return new PdfBoxPageAdaptor(factory.getImagePage(document, readImage(files[index++])));
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
