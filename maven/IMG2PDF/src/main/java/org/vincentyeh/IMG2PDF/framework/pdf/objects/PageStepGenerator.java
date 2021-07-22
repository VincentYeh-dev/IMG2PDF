package org.vincentyeh.IMG2PDF.framework.pdf.objects;

public interface PageStepGenerator {
    boolean hasNext();
    PdfPage<?> generateAndNext() throws Exception;
    int getIndex();
}
