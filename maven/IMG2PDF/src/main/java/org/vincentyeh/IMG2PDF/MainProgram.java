package org.vincentyeh.IMG2PDF;

import org.vincentyeh.IMG2PDF.commandline.command.IMG2PDFCommand;
import org.vincentyeh.IMG2PDF.commandline.handler.ResourceBundleExecutionHandler;
import org.vincentyeh.IMG2PDF.commandline.handler.ResourceBundleParameterHandler;
import picocli.CommandLine;

import java.util.Locale;
import java.util.ResourceBundle;


public class MainProgram {

    public static void main(String[] args) {
        SharedSpace.initialize();
        Locale locale = SharedSpace.Configuration.locale;
        CommandLine cmd = new CommandLine(new IMG2PDFCommand());

        cmd.setExecutionExceptionHandler(new ResourceBundleExecutionHandler(ResourceBundle.getBundle("cmd_err", locale)));
        cmd.setParameterExceptionHandler(new ResourceBundleParameterHandler(ResourceBundle.getBundle("cmd_err", locale)));
        cmd.setResourceBundle(ResourceBundle.getBundle("cmd", locale));

        System.exit(cmd.execute(args));
    }
}
