package org.vincentyeh.IMG2PDF.concrete.commandline;

import org.vincentyeh.IMG2PDF.concrete.commandline.command.ConvertCommand;
import org.vincentyeh.IMG2PDF.concrete.commandline.command.IMG2PDFCommand;
import org.vincentyeh.IMG2PDF.concrete.handler.ExceptionHandlerFactory;
import org.vincentyeh.IMG2PDF.framework.configuration.ConfigurationParser;
import picocli.CommandLine;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MainCommandMaker {
    public static CommandLine make(Map<ConfigurationParser.ConfigParam, Object> config) {
        ExceptionHandlerFactory.setResourceBundle(ResourceBundle.getBundle("handler", (Locale) config.get(ConfigurationParser.ConfigParam.LOCALE)));

        final CommandLine cmd = new CommandLine(new IMG2PDFCommand());
        cmd.setResourceBundle(ResourceBundle.getBundle("cmd",(Locale) config.get(ConfigurationParser.ConfigParam.LOCALE) ));

        cmd.setParameterExceptionHandler(new CommandLineParameterHandlerAdaptor());

        cmd.addSubcommand(new ConvertCommand(config));
        return cmd;
    }

}
