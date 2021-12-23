package org.vincentyeh.IMG2PDF.pdf.concrete.factory;

import org.vincentyeh.IMG2PDF.pdf.framework.objects.PdfPage;

public interface PageIterator {
    boolean hasNext();
    PdfPage<?> generateAndNext() throws Exception;
    int getIndex();
}
