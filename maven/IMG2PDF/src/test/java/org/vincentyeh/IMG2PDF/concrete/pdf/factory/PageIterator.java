package org.vincentyeh.IMG2PDF.concrete.pdf.factory;

import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfPage;

public interface PageIterator {
    boolean hasNext();
    PdfPage<?> generateAndNext() throws Exception;
    int getIndex();
}
