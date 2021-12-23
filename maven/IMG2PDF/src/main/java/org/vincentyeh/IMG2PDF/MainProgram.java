package org.vincentyeh.IMG2PDF;

import org.fusesource.jansi.AnsiConsole;
import org.vincentyeh.IMG2PDF.concrete.commandline.MainCommandMaker;
import org.vincentyeh.IMG2PDF.concrete.configuration.ConfigurationPropertiesParser;
import org.vincentyeh.IMG2PDF.concrete.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.FileNotExistsException;
import org.vincentyeh.IMG2PDF.framework.configuration.ConfigurationParser;
import picocli.CommandLine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;


public class MainProgram {

    public static void main(String[] args) {

        Map<ConfigurationParser.ConfigParam, Object> configuration = null;
        try {
            configuration = loadConfiguration("config.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private static Map<ConfigurationParser.ConfigParam, Object> loadConfiguration(String path) throws IOException {
        Properties properties = new Properties();
        File file = new File(path);
        try {
            FileUtils.checkExists(file);
            properties.load(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        } catch (FileNotExistsException e) {
            properties.put("dirlist-read-charset", "UTF-8");
            properties.put("language", Locale.ROOT.toLanguageTag());
            properties.store(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8), "configuration file");
        }
        return ((ConfigurationParser) new ConfigurationPropertiesParser()).parse(properties);
    }
}