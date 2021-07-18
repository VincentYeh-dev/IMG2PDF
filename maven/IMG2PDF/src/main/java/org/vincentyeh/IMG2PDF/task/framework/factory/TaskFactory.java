package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.DocumentArgument;
import org.vincentyeh.IMG2PDF.task.framework.PageArgument;
import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.io.File;

public abstract class TaskFactory {
    protected abstract PageArgument generatePageArgument();

    protected abstract DocumentArgument generateDocumentArgument();

    protected abstract File[] generateImages();

    protected abstract File generateDestination();

    public final Task create(){
        return new Task() {
            @Override
            public DocumentArgument getDocumentArgument() {
                return generateDocumentArgument();
            }

            @Override
            public PageArgument getPageArgument() {
                return generatePageArgument();
            }

            @Override
            public File[] getImages() {
                return generateImages();
            }

            @Override
            public File getPdfDestination() {
                return generateDestination();
            }
        };
    }


}
