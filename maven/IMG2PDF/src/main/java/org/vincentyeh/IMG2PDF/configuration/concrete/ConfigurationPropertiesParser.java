package org.vincentyeh.IMG2PDF.configuration.concrete;

import org.vincentyeh.IMG2PDF.configuration.framework.ConfigurationParser;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class ConfigurationPropertiesParser implements ConfigurationParser {

    private static final Locale[] supportedLocales = {
            Locale.TRADITIONAL_CHINESE,
            Locale.US
    };

    @Override
    public <T> Map<ConfigParam, Object> parse(T properties) {
        if (properties == null)
            throw new IllegalArgumentException("properties==null");

        Map<ConfigParam, Object> map = new HashMap<>();
        map.put(ConfigParam.DIR_LIST_READ_CHARSET, getDirectoryListCharset((Properties) properties));
        map.put(ConfigParam.LOCALE, getLocale((Properties) properties));
        return map;
    }

    public Charset getDirectoryListCharset(Properties properties) {
        String value = properties.getProperty("dirlist-read-charset");
        if (value == null) {
            throw new PropertiesNotFoundException("dirlist-read-charset");
        }

        return Charset.forName(value);
    }

    public Locale getLocale(Properties properties) throws LanguageNotSupportException {
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

    static class LanguageNotSupportException extends RuntimeException {
        public LanguageNotSupportException(String message) {
            super(message);
        }
    }
}
