package org.vincentyeh.IMG2PDF.commandline.action;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.vincentyeh.IMG2PDF.commandline.action.exception.HelperException;
import org.vincentyeh.IMG2PDF.commandline.parser.CheckHelpParser;
import org.vincentyeh.IMG2PDF.SharedSpace;
import org.vincentyeh.IMG2PDF.commandline.action.exception.UnrecognizedEnumException;
import org.vincentyeh.IMG2PDF.commandline.parser.HandledException;
import org.vincentyeh.IMG2PDF.commandline.parser.PropertiesOption;
import org.vincentyeh.IMG2PDF.pdf.doc.DocumentArgument;
import org.vincentyeh.IMG2PDF.pdf.page.PageArgument;
import org.vincentyeh.IMG2PDF.util.FileFilterHelper;
import org.vincentyeh.IMG2PDF.pdf.doc.DocumentAccessPermission;
import org.vincentyeh.IMG2PDF.pdf.page.PageAlign;
import org.vincentyeh.IMG2PDF.pdf.page.PageDirection;
import org.vincentyeh.IMG2PDF.pdf.page.PageSize;
import org.vincentyeh.IMG2PDF.task.Task;
import org.vincentyeh.IMG2PDF.task.TaskList;
import org.vincentyeh.IMG2PDF.util.FileSorter;
import org.vincentyeh.IMG2PDF.util.FileChecker;
import org.vincentyeh.IMG2PDF.util.NameFormatter;

import static org.vincentyeh.IMG2PDF.util.FileSorter.Sequence;
import static org.vincentyeh.IMG2PDF.util.FileSorter.Sortby;

public class CreateAction extends AbstractAction {

    private static final String DEFAULT_PDF_SIZE = "A4";
    private static final String DEFAULT_PDF_ALIGN = "CENTER-CENTER";
    private static final String DEFAULT_PDF_DIRECTION = "Portrait";
    private static final String DEFAULT_PDF_SORTBY = "NAME";
    private static final String DEFAULT_PDF_SEQUENCE = "INCREASE";
    private static final String DEFAULT_PDF_FILTER = "glob:*.{PNG,JPG}";

    protected final PageSize pdf_size;
    protected final PageAlign pdf_align;
    protected final PageDirection pdf_direction;
    protected final boolean pdf_auto_rotate;
    protected final Sortby pdf_sortby;
    protected final Sequence pdf_sequence;
    protected final String pdf_owner_password;
    protected final String pdf_user_password;
    protected final DocumentAccessPermission pdf_permission;
    protected final String pdf_dst;
    protected final File list_dst;
    protected final boolean debug;
    protected final boolean overwrite_tasklist;

    protected final File[] dirlists;
    protected final FileFilterHelper ffh;

    private static final Option opt_help;

    static {
        opt_help = PropertiesOption.getOption("h", "help", "create.help");
    }

    public CreateAction(String[] args) throws ParseException, HandledException {
        super(getLocaleOptions());

        CommandLine cmd = (new CheckHelpParser(opt_help)).parse(options, args);
        if (cmd.hasOption("-h"))
            throw new HelperException(options);

        debug = cmd.hasOption("debug");
        overwrite_tasklist = cmd.hasOption("overwrite");
        try {
            pdf_size = PageSize.getByString(cmd.getOptionValue("pdf_size", DEFAULT_PDF_SIZE));

            pdf_align = new PageAlign(cmd.getOptionValue("pdf_align", DEFAULT_PDF_ALIGN));

            pdf_direction = PageDirection.getByString(cmd.getOptionValue("pdf_direction", DEFAULT_PDF_DIRECTION));

            pdf_sortby = Sortby.getByString(cmd.getOptionValue("pdf_sortby", DEFAULT_PDF_SORTBY));

            pdf_sequence = Sequence.getByString(cmd.getOptionValue("pdf_sequence", DEFAULT_PDF_SEQUENCE));

        } catch (UnrecognizedEnumException e) {
//            TODO: Check
            String list=listEnum(e.getEnumClass());
            System.err.printf(SharedSpace.getResString("public.err.unrecognizable_enum_long")+"\n",e.getUnrecognizableEnum(),e.getEnumClass().getSimpleName(),list);
            throw new HandledException(e,getClass());
        }

        pdf_permission = new DocumentAccessPermission(cmd.getOptionValue("pdf_permission", "11"));

        pdf_auto_rotate = cmd.hasOption("pdf_auto_rotate");

        pdf_owner_password = cmd.getOptionValue("pdf_owner_password");
        pdf_user_password = cmd.getOptionValue("pdf_user_password");

        pdf_dst = cmd.getOptionValue("pdf_destination");

        String list_destination = cmd.getOptionValue("list_destination");
        list_dst = (new File(list_destination)).getAbsoluteFile();

        try {
            ffh = new FileFilterHelper(cmd.getOptionValue("filter", DEFAULT_PDF_FILTER));
        } catch (UnsupportedOperationException e) {
            System.err.printf(SharedSpace.getResString("create.err.filter") + "\n", e.getMessage());
            throw new HandledException(e,getClass());
        }

        String[] str_sources = cmd.getOptionValues("source");
        try {
            dirlists = verifyFiles(str_sources);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new HandledException(e,getClass());
        }

        System.out.printf("### " + SharedSpace.getResString("create.tasklist_config")
                        + " ###\n%s:%s\n%s:%s\n%s:%s\n%s:%s\n%s:%s\n%s:%s\n%s:%s############\n",
//
                SharedSpace.getResString("create.arg.pdf_align.name"), pdf_align,
//
                SharedSpace.getResString("create.arg.pdf_size.name"), pdf_size,
//
                SharedSpace.getResString("create.arg.pdf_direction.name"), pdf_direction,
//
                SharedSpace.getResString("create.arg.pdf_auto_rotate.name"), pdf_auto_rotate,
//
                SharedSpace.getResString("create.arg.filter.name"), ffh.getOperator(),
//
                SharedSpace.getResString("create.arg.list_destination.name"), list_destination,
//
                SharedSpace.getResString("create.arg.source.name"), dumpArrayString(dirlists)
//
        );
    }

    @Override
    public void start() throws Exception {
        TaskList tasks = new TaskList();

        for (File dirlist : dirlists) {
//          In dirlist
            FileChecker.checkReadableFile(dirlist);
            System.out.printf(SharedSpace.getResString("create.import_from_list") + "\n", dirlist.getName());

            List<String> lines = Files.readAllLines(dirlist.toPath(), SharedSpace.Configuration.DEFAULT_CHARSET);
            for (int line_counter = 0; line_counter < lines.size(); line_counter++) {
//              In line
                String line = lines.get(line_counter);

//            Ignore BOM Header:
                line = line.replace("\uFEFF", "");

                if (line.trim().isEmpty() || line.isEmpty())
                    continue;

                File dir = new File(line);

                if (!dir.isAbsolute()) {
                    dir = new File(dirlist.getParent(), line);
                } else {
                    dir = new File(line);
                }


                if (!dir.exists()) {
                    RuntimeException e=new RuntimeException(String.format(SharedSpace.getResString("create.err.source_filenotfound") + "\n", dirlist.getName(),
                            line_counter, dir.getAbsolutePath()));
                    System.err.println(e.getMessage());
                    throw new HandledException(e,getClass());
                }

                if (dir.isFile()) {
                    RuntimeException e=new RuntimeException(String.format(SharedSpace.getResString("create.err.source_path_is_file") + "\n", dirlist.getName(),
                            line_counter, dir.getAbsolutePath()));
                    System.err.println(e.getMessage());
                    throw new HandledException(e,getClass());
                }

                System.out.printf("\t[" + SharedSpace.getResString("public.info.importing") + "] %s\n",
                        dir.getAbsolutePath());

                tasks.add(mergeArgumentsToTask(dir));

                System.out.printf("\t[" + SharedSpace.getResString("public.info.imported") + "] %s\n",
                        dir.getAbsolutePath());
            }

        }
        if (!overwrite_tasklist && list_dst.exists()) {
            RuntimeException e=new RuntimeException(String.format(SharedSpace.getResString("public.err.overwrite") + "\n", list_dst.getAbsolutePath()));
            System.err.println(e.getMessage());
            throw new HandledException(e,getClass());
        }

        try {
            tasks.toXMLFile(list_dst);
            System.out.printf("[" + SharedSpace.getResString("public.info.exported") + "] %s\n", list_dst.getAbsolutePath());
        } catch (IOException e) {
            IOException e2=new IOException(String.format(SharedSpace.getResString("create.err.tasklist_create") + "\n", e.getMessage()));
            System.err.println(e2.getMessage());
            throw new HandledException(e2,getClass());
        }
    }

    private Task mergeArgumentsToTask(File source_directory) throws IOException {

        NameFormatter nf = new NameFormatter(source_directory);
        DocumentArgument documentArgument = new DocumentArgument(pdf_owner_password, pdf_user_password, pdf_permission, new File(nf.format(pdf_dst)));
        PageArgument pageArgument = new PageArgument();
        pageArgument.setAlign(pdf_align);
        pageArgument.setSize(pdf_size);
        pageArgument.setDirection(pdf_direction);
        pageArgument.setAutoRotate(pdf_auto_rotate);
        return new Task(documentArgument, pageArgument, importSortedImagesFiles(source_directory));
    }

    private File[] importSortedImagesFiles(File source_directory) {
        File[] files = source_directory.listFiles(ffh);
        if (files == null)
            files = new File[0];


        FileSorter sorter = new FileSorter(pdf_sortby, pdf_sequence);
        Arrays.sort(files, sorter);
        if (debug) {
            System.out.println("@Debug");
            System.out.println("Sort Images:");
            for (File img : files) {
                System.out.println("\t" + img);
            }
            System.out.println();
        }

        return files;
    }

    private static <T> String dumpArrayString(T[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (array != null && array.length != 0) {
            sb.append(array[0].toString());
            for (int i = 1; i < array.length; i++) {
                sb.append(",");
                sb.append(array[i].toString());
            }
        }
        sb.append("]\n");
        return sb.toString();
    }

    private static Options getLocaleOptions() {
        Options options = new Options();
        Option opt_debug = PropertiesOption.getOption("d", "debug", "create.arg.debug.help");
        Option opt_overwrite = PropertiesOption.getOption("ow", "overwrite", "create.arg.overwrite_tasklist.help");

        Option opt_pdf_size = PropertiesOption.getArgumentOption("pz", "pdf_size", "create.arg.pdf_size.help", listEnum(PageSize.class));
        Option opt_pdf_align = PropertiesOption.getArgumentOption("pa", "pdf_align", "create.arg.pdf_align.help");
        Option opt_pdf_direction = PropertiesOption.getArgumentOption("pdi", "pdf_direction", "create.arg.pdf_direction.help",
                listEnum(PageDirection.class));
        Option opt_pdf_auto_rotate = PropertiesOption.getOption("par", "pdf_auto_rotate", "create.arg.pdf_auto_rotate.help");
        Option opt_pdf_sortby = PropertiesOption.getArgumentOption("ps", "pdf_sortby", "create.arg.pdf_sortby.help", listEnum(Sortby.class));
        Option opt_pdf_sequence = PropertiesOption.getArgumentOption("pseq", "pdf_sequence", "create.arg.pdf_sequence.help", listEnum(Sequence.class));
        Option opt_pdf_owner_password = PropertiesOption.getArgumentOption("popwd", "pdf_owner_password",
                "create.arg.pdf_owner_password.help");
        Option opt_pdf_user_password = PropertiesOption.getArgumentOption("pupwd", "pdf_user_password", "create.arg.pdf_user_password.help");

        Option opt_pdf_permission = PropertiesOption.getArgumentOption("pp", "pdf_permission", "create.arg.pdf_permission.help");

        Option opt_pdf_destination = PropertiesOption.getArgumentOption("pdst", "pdf_destination", "create.arg.pdf_destination.help");
        opt_pdf_destination.setRequired(true);

        Option opt_filter = PropertiesOption.getArgumentOption("f", "filter", "create.arg.filter.help");

        Option opt_sources = PropertiesOption.getArgumentOption("src", "source", "create.arg.source.help");
        opt_sources.setRequired(true);

        Option opt_list_destination = PropertiesOption.getArgumentOption("ldst", "list_destination", "create.arg.list_destination.help");
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

        Option opt_mode = new Option("m", "mode", true, "mode");
        options.addOption(opt_mode);

        return options;
    }

}