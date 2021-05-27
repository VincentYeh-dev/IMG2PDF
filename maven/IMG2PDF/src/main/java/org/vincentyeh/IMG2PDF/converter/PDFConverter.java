package org.vincentyeh.IMG2PDF.converter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.vincentyeh.IMG2PDF.converter.exception.ConversionException;
import org.vincentyeh.IMG2PDF.converter.exception.OverwriteDenyException;
import org.vincentyeh.IMG2PDF.converter.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.converter.listener.ConversionInfoListener;
import org.vincentyeh.IMG2PDF.pdf.page.core.ImagePageFactory;
import org.vincentyeh.IMG2PDF.task.Task;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
//import org.vincentyeh.IMG2PDF.util.file.FileChecker;

import javax.imageio.ImageIO;

/**
 * The core of this program. At first,this class will be initialized by task.
 * When call() being called by other program,this program will start conversion
 * and finally return ImagesPDFDocument.
 *
 * @author VincentYeh
 */
public class PDFConverter implements ConversionInfoListener {

    private final PDDocument document;
    private final Task task;
    private final boolean overwrite;
    private ConversionInfoListener info_listener;

    public PDFConverter(Task task, long maxMainMemoryBytes, File tempFolder, boolean overwrite) throws IOException {
        this.overwrite = overwrite;

        if (task == null)
            throw new IllegalArgumentException("task is null.");
        this.task = task;

        if (tempFolder == null)
            throw new IllegalArgumentException("tempFolder is null");

        FileUtils.makeDirsIfNotExists(tempFolder);

        document = new PDDocument(MemoryUsageSetting.setupMixed(maxMainMemoryBytes).setTempDir(tempFolder));
        document.protect(task.getDocumentArgument().getSpp());

    }

    public File start() throws OverwriteDenyException, IOException {
        onConversionPreparing(task);
        checkOverwrite();

        File[] images = task.getImages();
        appendAllPageToDocument(images);

        File pdf = savePDFAndClose();
        onConversionComplete(pdf);
        return pdf;
    }

    private File savePDFAndClose() throws IOException {
        try {
            FileUtils.makeDirsIfNotExists(task.getPdfDestination().getParentFile());
            document.save(task.getPdfDestination());
            return task.getPdfDestination();
        } finally {
            closeDocument();
        }
    }

    private void appendAllPageToDocument(File[] images) throws ConversionException, ReadImageException {
        try {
            for (int i = 0; i < images.length; i++) {
                onConverting(i, images[i]);
                appendPageToDocument(images[i]);
            }
        } catch (Exception e) {
            closeDocument();
            throw e;
        }
    }

    private void appendPageToDocument(File file) throws ConversionException, ReadImageException {
        BufferedImage image = readImage(file);
        try {
            document.addPage(getImagePage(image));
        } catch (Exception e) {
            throw new ConversionException(file, e);
        }

    }

    private BufferedImage readImage(File image) throws ReadImageException {
        try {
            return ImageIO.read(image);
        } catch (Exception e) {
            throw new ReadImageException(image, e);
        }
    }

    private void checkOverwrite() throws OverwriteDenyException {
        if (!overwrite && task.getPdfDestination().exists()) {
            closeDocument();
            throw new OverwriteDenyException(task.getPdfDestination());
        }
    }

    private PDPage getImagePage(BufferedImage img) throws Exception {
        return ImagePageFactory.getImagePage(document, task.getPageArgument(), img);
    }

    private void closeDocument() {
        try {
            document.close();
        } catch (Exception ignored) {
        }
    }

    public void setInfoListener(ConversionInfoListener listener) {
        this.info_listener = listener;
    }

    @Override
    public void onConversionPreparing(Task task) {
        if (info_listener != null)
            info_listener.onConversionPreparing(task);
    }

    @Override
    public void onConverting(int index, File file) {
        if (info_listener != null)
            info_listener.onConverting(index, file);
    }

    @Override
    public void onConversionComplete(File dst) {
        if (info_listener != null)
            info_listener.onConversionComplete(dst);
    }
}
