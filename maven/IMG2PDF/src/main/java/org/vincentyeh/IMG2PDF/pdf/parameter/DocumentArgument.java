package org.vincentyeh.IMG2PDF.pdf.parameter;

public interface DocumentArgument {
    String getOwnerPassword();
    String getUserPassword();
    Permission getPermission();
    PDFDocumentInfo getInformation();

}
