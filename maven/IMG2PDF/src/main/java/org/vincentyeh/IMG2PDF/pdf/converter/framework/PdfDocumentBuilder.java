package org.vincentyeh.IMG2PDF.pdf.converter.framework;

import org.vincentyeh.IMG2PDF.pdf.parameter.Permission;

import java.io.IOException;

public abstract class PdfDocumentBuilder {
    public abstract void setUserPassword(String userPassword);
    public abstract void setOwnerPassword(String ownerPassword);
    public abstract void setPermission(Permission permission);
    public abstract void setTitle(String title);
    public abstract void encrypt() throws IOException;
    public abstract void addPage(PdfPage<?> page) throws Exception;
}
