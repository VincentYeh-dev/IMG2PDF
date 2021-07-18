package org.vincentyeh.IMG2PDF.task.concrete.factory;

import org.vincentyeh.IMG2PDF.task.framework.DocumentArgument;
import org.vincentyeh.IMG2PDF.task.framework.PageArgument;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskFactoryBridge;

import java.io.File;

public class StandardTaskFactoryBridge implements TaskFactoryBridge {

    private final PageArgument pageArgument;
    private final DocumentArgument documentArgument;
    private final File[] images;
    private final File destination;

    public StandardTaskFactoryBridge(PageArgument argument, DocumentArgument argument1, File[] images, File destination) {
        pageArgument = argument;
        documentArgument = argument1;
        this.images = images;
        this.destination = destination;

    }


    @Override
    public PageArgument generatePageArgument() {
        return pageArgument;
    }

    @Override
    public DocumentArgument generateDocumentArgument() {
        return documentArgument;
    }

    @Override
    public File[] generateImages() {
        return images;
    }

    @Override
    public File generateDestination() {
        return destination;
    }
}
