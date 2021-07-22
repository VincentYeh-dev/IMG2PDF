package org.vincentyeh.IMG2PDF.concrete.configuration;

import org.vincentyeh.IMG2PDF.framework.configuration.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class ConfigurationFactory {
    private static Locale[] supporteds ={
      Locale.TRADITIONAL_CHINESE
    };

    public static Configuration createFromPropertiesFile(File file) throws IOException {
        if (file == null)
            throw new IllegalArgumentException("file==null");
        return new ConfigPropertiesAdaptor(file);
    }

    public static Configuration createDefault() {
        return new Configuration() {
            @Override
            public Charset getDirectoryListCharset() {
                return StandardCharsets.UTF_8;
            }

            @Override
            public Locale getLocale() {
                return languageSupport(Locale.getDefault())?Locale.getDefault():Locale.ROOT;
            }
        };
    }

    public static Configuration createValidOptionFromPropertiesFile(File file) throws IOException {
        if (file == null)
            throw new IllegalArgumentException("file==null");

        Configuration configuration = createFromPropertiesFile(file);
        Configuration default_configuration = createDefault();

        Charset directoryListCharset;
        Locale locale;
        try {
            directoryListCharset = configuration.getDirectoryListCharset();
        } catch (Exception e) {
            directoryListCharset = default_configuration.getDirectoryListCharset();
        }

        try {
            locale = configuration.getLocale();
        } catch (Exception e) {

            if(languageSupport(Locale.getDefault()))
                locale = Locale.getDefault();
            else
                locale=default_configuration.getLocale();
        }

        Charset finalDirectoryListCharset = directoryListCharset;
        Locale finalLocale = locale;
        return new Configuration() {
            @Override
            public Charset getDirectoryListCharset() {
                return finalDirectoryListCharset;
            }

            @Override
            public Locale getLocale() {
                return finalLocale;
            }
        };

    }

    private static boolean languageSupport(Locale locale){
        for(Locale supported: supporteds){
            if (supported.equals(locale))
                return true;
        }
        return false;
    }


}
