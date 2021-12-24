package org.vincentyeh.IMG2PDF.commandline;

import org.vincentyeh.IMG2PDF.commandline.command.IMG2PDFCommand;
import org.vincentyeh.IMG2PDF.commandline.command.ConvertCommand;
import org.vincentyeh.IMG2PDF.handler.ExceptionHandlerFacade;
import org.vincentyeh.IMG2PDF.configuration.framework.ConfigurationParser;
import picocli.CommandLine;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MainCommandMaker {
    public static CommandLine make(Map<ConfigurationParser.ConfigParam, Object> config) {
        ExceptionHandlerFacade.setResourceBundle(ResourceBundle.getBundle("handler", (Locale) config.get(ConfigurationParser.ConfigParam.LOCALE)));

        final CommandLine cmd = new CommandLine(new IMG2PDFCommand());
        cmd.setResourceBundle(ResourceBundle.getBundle("cmd",(Locale) config.get(ConfigurationParser.ConfigParam.LOCALE) ));

        cmd.setParameterExceptionHandler(new CommandLineParameterHandlerAdaptor());

        cmd.addSubcommand(new ConvertCommand(config));
        return cmd;
    }

}
