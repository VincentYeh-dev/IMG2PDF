package org.vincentyeh.IMG2PDF.task.concrete.exception;


public class EmptyImagesException extends Exception {
    public EmptyImagesException(){
        super("No image was found.");
    }
}
