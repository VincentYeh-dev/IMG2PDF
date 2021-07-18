package org.vincentyeh.IMG2PDF.task.framework.factory;


import org.vincentyeh.IMG2PDF.task.framework.DocumentArgument;
import org.vincentyeh.IMG2PDF.task.framework.PageArgument;
import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.io.File;

class TaskFactory {

    public final Task create(TaskFactoryBridge bridge) {
        return new Task() {
            @Override
            public DocumentArgument getDocumentArgument() {
                return bridge.generateDocumentArgument();
            }

            @Override
            public PageArgument getPageArgument() {
                return bridge.generatePageArgument();
            }

            @Override
            public File[] getImages() {
                return bridge.generateImages();
            }

            @Override
            public File getPdfDestination() {
                return bridge.generateDestination();
            }
        };
    }
}
