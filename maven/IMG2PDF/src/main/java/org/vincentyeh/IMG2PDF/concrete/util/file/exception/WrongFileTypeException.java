package org.vincentyeh.IMG2PDF.concrete.util.file.exception;

import java.io.File;

public class WrongFileTypeException extends FileException{

    public enum Type {
        FOLDER, FILE
    }

    private final Type expected;
    private final Type value;

    public Type getExpected() {
        return expected;
    }

    public Type getValue() {
        return value;
    }

    public WrongFileTypeException(Type expected, Type value, File file) {
        super(value + "!=" + expected + "(expected)",file);
        this.expected = expected;
        this.value = value;
    }
}
