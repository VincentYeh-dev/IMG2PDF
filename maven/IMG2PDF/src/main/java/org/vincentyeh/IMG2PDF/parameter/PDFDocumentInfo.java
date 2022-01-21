package org.vincentyeh.IMG2PDF.parameter;

public class PDFDocumentInfo {
    private String title;
    private String author;
    private String creator;
    private String producer;
    private String subject;

    public final String getTitle() {
        return title;
    }

    public final String getAuthor() {
        return author;
    }

    public final String getCreator() {
        return creator;
    }

    public final String getProducer() {
        return producer;
    }

    public final String getSubject() {
        return subject;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
