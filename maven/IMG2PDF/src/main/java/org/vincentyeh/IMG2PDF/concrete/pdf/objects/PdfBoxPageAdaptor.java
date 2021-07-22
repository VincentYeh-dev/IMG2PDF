package org.vincentyeh.IMG2PDF.concrete.pdf.objects;

import org.apache.pdfbox.pdmodel.PDPage;
import org.vincentyeh.IMG2PDF.framework.pdf.objects.PdfPage;

public class PdfBoxPageAdaptor implements PdfPage<PDPage> {
    private final PDPage page;

    public PdfBoxPageAdaptor(PDPage page) {
        this.page = page;
    }

    @Override
    public PDPage get() {
        return page;
    }
}
