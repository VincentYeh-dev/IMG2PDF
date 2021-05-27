package org.vincentyeh.IMG2PDF.commandline.action;

import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

import org.vincentyeh.IMG2PDF.converter.exception.ConversionException;
import org.vincentyeh.IMG2PDF.converter.exception.OverwriteDenyException;
import org.vincentyeh.IMG2PDF.converter.exception.ReadImageException;
import org.vincentyeh.IMG2PDF.converter.listener.DefaultConversionInfoListener;
import org.vincentyeh.IMG2PDF.SharedSpace;
import org.vincentyeh.IMG2PDF.commandline.parser.core.HandledException;
import org.vincentyeh.IMG2PDF.converter.PDFConverter;
import org.vincentyeh.IMG2PDF.task.Task;
import org.vincentyeh.IMG2PDF.task.TaskListConverter;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;

public class ConvertAction implements Action {

    private final File tempFolder;
    private final long maxMainMemoryBytes;
    private final File[] tasklist_sources;
    private final boolean open_when_complete;
    private final boolean overwrite_output;

    private ConvertAction(File tempFolder, long maxMainMemoryBytes, File[] tasklist_sources, boolean open_when_complete, boolean overwrite_output) {
        this.tempFolder = tempFolder;
        this.maxMainMemoryBytes = maxMainMemoryBytes;
        this.tasklist_sources = tasklist_sources;
        this.open_when_complete = open_when_complete;
        this.overwrite_output = overwrite_output;
    }

    @Override
    public void start() throws Exception {
        System.out.println(SharedSpace.getResString("convert.import_tasklists"));
        for (File src : tasklist_sources) {
            System.out.print(
                    "\t[" + SharedSpace.getResString("public.info.importing") + "] " + src.getAbsolutePath() + "\r");
            try {
                List<Task> tasks = getTaskListFromFile(src);
                System.out.print("\t[" + SharedSpace.getResString("public.info.imported") + "] " + src.getAbsolutePath() + "\r\n\n");
                System.out.println(SharedSpace.getResString("convert.start_conversion"));
                convertAllToFile(tasks);
            } catch (HandledException ignored) {
            }

        }

    }

    private void convertAllToFile(List<Task> tasks) throws Exception {
//                TODO:No exception is thrown when task.getArray() is empty.Warning to the user when it happen.
        for (Task task : tasks) {
            try {
                File result = convertToFile(task);
                if (open_when_complete)
                    openPDF(result);
            } catch (HandledException ignored) {
            }
        }
    }


    private void openPDF(File file) {
        Desktop desktop = Desktop.getDesktop();
        if (file.exists())
            try {
                desktop.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private File convertToFile(Task task) throws IOException, HandledException {
        PDFConverter converter;
        try {
            converter = new PDFConverter(task, maxMainMemoryBytes, tempFolder, overwrite_output);
            converter.setInfoListener(new DefaultConversionInfoListener());
        } catch (IOException e) {
//            TODO:print error message
            throw new HandledException(e, getClass());
        }

        try {
            return converter.start();

        } catch (OverwriteDenyException e) {
            System.err.printf("\t" + SharedSpace.getResString("convert.listener.err.overwrite") + "\n", e.getFile().getAbsolutePath());
            throw new HandledException(e, getClass());
        } catch (ReadImageException e) {
            System.err.printf("\n\t\t" + SharedSpace.getResString("convert.listener.err.image") + "\n", e.getMessage());
            throw new HandledException(e, getClass());
        } catch (ConversionException e) {
            System.err.printf("\n\t\t" + SharedSpace.getResString("convert.listener.err.conversion") + "\n", e.getMessage());
            throw new HandledException(e, getClass());
        }

    }

    private List<Task> getTaskListFromFile(final File file)
            throws HandledException {
        try {
            FileUtils.checkAbsolute(file);
            FileUtils.checkExists(file);
            FileUtils.checkIsFile(file);
        } catch (FileUtils.WrongTypeException e) {
//            TODO:Print error message
            throw new HandledException(e, getClass());
        } catch (FileUtils.PathNotAbsoluteException e) {
//            TODO:Print error message
            throw new HandledException(e, getClass());
        } catch (FileNotFoundException e) {
//            TODO:Print error message
            throw new HandledException(e, getClass());
        }
        try {
            String xml= String.join("\n", Files.readAllLines(file.toPath(),SharedSpace.Configuration.DEFAULT_CHARSET));
            return (new TaskListConverter()).parse(xml);
        } catch (IOException e) {
//            TODO:Print error message
            throw new HandledException(e, getClass());
        }catch (com.thoughtworks.xstream.converters.ConversionException e){
            System.err.println(e.getCause().getMessage());
            throw new HandledException(e, getClass());
        }

    }

    public static class Builder {
        private File tempFolder;
        private long maxMainMemoryBytes;
        private File[] tasklist_sources;
        private boolean open_when_complete;
        private boolean overwrite_output;

        public ConvertAction build(){
            return new ConvertAction(tempFolder,maxMainMemoryBytes,tasklist_sources,open_when_complete,overwrite_output);
        }

        public Builder setTempFolder(File tempFolder) {
            this.tempFolder = tempFolder;
            return this;
        }

        public Builder setMaxMainMemoryBytes(long maxMainMemoryBytes) {
            this.maxMainMemoryBytes = maxMainMemoryBytes;

            return this;
        }

        public Builder setTasklistSources(File[] tasklist_sources) {
            this.tasklist_sources = tasklist_sources;

            return this;
        }

        public Builder setOpenWhenComplete(boolean open_when_complete) {
            this.open_when_complete = open_when_complete;
            return this;
        }

        public Builder setOverwrite(boolean overwrite_output) {
            this.overwrite_output = overwrite_output;
            return this;
        }
    }

}
