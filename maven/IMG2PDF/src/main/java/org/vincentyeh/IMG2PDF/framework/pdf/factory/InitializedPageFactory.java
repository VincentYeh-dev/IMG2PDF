package org.vincentyeh.IMG2PDF.framework.pdf.factory;

import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfPage;

public interface InitializedPageFactory {
    boolean hasNext();
    PdfPage<?> generateAndNext() throws Exception;
    int getIndex();
}
