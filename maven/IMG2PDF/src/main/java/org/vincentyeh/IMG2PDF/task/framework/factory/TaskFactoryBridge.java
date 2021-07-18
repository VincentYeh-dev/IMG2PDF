package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.DocumentArgument;
import org.vincentyeh.IMG2PDF.task.framework.PageArgument;

import java.io.File;

public abstract class TaskFactoryBridge {
    public abstract PageArgument generatePageArgument();
    public abstract DocumentArgument generateDocumentArgument();
    public abstract File[] generateImages();
    public abstract File generateDestination();
}
