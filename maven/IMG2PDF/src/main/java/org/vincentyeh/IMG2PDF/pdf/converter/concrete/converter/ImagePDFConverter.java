package org.vincentyeh.IMG2PDF.pdf.converter.concrete.converter;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.factory.PdfBoxImagePageFactory;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.objects.ImagePageStepGenerator;
import org.vincentyeh.IMG2PDF.pdf.converter.concrete.objects.PdfBoxDocumentAdaptor;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.converter.PDFConverter;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PageStepGenerator;
import org.vincentyeh.IMG2PDF.pdf.converter.framework.objects.PdfDocument;
import org.vincentyeh.IMG2PDF.task.framework.Task;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.MakeDirectoryException;

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
        return new ImagePageStepGenerator(task.getImages(), new PdfBoxImagePageFactory(task.getPageArgument(),document));
    }

    @Override
    protected PdfDocument<?> getDocument(Task task) {
        return new PdfBoxDocumentAdaptor(new PDDocument(setting));
    }
}
