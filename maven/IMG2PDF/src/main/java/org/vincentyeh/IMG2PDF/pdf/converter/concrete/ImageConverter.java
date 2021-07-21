package org.vincentyeh.IMG2PDF.pdf.converter.concrete;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.IConverter;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.PageIterator;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.PdfDocument;
import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.io.File;

public class ImageConverter extends IConverter {
    private final MemoryUsageSetting setting;

    public ImageConverter(long maxMainMemoryBytes, File tempFolder, boolean overwrite) {
        super(overwrite);
        setting = MemoryUsageSetting.setupMixed(maxMainMemoryBytes).setTempDir(tempFolder);
    }

    @Override
    protected PageIterator<?> getPages(Task task, PdfDocument<?> document) {
        return new PdfBoxPageIterator(task, (PDDocument) document.get());
    }

    @Override
    protected PdfDocument<?> getDocument(Task task) {
        return new PdfBoxDocumentAdaptor(new PDDocument(setting));
    }
}
