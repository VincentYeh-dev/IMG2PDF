package org.vincentyeh.IMG2PDF.pdf.framework.converter.exception;

import java.io.File;
import java.io.IOException;

public class SaveException extends IOException {
    private final File save_file;

    public SaveException(Throwable cause, File save_file) {
        super(cause);
        this.save_file = save_file;
    }

    public File getSaveFile() {
        return save_file;
    }
}
