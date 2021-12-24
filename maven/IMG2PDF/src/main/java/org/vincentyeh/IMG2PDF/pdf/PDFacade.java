package org.vincentyeh.IMG2PDF.pdf;

import org.vincentyeh.IMG2PDF.pdf.concrete.calculation.strategy.StandardImagePageCalculationStrategy;
import org.vincentyeh.IMG2PDF.pdf.concrete.converter.ImagePDFConverter;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.PDFConverter;
import org.vincentyeh.IMG2PDF.pdf.framework.listener.ConversionListener;
import org.vincentyeh.IMG2PDF.util.BytesSize;
import org.vincentyeh.IMG2PDF.util.file.exception.MakeDirectoryException;

import java.io.File;

public class PDFacade {
    private PDFacade() {

    }

    public static PDFConverter createImagePDFConverter(BytesSize maxMainMemoryBytes, File tempFolder, boolean overwrite_output, ConversionListener listener) throws MakeDirectoryException {
        PDFConverter converter = new ImagePDFConverter(maxMainMemoryBytes.getBytes(), tempFolder, overwrite_output, new StandardImagePageCalculationStrategy());
        converter.setListener(listener);
        return converter;
    }
}
