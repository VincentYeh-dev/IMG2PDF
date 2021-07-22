package org.vincentyeh.IMG2PDF.framework.task;

import org.vincentyeh.IMG2PDF.framework.parameter.DocumentArgument;
import org.vincentyeh.IMG2PDF.framework.parameter.PageArgument;

import java.io.File;

public interface Task {

    DocumentArgument getDocumentArgument();

    PageArgument getPageArgument();

    File[] getImages();

    File getPdfDestination();

}
