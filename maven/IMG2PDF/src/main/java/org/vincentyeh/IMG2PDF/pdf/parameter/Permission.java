package org.vincentyeh.IMG2PDF.pdf.parameter;

public interface Permission {
    boolean getCanAssembleDocument();
    boolean getCanExtractContent();
    boolean getCanExtractForAccessibility();
    boolean getCanFillInForm();
    boolean getCanModify();
    boolean getCanModifyAnnotations();
    boolean getCanPrint();
    boolean getCanPrintDegraded();
}
