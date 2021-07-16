package org.vincentyeh.IMG2PDF.task.framework;

import java.io.File;

public interface Task {

    DocumentArgument getDocumentArgument();

    PageArgument getPageArgument();

    File[] getImages();

    File getPdfDestination();

}
