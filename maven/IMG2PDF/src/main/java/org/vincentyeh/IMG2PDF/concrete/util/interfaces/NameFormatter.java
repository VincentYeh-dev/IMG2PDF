package org.vincentyeh.IMG2PDF.concrete.util.interfaces;

/**
 * @param <D> Data type
 */
public abstract class NameFormatter<D> {
    protected final String pattern;

    public NameFormatter(String pattern) {
        this.pattern = pattern;
        if (pattern==null)
            throw new IllegalArgumentException("data==null");
    }

    public abstract String format(D data) throws FormatException;

    public static abstract class FormatException extends Exception{
        public FormatException() {
        }

        public FormatException(String message) {
            super(message);
        }

        public FormatException(String message, Throwable cause) {
            super(message, cause);
        }

        public FormatException(Throwable cause) {
            super(cause);
        }

        public FormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
