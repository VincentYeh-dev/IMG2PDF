package org.vincentyeh.IMG2PDF.framework.pdf.converter;

import org.vincentyeh.IMG2PDF.concrete.pdf.exception.PDFConverterException;
import org.vincentyeh.IMG2PDF.concrete.pdf.exception.SaveException;
import org.vincentyeh.IMG2PDF.concrete.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.OverwriteException;
import org.vincentyeh.IMG2PDF.framework.pdf.listener.ConversionListener;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PageStepGenerator;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.framework.task.Task;

import java.io.File;

public abstract class PDFConverter {

    private final boolean overwrite;
    private ConversionListener listener;

    public PDFConverter(boolean overwrite) {
        this.overwrite = overwrite;
    }

    protected abstract PageStepGenerator getPageStepGenerator(Task task, PdfDocument<?> document);

    protected abstract PdfDocument<?> getDocument();

    public final File start(Task task) throws PDFConverterException{
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

            PageStepGenerator iterator = getPageStepGenerator(task, document);
            while (iterator.hasNext()) {
                if (listener != null)
                    listener.onConverting(iterator.getIndex());
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
