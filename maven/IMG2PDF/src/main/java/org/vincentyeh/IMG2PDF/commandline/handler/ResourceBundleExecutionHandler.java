package org.vincentyeh.IMG2PDF.commandline.handler;

import org.vincentyeh.IMG2PDF.commandline.handler.execution.ConvertHandler;
import org.vincentyeh.IMG2PDF.commandline.handler.execution.CreateHandler;
import org.vincentyeh.IMG2PDF.commandline.handler.execution.ResourceBundleHandler;
import org.vincentyeh.IMG2PDF.pattern.Handler;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.util.ResourceBundle;

public class ResourceBundleExecutionHandler implements CommandLine.IExecutionExceptionHandler {

    public ResourceBundleExecutionHandler(ResourceBundle resourceBundle) {
        ResourceBundleHandler.setResourceBundle(resourceBundle);
    }

    @Override
    public int handleExecutionException(Exception ex, CommandLine cmd, CommandLine.ParseResult parseResult) {
        Handler<String,HandleCondition> handler=getHandler();

        String msg;
        try {
            msg = handler.handle(new HandleCondition(cmd.getCommand().getClass(), ex));
        }catch (Handler.CantHandleException e){
            printErrorText(cmd,"Can't handle");
            msg=ex.getMessage();
            ex.printStackTrace();
        }

        printErrorText(cmd, msg);
        return CommandLine.ExitCode.SOFTWARE;
    }

    private Handler<String, HandleCondition> getHandler() {
        return new CreateHandler(new ConvertHandler(null));
    }


    private void printErrorText(CommandLine cmd, String message) {
        PrintWriter printer = cmd.getErr();
        printer.println(cmd.getColorScheme().errorText(message)); // bold red
    }

    private void printText(CommandLine cmd, String message) {
        PrintWriter printer = cmd.getErr();
        printer.println(message); // bold red
    }


    public static class HandleCondition {
        private final Class<?> clazz;
        private final Exception exception;

        private HandleCondition(Class<?> clazz, Exception exception) {
            this.clazz = clazz;
            this.exception = exception;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public Exception getException() {
            return exception;
        }
    }

}
