package org.vincentyeh.IMG2PDF.pdf.parameter;

public final class PDFDocumentInfo {
    private String title;
    private String author;
    private String creator;
    private String producer;
    private String subject;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreator() {
        return creator;
    }

    public String getProducer() {
        return producer;
    }

    public String getSubject() {
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
