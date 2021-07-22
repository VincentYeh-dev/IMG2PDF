package org.vincentyeh.IMG2PDF.framework.configuration;

import java.nio.charset.Charset;
import java.util.Locale;

public interface Configuration {
    Charset getDirectoryListCharset();
    Locale getLocale();

    class LanguageNotSupportException extends RuntimeException{
        public LanguageNotSupportException(String message) {
            super(message);
        }
    }
}
