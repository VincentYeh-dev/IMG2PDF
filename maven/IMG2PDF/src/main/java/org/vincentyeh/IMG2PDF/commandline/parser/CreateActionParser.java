package org.vincentyeh.IMG2PDF.commandline.parser;

import org.apache.commons.cli.*;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.vincentyeh.IMG2PDF.SharedSpace;
import org.vincentyeh.IMG2PDF.commandline.action.CreateAction;
import org.vincentyeh.IMG2PDF.commandline.parser.core.CheckHelpParser;
import org.vincentyeh.IMG2PDF.commandline.parser.core.HandledException;
import org.vincentyeh.IMG2PDF.commandline.option.MultiLanguageOptionFactory;
import org.vincentyeh.IMG2PDF.task.DocumentArgument;
import org.vincentyeh.IMG2PDF.pdf.page.PageAlign;
import org.vincentyeh.IMG2PDF.task.PageArgument;
import org.vincentyeh.IMG2PDF.pdf.page.PageDirection;
import org.vincentyeh.IMG2PDF.pdf.page.PageSize;
import org.vincentyeh.IMG2PDF.util.file.FileFilterHelper;
import org.vincentyeh.IMG2PDF.util.file.FileSorter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CreateActionParser extends ActionParser<CreateAction> {

    private static final String DEFAULT_PDF_SIZE = "A4";
    private static final String DEFAULT_PDF_ALIGN = "CENTER-CENTER";
    private static final String DEFAULT_PDF_DIRECTION = "Portrait";
    private static final String DEFAULT_PDF_SORTBY = "NAME";
    private static final String DEFAULT_PDF_SEQUENCE = "INCREASE";
    private static final String DEFAULT_PDF_FILTER = "glob:*.{PNG,JPG}";

    private final CommandLineParser parser;

    @Override
    public CreateAction parse(String[] arguments) throws ParseException, HandledException {
        CommandLine cmd = parser.parse(options, arguments);

        CreateAction.Builder builder = new CreateAction.Builder();
        builder.setDebug(cmd.hasOption("debug"));
        builder.setOverwrite(cmd.hasOption("overwrite"));
        builder.setPdfDestination(cmd.getOptionValue("pdf_destination"));
        builder.setTasklistDestination(getTaskListDestination(cmd));
        builder.setDocumentArgument(getDocumentArgument(cmd));
        builder.setPageArgument(getPageArgument(cmd));
        builder.setFileFilterHelper(getFileFilterHelper(cmd));
        builder.setSourceFiles(getDirlistSources(cmd));
        builder.setFileSorter(new FileSorter(getSortby(cmd), getSequence(cmd)));

        return builder.build();

    }

    private File[] getDirlistSources(CommandLine cmd) throws HandledException {
        try {
            return verifyFiles(cmd.getOptionValues("source"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new HandledException(e, getClass());
        }
    }

    private FileFilterHelper getFileFilterHelper(CommandLine cmd) throws HandledException {
        try {
            return new FileFilterHelper(cmd.getOptionValue("filter", DEFAULT_PDF_FILTER));

        } catch (UnsupportedOperationException e) {
            System.err.printf(SharedSpace.getResString("create.err.filter") + "\n", e.getMessage());
            throw new HandledException(e, getClass());
        }
    }

    private File getTaskListDestination(CommandLine cmd) {
        String list_destination = cmd.getOptionValue("list_destination");
        return (new File(list_destination)).getAbsoluteFile();
    }

    private DocumentArgument getDocumentArgument(CommandLine cmd) {
        DocumentArgument.Builder builder=new DocumentArgument.Builder();
        builder.setOwnerPassword(cmd.getOptionValue("pdf_owner_password"));
        builder.setUserPassword(cmd.getOptionValue("pdf_user_password"));
        builder.setAccessPermission(getDocumentAccessPermission(cmd.getOptionValue("pdf_permission", "11")));
        return builder.build();
    }

    private AccessPermission getDocumentAccessPermission(String symbol) {
        AccessPermission ap = new AccessPermission();

        if (symbol == null || symbol.isEmpty())
            throw new IllegalArgumentException("str is null or empty");
        if (symbol.length() != 2)
            throw new IllegalArgumentException("str invalid");

        char[] permissions = symbol.toCharArray();
        ap.setCanPrint(permissions[0] != '0');
        ap.setCanModify(permissions[1] != '0');

        return ap;
    }

    private PageArgument getPageArgument(CommandLine cmd) throws HandledException {
        PageArgument.Builder builder = new PageArgument.Builder();
        builder.setAlign(getValueOfAlign(cmd.getOptionValue("pdf_align", DEFAULT_PDF_ALIGN)));
        builder.setSize(getValueOfSize(cmd.getOptionValue("pdf_size", DEFAULT_PDF_SIZE)));
        builder.setDirection(getValueOfDirection(cmd.getOptionValue("pdf_direction", DEFAULT_PDF_DIRECTION)));

        builder.setAutoRotate(cmd.hasOption("pdf_auto_rotate"));
        return builder.build();
    }

    private PageDirection getValueOfDirection(String value) throws HandledException {
        return getValueOfEnum(PageDirection.class, value);
    }

    private PageSize getValueOfSize(String value) throws HandledException {
        return getValueOfEnum(PageSize.class, value);
    }


    private PageAlign getValueOfAlign(String value) throws HandledException {
        try {
            return new PageAlign(value);
        } catch (IllegalArgumentException e) {
//            TODO:check
            System.err.printf(SharedSpace.getResString("public.err.unrecognizable_enum_long") + "\n", value, PageAlign.class.getSimpleName(), Arrays.toString(new String[]{""}));
            throw new HandledException(e, getClass());
        }
    }


    private FileSorter.Sequence getSequence(CommandLine cmd) throws HandledException {
        return getValueOfEnum(FileSorter.Sequence.class, cmd.getOptionValue("pdf_sequence", DEFAULT_PDF_SEQUENCE));
    }

    private FileSorter.Sortby getSortby(CommandLine cmd) throws HandledException {
        return getValueOfEnum(FileSorter.Sortby.class, cmd.getOptionValue("pdf_sortby", DEFAULT_PDF_SORTBY));
    }


    public CreateActionParser() {
        Option opt_help = MultiLanguageOptionFactory.getOption("h", "help", "create.help");

        Option opt_debug = MultiLanguageOptionFactory.getOption("d", "debug", "create.arg.debug.help");
        Option opt_overwrite = MultiLanguageOptionFactory.getOption("ow", "overwrite", "create.arg.overwrite_tasklist.help");

        Option opt_pdf_size = MultiLanguageOptionFactory.getArgumentOption("pz", "pdf_size", "create.arg.pdf_size.help", listEnum(PageSize.class));
        Option opt_pdf_align = MultiLanguageOptionFactory.getArgumentOption("pa", "pdf_align", "create.arg.pdf_align.help");
        Option opt_pdf_direction = MultiLanguageOptionFactory.getArgumentOption("pdi", "pdf_direction", "create.arg.pdf_direction.help",
                listEnum(PageDirection.class));

        Option opt_pdf_auto_rotate = MultiLanguageOptionFactory.getOption("par", "pdf_auto_rotate", "create.arg.pdf_auto_rotate.help");
        Option opt_pdf_sortby = MultiLanguageOptionFactory.getArgumentOption("ps", "pdf_sortby", "create.arg.pdf_sortby.help", listEnum(FileSorter.Sortby.class));
        Option opt_pdf_sequence = MultiLanguageOptionFactory.getArgumentOption("pseq", "pdf_sequence", "create.arg.pdf_sequence.help", listEnum(FileSorter.Sequence.class));

        Option opt_pdf_owner_password = MultiLanguageOptionFactory.getArgumentOption("popwd", "pdf_owner_password",
                "create.arg.pdf_owner_password.help");
        Option opt_pdf_user_password = MultiLanguageOptionFactory.getArgumentOption("pupwd", "pdf_user_password", "create.arg.pdf_user_password.help");

        Option opt_pdf_permission = MultiLanguageOptionFactory.getArgumentOption("pp", "pdf_permission", "create.arg.pdf_permission.help");

        Option opt_pdf_destination = MultiLanguageOptionFactory.getArgumentOption("pdst", "pdf_destination", "create.arg.pdf_destination.help");
        opt_pdf_destination.setRequired(true);

        Option opt_filter = MultiLanguageOptionFactory.getArgumentOption("f", "filter", "create.arg.filter.help");

        Option opt_sources = MultiLanguageOptionFactory.getArgumentOption("src", "source", "create.arg.source.help");
        opt_sources.setRequired(true);

        Option opt_list_destination = MultiLanguageOptionFactory.getArgumentOption("ldst", "list_destination", "create.arg.list_destination.help");
        opt_list_destination.setRequired(true);

        options.addOption(opt_debug);
        options.addOption(opt_overwrite);
        options.addOption(opt_help);
        options.addOption(opt_pdf_size);
        options.addOption(opt_pdf_align);
        options.addOption(opt_pdf_direction);
        options.addOption(opt_pdf_auto_rotate);
        options.addOption(opt_pdf_sortby);
        options.addOption(opt_pdf_sequence);
        options.addOption(opt_pdf_owner_password);
        options.addOption(opt_pdf_user_password);
        options.addOption(opt_pdf_permission);
        options.addOption(opt_pdf_destination);
        options.addOption(opt_filter);
        options.addOption(opt_sources);
        options.addOption(opt_list_destination);

        parser = new CheckHelpParser(opt_help);
    }
}