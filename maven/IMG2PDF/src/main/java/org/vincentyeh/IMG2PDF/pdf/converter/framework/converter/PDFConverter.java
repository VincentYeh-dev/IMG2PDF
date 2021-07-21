package org.vincentyeh.IMG2PDF.pdf.converter.framework.converter;

import org.vincentyeh.IMG2PDF.pdf.converter.concrete.exception.PDFConverterException;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.exception.SaveException;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.listener.ConversionListener;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PageStepGenerator;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.task.framework.Task;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.OverwriteException;

import java.io.File;

public abstract class PDFConverter {

    private final boolean overwrite;
    private ConversionListener listener;

    public PDFConverter(boolean overwrite) {
        this.overwrite = overwrite;
    }

    protected abstract PageStepGenerator getPageGenerator(Task task, PdfDocument<?> document);

    protected abstract PdfDocument<?> getDocument(Task task);

    public final File start(Task task) throws PDFConverterException{
        if (task == null)
            throw new IllegalArgumentException("task is null.");

        if (listener != null)
            listener.initializing(task);

        try {
            checkOverwrite(task.getPdfDestination());

            PdfDocument<?> document = getDocument(task);
            document.setOwnerPassword(task.getDocumentArgument().getOwnerPassword());
            document.setUserPassword(task.getDocumentArgument().getUserPassword());
            document.setPermission(task.getDocumentArgument().getPermission());
            document.encrypt();
            document.setTitle(task.getDocumentArgument().getTitle());

            PageStepGenerator iterator = getPageGenerator(task, document);
            while (iterator.hasNext()) {
                if (listener != null)
                    listener.onConverting(iterator.getIndex(), task.getPdfDestination());
                document.addPage(iterator.generateAndNext());
            }

            document.save(task.getPdfDestination());
            document.close();

            if (listener != null)
                listener.onConversionComplete();

            return task.getPdfDestination();

        } catch (Exception e){
            throw new PDFConverterException(e,task);
        }finally {
            if(listener!=null)
                listener.onFinally();
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
}
