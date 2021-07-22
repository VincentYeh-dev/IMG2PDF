package org.vincentyeh.IMG2PDF.concrete.configuration;

import org.vincentyeh.IMG2PDF.concrete.commandline.command.ConvertCommand;
import org.vincentyeh.IMG2PDF.framework.configuration.Configuration;

import java.nio.charset.Charset;
import java.util.Locale;

public class ConvertConfigurationAdaptor implements ConvertCommand.Configuration {

    private final Configuration configuration;
    public ConvertConfigurationAdaptor(Configuration configuration) {
        this.configuration=configuration;
    }

    @Override
    public Charset getDirectoryListCharset() {
        return configuration.getDirectoryListCharset();
    }

    @Override
    public Locale getLocale() {
        return configuration.getLocale();
    }


}
