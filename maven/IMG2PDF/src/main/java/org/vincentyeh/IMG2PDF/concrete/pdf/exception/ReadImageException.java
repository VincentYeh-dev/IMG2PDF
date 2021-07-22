package org.vincentyeh.IMG2PDF.concrete.pdf.exception;

import java.io.File;

public class ReadImageException extends RuntimeException{
    private final File image_file;
    public ReadImageException(Throwable cause, File image_file) {
        super(cause);
        this.image_file = image_file;
    }

    public File getErrorImageFile() {
        return image_file;
    }
}
