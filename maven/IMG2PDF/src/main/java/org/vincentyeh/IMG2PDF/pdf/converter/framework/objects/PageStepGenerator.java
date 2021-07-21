package org.vincentyeh.IMG2PDF.pdf.converter.framework.objects;

public interface PageStepGenerator {
    boolean hasNext();
    PdfPage<?> generateAndNext() throws Exception;
    int getIndex();
}
