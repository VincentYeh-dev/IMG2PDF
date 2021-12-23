package org.vincentyeh.IMG2PDF.task.framework;

import org.vincentyeh.IMG2PDF.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.parameter.PageArgument;

import java.io.File;

public interface Task {

    DocumentArgument getDocumentArgument();

    PageArgument getPageArgument();

    File[] getImages();

    File getPdfDestination();

}
