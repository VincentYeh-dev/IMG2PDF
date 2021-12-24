package org.vincentyeh.IMG2PDF.pdf.framework.converter.exception;

import java.io.File;
import java.io.IOException;

public class ReadImageException extends IOException {
    private final File image_file;
    public ReadImageException(Throwable cause, File image_file) {
        super(cause);
        this.image_file = image_file;
    }

    public File getErrorImageFile() {
        return image_file;
    }
}
