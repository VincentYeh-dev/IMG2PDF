package org.vincentyeh.IMG2PDF.task;

import java.io.File;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("task")
public class Task {

    @XStreamAlias("DocumentArgument")
    private final DocumentArgument documentArgument;

    @XStreamAlias("PageArgument")
    private final PageArgument pageArgument;

    @XStreamAlias("images")
    private final File[] images;

    @XStreamAlias("destination")
    private final File pdf_destination;


    public Task(DocumentArgument documentArgument, PageArgument pageArgument, File[] images, File pdf_destination) {
        if (images == null)
            throw new IllegalArgumentException("images is null");
        this.images = images;
        this.pdf_destination = pdf_destination;
        this.documentArgument = documentArgument;
        this.pageArgument = pageArgument;
    }


    public File[] getImages() {
        return this.images;
    }

    public File getPdfDestination() {
        return pdf_destination;
    }

    public DocumentArgument getDocumentArgument() {
        return documentArgument;
    }

    public PageArgument getPageArgument() {
        return pageArgument;
    }

}
