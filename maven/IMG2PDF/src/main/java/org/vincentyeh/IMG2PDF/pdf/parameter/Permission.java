package org.vincentyeh.IMG2PDF.pdf.parameter;

public final class Permission {
    private boolean canAssembleDocument;
    private boolean canExtractContent;
    private boolean canExtractForAccessibility;
    private boolean canFillInForm;
    private boolean canModify;
    private boolean canModifyAnnotations;
    private boolean canPrint;
    private boolean canPrintDegraded;

    public boolean getCanAssembleDocument() {
        return canAssembleDocument;
    }

    public boolean getCanExtractContent() {
        return canExtractContent;
    }

    public boolean getCanExtractForAccessibility() {
        return canExtractForAccessibility;
    }

    public boolean getCanFillInForm() {
        return canFillInForm;
    }

    public boolean getCanModify() {
        return canModify;
    }

    public boolean getCanModifyAnnotations() {
        return canModifyAnnotations;
    }

    public boolean getCanPrint() {
        return canPrint;
    }

    public boolean getCanPrintDegraded() {
        return canPrintDegraded;
    }

    public void setCanAssembleDocument(boolean canAssembleDocument) {
        this.canAssembleDocument = canAssembleDocument;
    }

    public void setCanExtractContent(boolean canExtractContent) {
        this.canExtractContent = canExtractContent;
    }

    public void setCanExtractForAccessibility(boolean canExtractForAccessibility) {
        this.canExtractForAccessibility = canExtractForAccessibility;
    }

    public void setCanFillInForm(boolean canFillInForm) {
        this.canFillInForm = canFillInForm;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    public void setCanModifyAnnotations(boolean canModifyAnnotations) {
        this.canModifyAnnotations = canModifyAnnotations;
    }

    public void setCanPrint(boolean canPrint) {
        this.canPrint = canPrint;
    }

    public void setCanPrintDegraded(boolean canPrintDegraded) {
        this.canPrintDegraded = canPrintDegraded;
    }
}
