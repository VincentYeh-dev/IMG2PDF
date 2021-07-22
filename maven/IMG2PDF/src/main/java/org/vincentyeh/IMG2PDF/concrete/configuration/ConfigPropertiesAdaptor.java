package org.vincentyeh.IMG2PDF.concrete.configuration;

import org.vincentyeh.IMG2PDF.framework.configuration.Configuration;
import org.vincentyeh.IMG2PDF.concrete.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.WrongFileTypeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Properties;

class ConfigPropertiesAdaptor implements Configuration {
    private static final Locale[] supportedLocales = {
            Locale.TRADITIONAL_CHINESE
    };

    private final Properties properties;

    public ConfigPropertiesAdaptor(File file) throws IOException {
        if (file == null)
            throw new IllegalArgumentException("file==null");
        properties = new Properties();
        FileUtils.checkExists(file);
        FileUtils.checkType(file, WrongFileTypeException.Type.FILE);

        FileInputStream fis = new FileInputStream(file);
        properties.load(fis);
        fis.close();
    }

    public ConfigPropertiesAdaptor(Properties properties) {
        if (properties == null)
            throw new IllegalArgumentException("properties==null");
        this.properties = properties;
    }


    @Override
    public Charset getDirectoryListCharset() {
        String value = properties.getProperty("dirlist-read-charset");
        if (value == null) {
            throw new PropertiesNotFoundException("dirlist-read-charset");
        }

        return Charset.forName(value);
    }

    @Override
    public Locale getLocale() throws LanguageNotSupportException {
        String value = properties.getProperty("language");
        if (value == null) {
            throw new PropertiesNotFoundException("language");
        }

        Locale locale = Locale.forLanguageTag(value);
        checkLanguageSupport(locale);
        return locale;
    }

    private void checkLanguageSupport(Locale target) throws LanguageNotSupportException {
        boolean found = target.equals(Locale.ROOT);

        for (Locale locale : supportedLocales) {
            if (target.equals(locale)) {
                found = true;
                break;
            }
        }
        if (!found)
            throw new LanguageNotSupportException(target + " not supported");
    }

    public static class PropertiesNotFoundException extends RuntimeException {
        private final String property;

        public PropertiesNotFoundException(String property) {
            this.property = property;
        }

        public String getProperty() {
            return property;
        }
    }
}
