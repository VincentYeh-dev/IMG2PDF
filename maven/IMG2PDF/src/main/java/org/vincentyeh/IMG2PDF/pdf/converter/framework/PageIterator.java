package org.vincentyeh.IMG2PDF.pdf.converter.framework;

public interface PageIterator<T> {
    boolean hasNext();
    PdfPage<?> next() throws Exception;
    int getIndex();
}
