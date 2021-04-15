package org.vincentyeh.IMG2PDF.commandline.action;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.vincentyeh.IMG2PDF.SharedSpace;
import org.vincentyeh.IMG2PDF.commandline.action.exception.HelperException;
import org.vincentyeh.IMG2PDF.commandline.parser.HandledException;
import org.vincentyeh.IMG2PDF.commandline.parser.PropertiesOption;
import org.vincentyeh.IMG2PDF.commandline.parser.RelaxedParser;

public class MainProgram extends AbstractAction {

    private static final Option opt_help;

    static {
        opt_help = PropertiesOption.getOption("h", "help", "main.help");
    }

    private final AbstractAction action;

    public MainProgram(String[] args) throws ParseException, HandledException {
        super(getLocaleOptions());

        System.out.println("##IMG2PDF##");
        System.out.printf("%s: %s", SharedSpace.getResString("public.info.developer"), SharedSpace.Configuration.DEVELOPER);
        System.out.printf("\n%s: %s\n", SharedSpace.getResString("public.info.version"), SharedSpace.Configuration.PROGRAM_VER);
        System.out.println("-----------------------");

        CommandLine mode_chooser = (new RelaxedParser()).parse(options, args);

        if (args.length == 0 || mode_chooser.hasOption("help") && !mode_chooser.hasOption("mode")) {
            throw new HelperException(options);
        }

        ActionMode mode;
        if (mode_chooser.hasOption("mode")) {
            mode = ActionMode.getByString(mode_chooser.getOptionValue("mode"));

        } else {
            throw new HelperException(options);
        }

        switch (mode) {
            case create:
                action = new CreateAction(args);
                break;
            case convert:
                action = new ConvertAction(args);
                break;
            default:
                throw new RuntimeException("mode==??");
        }
    }

    @Override
    public void start() throws Exception {
        action.start();
    }

    private static Options getLocaleOptions() {

        Options options = new Options();
        Option opt_mode = PropertiesOption.getArgumentOption("m", "mode", "main.arg.mode.help", listEnum(ActionMode.class));
        options.addOption(opt_mode);
        options.addOption(opt_help);

        return options;
    }

    public static void main(String[] args) {
        MainProgram main;
        try {
            main = new MainProgram(args);
        } catch (HelperException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(SharedSpace.Configuration.PROGRAM_NAME, e.opt);
            return;
        } catch (MissingOptionException e) {
            System.err.println(createMissingOptionsMessage(e.getMissingOptions()));
            return;
        } catch (MissingArgumentException e) {
            System.err.println(createMissingArgumentOptionsMessage(e.getOption()));
            return;
        } catch (UnrecognizedOptionException e) {
            System.err.println(createUnrecognizedOptionMessage(e.getOption()));
            System.err.println(e.getMessage());
            return;
        } catch (HandledException e) {
            System.err.println(e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            main.start();
        } catch (HandledException e) {
//            do nothing
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static <T> void dumpArray(T[] array) {
//        System.out.print("[");
//        System.out.print(array[0]);
//        for (int i = 1; i < array.length; i++) {
//            System.out.print(",");
//            System.out.print(array[i]);
//        }
//        System.out.print("]\n");
//    }

}
