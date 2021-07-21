package org.vincentyeh.IMG2PDF.pdf.converter.concrete.converter;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.objects.PdfBoxDocumentAdaptor;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.objects.PdfBoxPageGenerator;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.converter.PDFConverter;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PageGenerator;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.io.File;

public class ImagePDFConverter extends PDFConverter {
    private final MemoryUsageSetting setting;

    public ImagePDFConverter(long maxMainMemoryBytes, File tempFolder, boolean overwrite) {
        super(overwrite);
        setting = MemoryUsageSetting.setupMixed(maxMainMemoryBytes).setTempDir(tempFolder);
    }

    @Override
    protected PageGenerator getPageGenerator(Task task, PdfDocument<?> document) {
        return new PdfBoxPageGenerator(task, (PDDocument) document.get());
    }

    @Override
    protected PdfDocument<?> getDocument(Task task) {
        return new PdfBoxDocumentAdaptor(new PDDocument(setting));
    }
}
