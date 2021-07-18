package org.vincentyeh.IMG2PDF.task.concrete.factory.argument;

import org.vincentyeh.IMG2PDF.task.framework.factory.CreateArgument;

import java.io.File;

public class TextLineCreateArgument implements CreateArgument {
    private final File source;
    private final int line;

    public TextLineCreateArgument(File source, int line) {
        this.source = source;
        this.line = line;
    }

    public File getSource() {
        return source;
    }

    public int getLine() {
        return line;
    }
}
