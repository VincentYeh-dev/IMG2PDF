package org.vincentyeh.IMG2PDF.framework.parameter;

public interface DocumentArgument {
    String getOwnerPassword();
    String getUserPassword();
    Permission getPermission();
    PDFDocumentInfo getInformation();

}
