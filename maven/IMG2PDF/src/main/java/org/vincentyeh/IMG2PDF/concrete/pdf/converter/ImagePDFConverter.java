package org.vincentyeh.IMG2PDF.concrete.pdf.converter;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.vincentyeh.IMG2PDF.concrete.pdf.objects.ImagePageStepGenerator;
import org.vincentyeh.IMG2PDF.concrete.pdf.objects.PdfBoxDocumentAdaptor;
import org.vincentyeh.IMG2PDF.framework.pdf.converter.PDFConverter;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PageStepGenerator;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.framework.task.Task;
import org.vincentyeh.IMG2PDF.concrete.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.MakeDirectoryException;

import java.io.File;

public class ImagePDFConverter extends PDFConverter {
    private final MemoryUsageSetting setting;

    public ImagePDFConverter(long maxMainMemoryBytes, File tempFolder, boolean overwrite) throws MakeDirectoryException {
        super(overwrite);
        FileUtils.makeDirectories(tempFolder);
        setting = MemoryUsageSetting.setupMixed(maxMainMemoryBytes).setTempDir(tempFolder);
    }

    @Override
    protected PageStepGenerator getPageStepGenerator(Task task, PdfDocument<?> document) {
        return new ImagePageStepGenerator(task.getPageArgument(), task.getImages(), document);
    }

    @Override
    protected PdfDocument<?> getDocument() {
        return new PdfBoxDocumentAdaptor(new PDDocument(setting));
    }
}
