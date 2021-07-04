package org.vincentyeh.IMG2PDF.pdf.converter.listener;

import org.vincentyeh.IMG2PDF.task.Task;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.vincentyeh.IMG2PDF.util.PrinterUtils.print;
import static org.vincentyeh.IMG2PDF.util.PrinterUtils.printRenderFormat;

public class DefaultConversionListener implements ConversionListener {
    private final char[] progress_bar;
    private final ResourceBundle resourceBundle;
    private Task task;
    private long startSeconds;

    public DefaultConversionListener(Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle("pdf_converter_listener",locale);
        progress_bar = new char[10];
        Arrays.fill(progress_bar, ' ');
    }

    private int total;
    private int counter = 0;

    private double perImg;
    private double progress = 0;

    @Override
    public void onConversionPreparing(Task task) {
        this.task = task;
        total = task.getImages().length;
        perImg = (10. / total);
        startSeconds =(System.currentTimeMillis()/1000);
    }

    @Override
    public void onConverting(int index, File file) {
        progress += perImg;
        while (progress >= 1) {
            progress_bar[counter] = '=';
            progress -= 1;
            counter++;
        }
        String name = getSimplifiedName(task.getPdfDestination().getAbsolutePath());
        printRenderFormat(resourceBundle.getString("convert.listener.converting")+"\r", new String(progress_bar), name, index + 1, total, file.getName());
    }

    @Override
    public void onConversionComplete(File dst) {
        long completeSeconds = System.currentTimeMillis()/1000;
        printRenderFormat(resourceBundle.getString("convert.listener.done"), (completeSeconds - startSeconds),dst.getAbsolutePath());
    }

    @Override
    public void onFinally(){
        print("\r\n");
    }

    private String getSimplifiedName(String raw){

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            if (i < 30) {
                sb.append(raw.charAt(i));
            } else if (i > raw.length() - 30) {
                sb.append(raw.charAt(i));
            }else{
                sb.append("#");
            }
        }
       return sb.toString().replaceAll("#+","...");
    }

}
