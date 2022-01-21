package org.vincentyeh.IMG2PDF.pdf.parameter;

public final class DocumentArgument {
    private String ownerPassword;
    private String userPassword;
    private Permission permission;
    private PDFDocumentInfo info;

    public String getOwnerPassword() {
        return ownerPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public Permission getPermission() {
        return permission;
    }

    public PDFDocumentInfo getInformation() {
        return info;
    }

    public void setInformation(PDFDocumentInfo info) {
        this.info = info;
    }

    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
