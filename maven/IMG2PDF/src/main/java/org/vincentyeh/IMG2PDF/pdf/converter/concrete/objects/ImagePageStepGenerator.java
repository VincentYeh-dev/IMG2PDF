package org.vincentyeh.IMG2PDF.pdf.converter.concrete.objects;

import org.vincentyeh.IMG2PDF.pdf.converter.concrete.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.factory.ImagePageFactory;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PageStepGenerator;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfPage;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ImagePageStepGenerator implements PageStepGenerator {
    private final File[] files;
    private final ImagePageFactory  factory;
    private int index = 0;

    public ImagePageStepGenerator(File[] files, ImagePageFactory factory) {
        this.files = files;
        this.factory=factory;
    }

    @Override
    public boolean hasNext() {
        return index < files.length;
    }

    @Override
    public PdfPage<?> generateAndNext() throws Exception {
        return factory.create(readImage(files[index++]));
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
