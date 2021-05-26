package org.vincentyeh.IMG2PDF.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.vincentyeh.IMG2PDF.pdf.doc.DocumentArgument;
import org.vincentyeh.IMG2PDF.pdf.page.PageArgument;

public class Task {
    private final DocumentArgument documentArgument;
    private final PageArgument pageArgument;

    private final File[] images;
    private final File destination;


    public Task(DocumentArgument documentArgument, PageArgument pageArgument, File[] images, File destination) {
        if (images == null)
            throw new IllegalArgumentException("images is null");
        this.images = images;
        this.destination = destination;
        this.documentArgument = documentArgument;
        this.pageArgument = pageArgument;
    }

    public Task(Element element) throws IllegalArgumentException {
        if (element == null)
            throw new NullPointerException("element is null");

        this.documentArgument = new DocumentArgument.Builder().buildFrom(element.getChild("DocumentArgument"));
        this.pageArgument = new PageArgument.Builder().buildFrom((element.getChild("PageArgument")));
        this.destination = new File(element.getChild("destination").getValue());
        List<Element> contains_files = element.getChild("files").getChildren("image");
        ArrayList<Element> xml_files = new ArrayList<>(contains_files);
        this.images = parseElementsToImages(xml_files);

    }

    public Element toElement() {
        Element task = new Element("task");
        Element xml_files = new Element("files");
        for (File img : images) {
            Element xml_file = new Element("image");
            xml_file.addContent(img.getAbsolutePath());
            xml_files.addContent(xml_file);
        }
        task.addContent(xml_files);
        Element elm_destination = new Element("destination");
        elm_destination.addContent(destination.getAbsolutePath());

        task.addContent(elm_destination);
        task.addContent(documentArgument.toElement());
        task.addContent(pageArgument.toElement());

        return task;
    }

    private File[] parseElementsToImages(ArrayList<Element> elements) {

        return elements.stream().map(element -> new File(element.getValue())).toArray(File[]::new);

    }


    public File[] getImages() {
        return this.images;
    }

    public File getDestination() {
        return destination;
    }

    public DocumentArgument getDocumentArgument() {
        return documentArgument;
    }

    public PageArgument getPageArgument() {
        return pageArgument;
    }


}
