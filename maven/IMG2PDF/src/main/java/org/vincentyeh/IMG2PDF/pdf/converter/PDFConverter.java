package org.vincentyeh.IMG2PDF.pdf.converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.ImagePage;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.PDFBoxDocumentBuilder;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.PDFConversionException;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.PDFConverterException;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.pdf.converter.exception.SaveException;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.ConversionListener;
import org.vincentyeh.IMG2PDF.pdf.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;
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

    private final long maxMainMemoryBytes;
    private final File tempFolder;
    private final boolean overwrite;
    private ConversionListener listener;


    public PDFConverter(long maxMainMemoryBytes, File tempFolder, boolean overwrite) throws IOException {
        this.maxMainMemoryBytes = maxMainMemoryBytes;
        this.tempFolder = tempFolder;
        this.overwrite = overwrite;

        if (tempFolder == null)
            throw new IllegalArgumentException("tempFolder is null");

        FileUtils.makeDirectories(tempFolder);


    }

    public File start(Task task) throws PDFConverterException {
        if (task == null)
            throw new IllegalArgumentException("task is null.");

        initializing(task);

        try {

            PDFBoxDocumentBuilder builder = new PDFBoxDocumentBuilder(maxMainMemoryBytes, tempFolder);

            checkOverwrite(task.getPdfDestination());
            DocumentArgument documentArgument = task.getDocumentArgument();
            builder.setOwnerPassword(documentArgument.getOwnerPassword());
            builder.setUserPassword(documentArgument.getUserPassword());
            builder.setPermission(documentArgument.getPermission());
            builder.encrypt();
            builder.setTitle(documentArgument.getTitle());

            File[] images = task.getImages();

            appendAllPageToDocument(task, images, builder);

            File pdf = savePDF(builder.getResult(), task.getPdfDestination());
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

    private void appendAllPageToDocument(Task task, File[] images, PDFBoxDocumentBuilder builder) throws PDFConversionException, ReadImageException {
        for (int i = 0; i < images.length; i++) {
            onConverting(i, images[i]);
            appendPageToDocument(task.getPageArgument(), images[i], builder);
        }
    }

    private void appendPageToDocument(PageArgument pageArgument, File file, PDFBoxDocumentBuilder builder) throws PDFConversionException, ReadImageException {
        BufferedImage image = readImage(file);
        try {
            builder.addPage(new ImagePage(pageArgument,image,builder.getResult()));
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
