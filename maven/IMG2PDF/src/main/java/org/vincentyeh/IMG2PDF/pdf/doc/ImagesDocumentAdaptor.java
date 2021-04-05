package org.vincentyeh.IMG2PDF.pdf.doc;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.vincentyeh.IMG2PDF.pdf.page.ImagePage;

import java.io.IOException;

public class ImagesDocumentAdaptor {

    private final DocumentArgument argument;
    private final PDDocument document;
    public ImagesDocumentAdaptor(DocumentArgument argument) throws IOException {
        this.argument=argument;
        document=new PDDocument();
        document.protect(argument.getSpp());
    }

    public void save() throws IOException {
        document.save(argument.getDestination());
    }

    public void addPage(ImagePage imgPage) {
        document.addPage(imgPage);
    }

    public PDDocument getDocument() {
        return document;
    }

    /**
     * Close PDF Document.
     */
    public void closeDocument() {
        try {
            if (document != null)
                document.close();
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }
}
