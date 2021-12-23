package org.vincentyeh.IMG2PDF.commandline.command;

import picocli.CommandLine;

@CommandLine.Command(name = "img2pdf", mixinStandardHelpOptions = true,
        version = {
        "IMG2PDF v1.0.0",
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
