package org.vincentyeh.IMG2PDF.pdf.framework.converter;

import org.apache.pdfbox.contentstream.operator.state.Save;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.exception.PDFConversionException;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.exception.SaveException;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.OverwriteException;
import org.vincentyeh.IMG2PDF.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.pdf.framework.listener.ConversionListener;
import org.vincentyeh.IMG2PDF.pdf.framework.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.pdf.framework.objects.PdfPage;
import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.io.File;
import java.io.IOException;

public abstract class PDFConverter {

    private final boolean overwrite;
    private ConversionListener listener;

    public PDFConverter(boolean overwrite) {
        this.overwrite = overwrite;
    }

    protected abstract PdfDocument<?> getDocument();

    protected abstract PdfPage<?> generatePage(File file, PageArgument pageArgument, PdfDocument<?> document) throws IOException;


    public final File start(Task task) throws PDFConversionException {
        if (task == null)
            throw new IllegalArgumentException("task is null.");

        if (listener != null)
            listener.initializing(task);

        try {
            checkOverwrite(task.getPdfDestination());

            PdfDocument<?> document = getDocument();
            document.setOwnerPassword(task.getDocumentArgument().getOwnerPassword());
            document.setUserPassword(task.getDocumentArgument().getUserPassword());
            document.setPermission(task.getDocumentArgument().getPermission());
            document.encrypt();
            document.setInfo(task.getDocumentArgument().getInformation());

            File[] files = task.getImages();
            for (int i = 0; i < files.length; i++) {
                if (listener != null)
                    listener.onConverting(i);
                document.addPage(generatePage(files[i], task.getPageArgument(), document));
            }

            try {
                document.save(task.getPdfDestination());
            } catch (IOException e) {
                throw new SaveException(e,task.getPdfDestination());
            }

            document.close();

            if (listener != null)
                listener.onConversionComplete();

            return task.getPdfDestination();
        } catch (Exception e) {
            throw new PDFConversionException(task, e);
        } finally {
            if (listener != null)
                listener.onFinally();
        }

    }

    private void checkOverwrite(File file) throws SaveException {
        if (!overwrite) {
            try {
                FileUtils.checkOverwrite(file, "PDF overwrite deny,File is already exists:" + file.getAbsoluteFile());
            } catch (OverwriteException e) {
                throw new SaveException(e, file);
            }
        }
    }

    public void setListener(ConversionListener listener) {
        this.listener = listener;
    }

}
