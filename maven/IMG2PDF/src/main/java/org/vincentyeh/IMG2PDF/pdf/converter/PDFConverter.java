package org.vincentyeh.IMG2PDF.pdf.converter;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.vincentyeh.IMG2PDF.pdf.converter.core.ImagePageFactory;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.PDFConversionException;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.PDFConverterException;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.SaveException;
import org.vincentyeh.IMG2PDF.pdf.converter.listener.ConversionListener;
import org.vincentyeh.IMG2PDF.task.framework.DocumentArgument;
import org.vincentyeh.IMG2PDF.task.framework.Task;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.OverwriteException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The core of this program. At first,this class will be initialized by task.
 * When call() being called by other program,this program will start conversion
 * and finally return ImagesPDFDocument.
 *
 * @author VincentYeh
 */
public class PDFConverter implements ConversionListener {

    private final boolean overwrite;
    private ConversionListener listener;

    private final MemoryUsageSetting memoryUsageSetting;

    public PDFConverter(long maxMainMemoryBytes, File tempFolder, boolean overwrite) throws IOException {
        this.overwrite = overwrite;

        if (tempFolder == null)
            throw new IllegalArgumentException("tempFolder is null");

        FileUtils.makeDirectories(tempFolder);

        memoryUsageSetting = MemoryUsageSetting.setupMixed(maxMainMemoryBytes).setTempDir(tempFolder);
    }

    public File start(Task task) throws PDFConverterException {
        if (task == null)
            throw new IllegalArgumentException("task is null.");

        initializing(task);

        try (PDDocument document = new PDDocument(memoryUsageSetting)) {
            checkOverwrite(task.getPdfDestination());
            document.protect(createProtectionPolicy(task.getDocumentArgument()));

            File[] images = task.getImages();
            appendAllPageToDocument(task, images, document);
            File pdf = savePDF(document, task.getPdfDestination());
            onConversionComplete();
            return pdf;
        } catch (Exception e) {
            throw new PDFConverterException(e, task);
        } finally {
            onFinally();
        }
    }

    private File savePDF(PDDocument document, File file) throws SaveException {
        try {
            FileUtils.checkFileValidity(file);
            FileUtils.makeDirectories(FileUtils.getParentFile(file));
            document.save(file);
            return file;
        } catch (Exception e) {
            throw new SaveException(e);
        }
    }

    private void appendAllPageToDocument(Task task, File[] images, PDDocument document) throws PDFConversionException, ReadImageException {
        for (int i = 0; i < images.length; i++) {
            onConverting(i, images[i]);
            appendPageToDocument(task, images[i], document);
        }
    }

    private void appendPageToDocument(Task task, File file, PDDocument document) throws PDFConversionException, ReadImageException {
        BufferedImage image = readImage(file);
        try {
            document.addPage(getImagePage(task, image, document));
        } catch (Exception e) {
            throw new PDFConversionException(e);
        }

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

    private void checkOverwrite(File file) throws SaveException {
        if (!overwrite) {
            try {
                FileUtils.checkOverwrite(file, "PDF overwrite deny,File is already exists:" + file.getAbsoluteFile());
            } catch (OverwriteException e) {
                throw new SaveException(e);
            }
        }
    }

    private PDPage getImagePage(Task task, BufferedImage image, PDDocument document) throws Exception {
        ImagePageFactory.Builder builder = new ImagePageFactory.Builder();
        builder.setAutoRotate(task.getPageArgument().autoRotate());
        builder.setDirection(task.getPageArgument().getDirection());
        builder.setSize(task.getPageArgument().getSize());
        builder.setAlign(task.getPageArgument().getAlign());

        return builder.build().getImagePage(document, image);
    }

    private StandardProtectionPolicy createProtectionPolicy(DocumentArgument spec) {

        // Define the length of the encryption key.
        // Possible values are 40 or 128 (256 will be available in PDFBox 2.0).
        int keyLength = 128;
        StandardProtectionPolicy spp = new StandardProtectionPolicy(spec.getOwnerPassword(), spec.getUserPassword(), spec.getAccessPermission());
        spp.setEncryptionKeyLength(keyLength);
        return spp;
    }

    public void setListener(ConversionListener listener) {
        this.listener = listener;
    }


    @Override
    public void initializing(Task task) {
        if (listener != null) {
            listener.initializing(task);
        }
    }

    @Override
    public void onConverting(int index, File file) {
        if (listener != null)
            listener.onConverting(index, file);
    }

    @Override
    public void onConversionComplete() {
        if (listener != null)
            listener.onConversionComplete();
    }

    @Override
    public void onFinally() {
        if (listener != null)
            listener.onFinally();
    }


}
