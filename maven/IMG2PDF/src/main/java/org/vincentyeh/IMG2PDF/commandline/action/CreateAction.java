package org.vincentyeh.IMG2PDF.commandline.action;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.vincentyeh.IMG2PDF.SharedSpace;
import org.vincentyeh.IMG2PDF.commandline.parser.core.HandledException;
import org.vincentyeh.IMG2PDF.pdf.doc.DocumentArgument;
import org.vincentyeh.IMG2PDF.pdf.page.PageArgument;
import org.vincentyeh.IMG2PDF.util.file.FileFilterHelper;
import org.vincentyeh.IMG2PDF.task.Task;
import org.vincentyeh.IMG2PDF.task.TaskList;
import org.vincentyeh.IMG2PDF.util.file.FileSorter;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.NameFormatter;

public class CreateAction implements Action {


    private final FileSorter fileSorter;
    protected final FileFilterHelper ffh;

    //    For PDF
    private final DocumentArgument documentArgument;
    private final PageArgument pageArgument;
    private final String pdf_dst;

    //    For tasklist
    private final File tasklist_dst;
    private final boolean debug;
    private final boolean overwrite;
    private final File[] sourceFiles;

    private CreateAction(FileSorter fileSorter, FileFilterHelper ffh, DocumentArgument documentArgument, PageArgument pageArgument, String pdf_dst, File tasklist_dst, boolean debug, boolean overwrite, File[] sourceFiles) {
        this.fileSorter = fileSorter;
        this.ffh = ffh;
        this.documentArgument = documentArgument;
        this.pageArgument = pageArgument;
        this.pdf_dst = pdf_dst;
        this.tasklist_dst = tasklist_dst;
        this.debug = debug;
        this.overwrite = overwrite;
        this.sourceFiles = sourceFiles;
    }

//    private void printInformation() {
//        System.out.println();
//        System.out.printf("### " + SharedSpace.getResString("create.tasklist_config")
//                        + " ###\n%s:%s\n%s:%s\n%s:%s\n%s:%s\n%s:%s\n%s:%s\n%s:%s\n############\n",
////
//                SharedSpace.getResString("create.arg.pdf_align.name"), pageArgument.getAlign(),
////
//                SharedSpace.getResString("create.arg.pdf_size.name"), pageArgument.getSize(),
////
//                SharedSpace.getResString("create.arg.pdf_direction.name"), pageArgument.getDirection(),
////
//                SharedSpace.getResString("create.arg.pdf_auto_rotate.name"), pageArgument.getAutoRotate(),
////
//                SharedSpace.getResString("create.arg.filter.name"), ffh.getOperator(),
////
//                SharedSpace.getResString("create.arg.list_destination.name"), tasklist_dst,
////
//                SharedSpace.getResString("create.arg.source.name"), listStringArray(ArrayToStringArray(sourceFiles))
////
//        );
//        System.out.println();
//    }

    @Override
    public void start() throws Exception {
        TaskList tasks = new TaskList();

        for (File dirlist : sourceFiles) {
//          In dirlist
            FileUtils.checkAbsolute(dirlist);
            FileUtils.checkIsFile(dirlist);

            System.out.printf(SharedSpace.getResString("create.import_from_list") + "\n", dirlist.getName());
            List<String> lines = Files.readAllLines(dirlist.toPath(), SharedSpace.Configuration.DEFAULT_CHARSET);

            for (int line_index = 0; line_index < lines.size(); line_index++) {

                String line = getFixedLine(lines.get(line_index));

                if (line == null)
                    continue;

                File dir = getAbsoluteFileFromLine(line, dirlist);

                System.out.printf("\t[" + SharedSpace.getResString("public.info.importing") + "] %s\n",
                        dir.getAbsolutePath());

                checkDirectoryInSource(dirlist, line_index, dir);

                tasks.add(mergeAllIntoTask(dir));
                System.out.printf("\t[" + SharedSpace.getResString("public.info.imported") + "] %s\n",
                        dir.getAbsolutePath());
            }

        }

        if (!overwrite && tasklist_dst.exists()) {
            System.err.printf(SharedSpace.getResString("public.err.overwrite") + "\n", tasklist_dst.getAbsolutePath());
            throw new HandledException(new RuntimeException("Overwrite deny"), getClass());
        }

        try {
            save(tasks, tasklist_dst);
            System.out.printf("[" + SharedSpace.getResString("public.info.exported") + "] %s\n", tasklist_dst.getAbsolutePath());
        } catch (IOException e) {
            System.err.printf(SharedSpace.getResString("create.err.tasklist_create") + "\n", e.getMessage());
            throw new HandledException(e, getClass());
        }
    }

    private void checkDirectoryInSource(File dirlist, int line, File directory) throws HandledException {
        try {
            FileUtils.checkExists(directory);
            FileUtils.checkIsDirectory(directory);
        } catch (FileNotFoundException e) {
            System.err.printf(SharedSpace.getResString("create.err.source_filenotfound") + "\n", dirlist.getName(),
                    line, directory.getAbsolutePath());
            throw new HandledException(e, getClass());
        } catch (FileUtils.WrongTypeException e) {
            System.err.printf(SharedSpace.getResString("create.err.source_path_is_file") + "\n", dirlist.getName(),
                    line, directory.getAbsolutePath());
            throw new HandledException(e, getClass());
        }
    }

    private File getAbsoluteFileFromLine(String line, File directoryList) {
        File dir = new File(line);
        if (!dir.isAbsolute()) {
            return new File(directoryList.getParent(), line).getAbsoluteFile();
        } else {
            return dir;
        }
    }

    private String getFixedLine(String raw) {
        if (raw.isEmpty() || raw.trim().isEmpty())
            return null;
        else
            return removeBOMHeaderInUTF8Line(raw).trim();
    }

    private String removeBOMHeaderInUTF8Line(String raw) {
        return raw.replace("\uFEFF", "");
    }

    private Task mergeAllIntoTask(File source_directory) throws IOException {
        FileUtils.checkAbsolute(source_directory);
        FileUtils.checkExists(source_directory);
        FileUtils.checkIsDirectory(source_directory);

        NameFormatter nf = new NameFormatter(source_directory);
        return new Task(documentArgument, pageArgument, importSortedImagesFiles(source_directory), new File(nf.format(pdf_dst)).getAbsoluteFile());
    }

    private File[] importSortedImagesFiles(File source_directory) {
        File[] files = source_directory.listFiles(ffh);
        if (files == null)
            files = new File[0];


        Arrays.sort(files,fileSorter);
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

    public void save(TaskList taskList, File destination) throws IOException {
        FileUtils.makeDirsIfNotExists(destination.getParentFile());
        FileUtils.checkAbsolute(destination);
        FileUtils.checkIsFile(destination);

        Document doc = new Document();
        Element root = taskList.toElement();
        doc.setRootElement(root);
        XMLOutputter outer = new XMLOutputter();
        Format format = Format.getPrettyFormat();
        outer.setFormat(format);

        outer.output(doc, new OutputStreamWriter(new FileOutputStream(destination), SharedSpace.Configuration.DEFAULT_CHARSET));
    }

    public static class Builder{
        private FileSorter fileSorter;
        protected FileFilterHelper ffh;

        private DocumentArgument documentArgument;
        private PageArgument pageArgument;
        private String pdf_dst;

        private File tasklist_dst;
        private boolean debug;
        private boolean overwrite;
        private File[] sourceFiles;

        public Builder setFileSorter(FileSorter fileSorter) {
            this.fileSorter = fileSorter;
            return this;
        }

        public Builder setFileFilterHelper(FileFilterHelper ffh) {
            this.ffh = ffh;
            return this;
        }

        public Builder setDocumentArgument(DocumentArgument documentArgument) {
            this.documentArgument = documentArgument;
            return this;
        }

        public Builder setPageArgument(PageArgument pageArgument) {
            this.pageArgument = pageArgument;
            return this;
        }

        public Builder setPdfDestination(String pdf_dst) {
            this.pdf_dst = pdf_dst;

            return this;
        }

        public Builder setTasklistDestination(File tasklist_dst) {
            this.tasklist_dst = tasklist_dst;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setOverwrite(boolean overwrite) {
            this.overwrite = overwrite;
            return this;
        }

        public Builder setSourceFiles(File[] sourceFiles) {
            this.sourceFiles = sourceFiles;
            return this;
        }

        public CreateAction build(){
            return new CreateAction(fileSorter, ffh, documentArgument, pageArgument, pdf_dst, tasklist_dst, debug, overwrite, sourceFiles);
        }
    }
}