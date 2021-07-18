package org.vincentyeh.IMG2PDF.task.concrete.factory;

import org.vincentyeh.IMG2PDF.task.concrete.exception.EmptyImagesException;
import org.vincentyeh.IMG2PDF.task.concrete.exception.TextLineException;
import org.vincentyeh.IMG2PDF.task.concrete.factory.argument.TextLineCreateArgument;
import org.vincentyeh.IMG2PDF.task.concrete.factory.argument.TextLineInitialArgument;
import org.vincentyeh.IMG2PDF.task.framework.DocumentArgument;
import org.vincentyeh.IMG2PDF.task.framework.PageArgument;
import org.vincentyeh.IMG2PDF.task.framework.Task;
import org.vincentyeh.IMG2PDF.task.framework.factory.CreateArgument;
import org.vincentyeh.IMG2PDF.task.framework.factory.TaskFactory;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;
import org.vincentyeh.IMG2PDF.util.file.exception.WrongFileTypeException;

import java.io.File;
import java.util.Arrays;

public class TextLineTaskFactory extends TaskFactory {

    protected TextLineTaskFactory(TextLineInitialArgument initialArgument) {
        super(initialArgument);
    }

    private File[] importSortedImagesFiles(File source_directory) throws EmptyImagesException{
        final TextLineInitialArgument initialArgument = (TextLineInitialArgument) this.initialArgument;

        File[] files = source_directory.listFiles(initialArgument.getFilter());

        if (files == null || files.length == 0)
            throw new EmptyImagesException("No image was found in: " + source_directory);

        Arrays.sort(files, initialArgument.getSorter());

        return files;
    }

    @Override
    public Task create(CreateArgument createArgument) throws Exception {
        final TextLineCreateArgument textLineCreateArgument = (TextLineCreateArgument) createArgument;
        final TextLineInitialArgument initialArgument = (TextLineInitialArgument) this.initialArgument;

        try {
            FileUtils.checkExists(textLineCreateArgument.getSource());
            FileUtils.checkType(textLineCreateArgument.getSource(), WrongFileTypeException.Type.FOLDER);

            File[] images = importSortedImagesFiles(textLineCreateArgument.getSource());
            File destination = new File(initialArgument.getFormatter().format(textLineCreateArgument.getSource())).getAbsoluteFile();

            return new Task() {
                @Override
                public DocumentArgument getDocumentArgument() {
                    return initialArgument.getDocumentArgument();
                }

                @Override
                public PageArgument getPageArgument() {
                    return initialArgument.getPageArgument();
                }

                @Override
                public File[] getImages() {
                    return images;
                }

                @Override
                public File getPdfDestination() {
                    return destination;
                }
            };

        } catch (Exception e) {
            throw new TextLineException(e,textLineCreateArgument.getSource(),textLineCreateArgument.getLine());
        }
    }
}
