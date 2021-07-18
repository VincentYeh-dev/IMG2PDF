package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.DocumentArgument;
import org.vincentyeh.IMG2PDF.task.framework.PageArgument;

import java.io.File;

public interface TaskFactoryBridge {
    PageArgument generatePageArgument();

    DocumentArgument generateDocumentArgument();

    File[] generateImages();

    File generateDestination();
}
