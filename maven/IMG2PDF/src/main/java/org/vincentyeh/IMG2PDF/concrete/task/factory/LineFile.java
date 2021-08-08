package org.vincentyeh.IMG2PDF.concrete.task.factory;

import java.io.File;

class LineFile{
    private final int line;
    private final File file;

    public LineFile(int line, File file) {
        this.line = line;
        this.file = file;
    }

    public int getLine() {
        return line;
    }

    public File getFile() {
        return file;
    }
}
