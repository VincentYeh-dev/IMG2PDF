package org.vincentyeh.IMG2PDF.parameter;

public class Permission {
    private boolean canAssembleDocument;
    private boolean canExtractContent;
    private boolean canExtractForAccessibility;
    private boolean canFillInForm;
    private boolean canModify;
    private boolean canModifyAnnotations;
    private boolean canPrint;
    private boolean canPrintDegraded;

    public final boolean getCanAssembleDocument(){
        return canAssembleDocument;
    }

    public final boolean getCanExtractContent(){
        return canExtractContent;
    }

    public final boolean getCanExtractForAccessibility(){
        return canExtractForAccessibility;
    }

    public final boolean getCanFillInForm(){
        return canFillInForm;
    }

    public final boolean getCanModify(){
        return canModify;
    }

    public final boolean getCanModifyAnnotations(){
        return canModifyAnnotations;
    }

    public final boolean getCanPrint(){
        return canPrint;
    }

    public final boolean getCanPrintDegraded(){
        return canPrintDegraded;
    }

    public final void setCanAssembleDocument(boolean canAssembleDocument) {
        this.canAssembleDocument = canAssembleDocument;
    }

    public final void setCanExtractContent(boolean canExtractContent) {
        this.canExtractContent = canExtractContent;
    }

    public final void setCanExtractForAccessibility(boolean canExtractForAccessibility) {
        this.canExtractForAccessibility = canExtractForAccessibility;
    }

    public final void setCanFillInForm(boolean canFillInForm) {
        this.canFillInForm = canFillInForm;
    }

    public final void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    public final void setCanModifyAnnotations(boolean canModifyAnnotations) {
        this.canModifyAnnotations = canModifyAnnotations;
    }

    public final void setCanPrint(boolean canPrint) {
        this.canPrint = canPrint;
    }

    public final void setCanPrintDegraded(boolean canPrintDegraded) {
        this.canPrintDegraded = canPrintDegraded;
    }
}
