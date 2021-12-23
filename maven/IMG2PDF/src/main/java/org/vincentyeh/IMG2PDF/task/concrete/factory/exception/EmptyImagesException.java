package org.vincentyeh.IMG2PDF.task.concrete.factory.exception;


public class EmptyImagesException extends Exception {
    public EmptyImagesException(){
        super("No image was found.");
    }
}
