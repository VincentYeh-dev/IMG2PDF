package org.vincentyeh.IMG2PDF.concrete.commandline;

import org.vincentyeh.IMG2PDF.concrete.commandline.command.ConvertCommand;
import org.vincentyeh.IMG2PDF.concrete.commandline.command.IMG2PDFCommand;
import org.vincentyeh.IMG2PDF.concrete.configuration.ConvertConfigurationAdaptor;
import org.vincentyeh.IMG2PDF.concrete.handler.ExceptionHandlerFactory;
import org.vincentyeh.IMG2PDF.framework.configuration.Configuration;
import picocli.CommandLine;

import java.util.ResourceBundle;

public class MainCommandMaker {
    public static CommandLine make(Configuration configuration) {
        ExceptionHandlerFactory.setResourceBundle(ResourceBundle.getBundle("handler", configuration.getLocale()));

        final CommandLine cmd = new CommandLine(new IMG2PDFCommand());
        cmd.setResourceBundle(ResourceBundle.getBundle("cmd", configuration.getLocale()));

        cmd.setParameterExceptionHandler(new CommandLineParameterHandlerAdaptor());

        cmd.addSubcommand(new ConvertCommand(getConvertConfiguration(configuration)));
        return cmd;
    }

    private static ConvertCommand.Configuration getConvertConfiguration(Configuration configuration) {
        return new ConvertConfigurationAdaptor(configuration);
    }
}
