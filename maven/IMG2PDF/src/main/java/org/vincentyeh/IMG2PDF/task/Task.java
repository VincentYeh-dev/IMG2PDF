package org.vincentyeh.IMG2PDF.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.jdom2.Element;
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

    public Task(Element element) throws IllegalArgumentException {
        if (element == null)
            throw new NullPointerException("element is null");

        this.documentArgument = new DocumentArgument.Builder().buildFrom(element.getChild("DocumentArgument"));
        this.pageArgument = new PageArgument.Builder().buildFrom((element.getChild("PageArgument")));
        this.pdf_destination = new File(element.getChild("destination").getValue());
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
        elm_destination.addContent(pdf_destination.getPath());

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
