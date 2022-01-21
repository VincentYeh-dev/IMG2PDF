package org.vincentyeh.IMG2PDF.task.framework;

import org.vincentyeh.IMG2PDF.pdf.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;

import java.io.File;

public final class Task {

    private final DocumentArgument documentArgument;
    private final PageArgument pageArgument;
    private final File[] images;
    private final File destination;

    public Task(DocumentArgument documentArgument, PageArgument pageArgument, File[] images, File destination) {
        this.documentArgument = documentArgument;
        this.pageArgument = pageArgument;
        this.images = images;
        this.destination = destination;
    }

    public DocumentArgument getDocumentArgument() {
        return documentArgument;
    }

    public PageArgument getPageArgument() {
        return pageArgument;
    }

    public File[] getImages() {
        return images;
    }

    public File getPdfDestination() {
        return destination;
    }
}
