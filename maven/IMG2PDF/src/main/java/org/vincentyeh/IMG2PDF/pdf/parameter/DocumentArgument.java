package org.vincentyeh.IMG2PDF.pdf.parameter;

public class DocumentArgument {
    private String ownerPassword;
    private String userPassword;
    private Permission permission;
    private PDFDocumentInfo info;

    public final String getOwnerPassword(){
        return ownerPassword;
    }

    public final String getUserPassword(){
        return userPassword;
    }

    public final Permission getPermission(){
        return permission;
    }

    public final PDFDocumentInfo getInformation(){
        return info;
    }

    public final void setInformation(PDFDocumentInfo info) {
        this.info = info;
    }

    public final void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }

    public final void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public final void setPermission(Permission permission) {
        this.permission = permission;
    }
}
