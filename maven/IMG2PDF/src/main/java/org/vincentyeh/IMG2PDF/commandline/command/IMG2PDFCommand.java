package org.vincentyeh.IMG2PDF.commandline.command;

import picocli.CommandLine;

import java.util.Locale;

@CommandLine.Command(name = "img2pdf", mixinStandardHelpOptions = true,
        version = {
        "IMG2PDF-0.0.1-SNAPSHOT",
        "Picocli " + picocli.CommandLine.VERSION,
        "JVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
        "OS: ${os.name} ${os.version} ${os.arch}"}
        )

public class IMG2PDFCommand {

    @CommandLine.Option(names = {"--debug","-d"})
    private boolean debug;

    public boolean isDebug() {
        return debug;
    }
}
