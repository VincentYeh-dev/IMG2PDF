package org.vincentyeh.IMG2PDF.commandline.concrete.command;

import org.fusesource.jansi.Ansi;
import org.vincentyeh.IMG2PDF.commandline.concrete.converter.*;
import org.vincentyeh.IMG2PDF.handler.ExceptionHandlerFacade;
import org.vincentyeh.IMG2PDF.pdf.PDFacade;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.exception.PDFConversionException;
import org.vincentyeh.IMG2PDF.pdf.parameter.*;
import org.vincentyeh.IMG2PDF.task.TaskListFactoryFacade;
import org.vincentyeh.IMG2PDF.configuration.framework.ConfigurationParser;
import org.vincentyeh.IMG2PDF.handler.framework.CantHandleException;
import org.vincentyeh.IMG2PDF.handler.framework.ExceptionHandler;
import org.vincentyeh.IMG2PDF.pdf.framework.converter.PDFConverter;
import org.vincentyeh.IMG2PDF.pdf.concrete.listener.DefaultConversionListener;
import org.vincentyeh.IMG2PDF.task.framework.Task;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskListFactory;
import org.vincentyeh.IMG2PDF.util.file.FileSorter;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.MakeDirectoryException;
import picocli.CommandLine;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;

import static org.vincentyeh.IMG2PDF.util.PrinterUtils.*;

@CommandLine.Command(name = "convert")
public class ConvertCommand implements Callable<Integer> {

    @CommandLine.ParentCommand
    IMG2PDFCommand img2PDFCommand;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"--sorter", "-sr"}, defaultValue = "NAME-INCREASE", converter = FileSorterConverter.class)
    FileSorter fileSorter;

    @CommandLine.Option(names = {"--filter", "-f"}, defaultValue = "*.{PNG,png,JPG,jpg}", converter = FileFilterConverter.class)
    FileFilter filter;

    @CommandLine.Option(names = {"--pdf_owner_password", "-popwd"})
    String pdf_owner_password;

    @CommandLine.Option(names = {"--pdf_user_password", "-pupwd"})
    String pdf_user_password;

    @CommandLine.Option(names = {"--pdf_permission", "-pp"}, defaultValue = "255", converter = PermissionConverter.class)
    Permission pdf_permission;

    @CommandLine.Option(names = {"--pdf_align", "-pa"}, defaultValue = "CENTER-CENTER", converter = PageAlignConverter.class)
    PageAlign pdf_align;

    @CommandLine.Option(names = {"--pdf_size", "-pz"}, defaultValue = "DEPEND_ON_IMG")
    PageSize pdf_size;

    @CommandLine.Option(names = {"--pdf_direction", "-pdi"}, defaultValue = "Portrait")
    PageDirection pdf_direction;

    @CommandLine.Option(names = {"--pdf_auto_rotate", "-par"})
    boolean pdf_auto_rotate;

    @CommandLine.Option(names = {"--pdf_destination", "-pdst"}, defaultValue = "<NAME>.pdf")
    String pdf_dst;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true)
    boolean usageHelpRequested;

    @CommandLine.Option(names = {"--temp_folder", "-tmp"}, converter = AbsoluteFileConverter.class, defaultValue = ".org.vincentyeh.IMG2PDF.tmp")
    File tempFolder;

    @CommandLine.Option(names = {"--memory_max_usage", "-mx"}, defaultValue = "50MB", converter = ByteSizeConverter.class)
    long maxMainMemoryBytes;

//    @CommandLine.Option(names = {"--open_when_complete", "-o"})
//    boolean open_when_complete;

    @CommandLine.Option(names = {"--overwrite", "-ow"})
    boolean overwrite_output;

    @CommandLine.Parameters(arity = "1..*", converter = AbsoluteFileConverter.class)
    List<File> sourceFiles;


    private final Charset dir_list_read_charset;
    private final Locale locale;

    public ConvertCommand(Map<ConfigurationParser.ConfigParam, Object> config) {
        dir_list_read_charset = (Charset) config.get(ConfigurationParser.ConfigParam.DIR_LIST_READ_CHARSET);
        locale = (Locale) config.get(ConfigurationParser.ConfigParam.LOCALE);
    }

    @Override
    public Integer call() {
        try {
            checkParameters();
            List<Task> tasks = importAllTaskFromDirectoryLists();

            printLine(getResourceBundleString("execution.convert.start.start_conversion"));

            convertAllToFile(tasks);

            return CommandLine.ExitCode.OK;
        } catch (Exception e) {
            printStackTrance(e);
            return CommandLine.ExitCode.SOFTWARE;
        }
    }

    private List<Task> importAllTaskFromDirectoryLists() {
        List<Task> tasks = new LinkedList<>();
        TaskListFactory<?, File> factory = TaskListFactoryFacade.createDirectoryTaskListFactory(dir_list_read_charset, getDocumentArgument(), getPageArgument(), filter, fileSorter, pdf_dst);
        int previous_size = 0;
        for (File directoryList : sourceFiles) {
            try {
                printColorFormat(getResourceBundleString("execution.convert.start.parsing") + "\n", Ansi.Color.BLUE, directoryList.getPath());
                factory.createTo(directoryList, tasks);
                printColorFormat(getResourceBundleString("execution.convert.start.parsed") + "\n", Ansi.Color.BLUE, tasks.size() - previous_size, directoryList.getPath());
                previous_size = tasks.size();
            } catch (Exception e) {
                handleException(e, ExceptionHandlerFacade.getTextFileTaskFactoryExceptionHandler(null), "\t", "");
            }
        }
        return tasks;
    }

    private void checkParameters() throws NoSuchFieldException, IllegalAccessException {
        printDebugLog("-------------------------------------");
        printDebugLog(getColor("Check Parameters", Ansi.Color.YELLOW));
        printDebugLog("-------------------------------------");
        checkPrintNullParameter("overwrite_output");
        checkPrintNullParameter("fileSorter");
        checkPrintNullParameter("filter");
        checkPrintNullParameter("pdf_align");
        checkPrintNullParameter("pdf_size");
        checkPrintNullParameter("pdf_direction");
        checkPrintNullParameter("pdf_dst");
//        checkPrintNullParameter("open_when_complete");
        checkPrintNullParameter("tempFolder");
        checkPrintNullParameter("maxMainMemoryBytes");
        checkPrintNullParameter("sourceFiles");

        for (File source : sourceFiles) {
            if (!source.isAbsolute()) throw new IllegalArgumentException("source is not absolute: " + source);
            if (FileUtils.isRoot(source)) throw new IllegalArgumentException("source is root: " + source);
        }

        if (tempFolder == null) throw new IllegalArgumentException("tempFolder==null");
        if (!tempFolder.isAbsolute()) throw new IllegalArgumentException("tempFolder is not absolute: " + tempFolder);
        if (FileUtils.isRoot(tempFolder)) throw new IllegalArgumentException("tempFolder is root: " + tempFolder);
        printDebugLog("-------------------------------------");
        printDebugLog("-------------------------------------");
    }

    private PageArgument getPageArgument() {
        PageArgument argument=new PageArgument();
        argument.setAlign(pdf_align);
        argument.setSize(pdf_size);
        argument.setDirection(pdf_direction);
        argument.setAutoRotate(pdf_auto_rotate);
        return argument;
    }

    private DocumentArgument getDocumentArgument() {
        DocumentArgument argument=new DocumentArgument();
        argument.setInformation(null);
        argument.setOwnerPassword(pdf_owner_password);
        argument.setUserPassword(pdf_user_password);
        argument.setPermission(pdf_permission);
        return argument;
    }

    private void convertAllToFile(List<Task> tasks) throws MakeDirectoryException {
        printDebugLog("Converter Configuration");
        printDebugLog(getColor("\t|- max main memory usage:" + maxMainMemoryBytes, Ansi.Color.CYAN));
        printDebugLog(getColor("\t|- temporary folder:" + tempFolder.getAbsolutePath(), Ansi.Color.CYAN));
        printDebugLog(getColor("\t|- Overwrite:" + overwrite_output, Ansi.Color.CYAN));
        PDFConverter converter = PDFacade.createImagePDFConverter(maxMainMemoryBytes, tempFolder, overwrite_output, new DefaultConversionListener(locale));

        for (Task task : tasks) {
            printDebugLog("Converting");
            printDebugLog("Name: " + task.getPdfDestination());
            printDebugLog("Images");
            Arrays.stream(task.getImages()).forEach(img -> printDebugLog(getColor("\t|- " + img, Ansi.Color.CYAN)));

            try {
                converter.start(task);
            } catch (PDFConversionException e) {
                handleException(e, ExceptionHandlerFacade.getPDFConversionExceptionHandler(null), "\t", "");
            }
        }
    }


    private void handleException(Exception e, ExceptionHandler handler, String prefix, String suffix) {
        try {
            printRenderFormat(prefix + "[@|red ERROR|@] %s\n" + suffix, handler.handle(e));
            if (img2PDFCommand.isDebug()) printStackTrance(e);
        } catch (CantHandleException cantHandleException) {
            printColor("Can't handle.\n", Ansi.Color.RED);
            printStackTrance(e);
        }
    }


    private void printDebugLog(Object msg) {
        if (img2PDFCommand.isDebug()) {
            printRenderFormat("[@|blue DEBUG|@] %s\n", msg);
        }
    }

    private void printErrorLog(Object msg) {
        if (img2PDFCommand.isDebug()) {
            printRenderFormat("[@|red ERROR|@] %s\n", msg);
        }
    }

    private Ansi getKeyValuePair(String key, Object value, Ansi.Color color) {
        return getRenderFormat("@|yellow %s|@=@|%s %s|@", key, color, value);
    }

    private Ansi getKeyValuePair(String field_name) throws NoSuchFieldException, IllegalAccessException {
        Field field = ConvertCommand.class.getDeclaredField(field_name);
        field.setAccessible(true);
        return getKeyValuePair(field.getName(), field.get(this), Ansi.Color.GREEN);
    }

    private void checkPrintNullParameter(String field_name) throws NoSuchFieldException, IllegalAccessException {
        if (checkNullParameter(field_name)) {
            printDebugLog(getKeyValuePair(field_name));
        } else {
            printErrorLog(getKeyValuePair(field_name, null, Ansi.Color.RED));
            throw new IllegalArgumentException(field_name + "==null");
        }

    }

    private boolean checkNullParameter(String field_name) throws NoSuchFieldException, IllegalAccessException {
        Field field = ConvertCommand.class.getDeclaredField(field_name);
        return (field.get(this) != null);
    }

    private String getResourceBundleString(String key) {
        return spec.resourceBundle().getString(key);
    }
}