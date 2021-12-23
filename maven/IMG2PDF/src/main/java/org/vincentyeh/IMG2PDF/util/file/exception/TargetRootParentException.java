package org.vincentyeh.IMG2PDF.util.file.exception;

import java.io.File;

public class TargetRootParentException extends NoParentException {
    public TargetRootParentException(File file) {
        super(file);
    }
}
