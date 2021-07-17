package org.vincentyeh.IMG2PDF;

import org.vincentyeh.IMG2PDF.commandline.command.ConvertCommand;
import org.vincentyeh.IMG2PDF.commandline.command.IMG2PDFCommand;
import org.vincentyeh.IMG2PDF.commandline.handler.CommandLineParameterHandlerAdaptor;
import org.vincentyeh.IMG2PDF.commandline.handler.concrete.ExceptionHandlerFactory;
import org.vincentyeh.IMG2PDF.configuration.Configuration;
import org.vincentyeh.IMG2PDF.configuration.ConvertConfigurationAdaptor;
import picocli.CommandLine;

import java.util.ResourceBundle;

class MainCommandMaker {
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
