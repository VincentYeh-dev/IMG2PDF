package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.factory.exception.TaskBuilderException;
import org.vincentyeh.IMG2PDF.pdf.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;
import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.io.File;

public abstract class TaskBuilder<SOURCE> {

    protected abstract PageArgument getPageArgumentFromSource(SOURCE source) throws TaskBuilderException;

    protected abstract DocumentArgument getDocumentArgumentFromSource(SOURCE source) throws TaskBuilderException;

    protected abstract File getPdfDestinationFromSource(SOURCE source) throws TaskBuilderException;

    protected abstract File[] getImagesFromSource(SOURCE source) throws TaskBuilderException;

    public final Task build(SOURCE source) throws TaskBuilderException {
        PageArgument pageArgument = getPageArgumentFromSource(source);
        DocumentArgument documentArgument = getDocumentArgumentFromSource(source);
        File[] images = getImagesFromSource(source);
        File destination = getPdfDestinationFromSource(source);
        return new Task(documentArgument, pageArgument, images, destination);
    }

}
