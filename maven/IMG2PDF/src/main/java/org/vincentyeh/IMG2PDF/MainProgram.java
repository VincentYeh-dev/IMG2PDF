package org.vincentyeh.IMG2PDF;

import org.fusesource.jansi.AnsiConsole;
import org.vincentyeh.IMG2PDF.concrete.commandline.MainCommandMaker;
import org.vincentyeh.IMG2PDF.framework.configuration.Configuration;
import org.vincentyeh.IMG2PDF.concrete.configuration.ConfigurationFactory;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;


public class MainProgram {

    public static void main(String[] args) {

        Configuration configuration = loadConfiguration("config.properties");

        int exitCode;
        try {
            AnsiConsole.systemInstall();

            CommandLine cmd = MainCommandMaker.make(configuration);

            exitCode = cmd.execute(args);

        } catch (Error e) {
//            ErrorHandler handler = new ErrorHandler(null,);
//            try {
//                PrinterUtils.printColor(handler.handle(e), Ansi.Color.RED);
//            } catch (Handler.CantHandleException cantHandleException) {
//                cantHandleException.printStackTrace();
//            }
            exitCode = 100;
        } finally {
            AnsiConsole.systemUninstall();
        }

        System.exit(exitCode);

    }

    private static Configuration loadConfiguration(String path) {
        try {
            return ConfigurationFactory.createValidOptionFromPropertiesFile(new File(path));
        } catch (IOException e) {
            return ConfigurationFactory.createDefault();
        }
    }
}