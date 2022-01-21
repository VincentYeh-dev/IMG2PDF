package org.vincentyeh.IMG2PDF.task.framework;

import org.vincentyeh.IMG2PDF.pdf.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.pdf.parameter.PageArgument;

import java.io.File;

public interface Task {

    DocumentArgument getDocumentArgument();

    PageArgument getPageArgument();

    File[] getImages();

    File getPdfDestination();

}
