package org.vincentyeh.IMG2PDF.task.framework;

import org.apache.pdfbox.pdmodel.encryption.AccessPermission;

public interface DocumentArgument {

    String getOwnerPassword();
    String getUserPassword();
    AccessPermission getAccessPermission();

}
