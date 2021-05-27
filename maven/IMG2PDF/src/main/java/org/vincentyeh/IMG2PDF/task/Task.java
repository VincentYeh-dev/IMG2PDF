package org.vincentyeh.IMG2PDF.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.vincentyeh.IMG2PDF.pdf.doc.DocumentArgument;
import org.vincentyeh.IMG2PDF.pdf.page.PageArgument;

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

    @Override
    public String toString() {
        return "Task{" +
                "documentArgument=" + documentArgument +
                ", pageArgument=" + pageArgument +
                ", images=" + Arrays.toString(images) +
                ", pdf_destination=" + pdf_destination +
                '}';
    }
}
