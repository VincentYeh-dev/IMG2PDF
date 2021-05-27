package org.vincentyeh.IMG2PDF.task;

import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

public class DocumentArgument {

    public static class Builder {
        private String owner_password;
        private String user_password;
        private AccessPermission ap;

        public Builder setOwnerPassword(String owner_password) {
            this.owner_password = owner_password;
            return this;
        }

        public Builder setUserPassword(String user_password) {
            this.user_password = user_password;
            return this;
        }

        public Builder setAccessPermission(AccessPermission ap) {
            this.ap = ap;
            return this;
        }

        public DocumentArgument build() {
            return new DocumentArgument(owner_password, user_password, ap);
        }

    }

    private final String owner_password;
    private final String user_password;
    private final AccessPermission ap;

    private DocumentArgument(String owner_password, String user_password, AccessPermission ap) {
        this.owner_password = owner_password;
        this.user_password = user_password;
        this.ap = ap;
    }

//    public Element toElement() {
//        Element element = new Element("DocumentArgument");
//
//        Element permission = new Element("permission");
//        permission.setAttribute("canPrint", String.valueOf(ap.canPrint()));
//        permission.setAttribute("canModify", String.valueOf(ap.canModify()));
//        permission.addContent(new Element("OWNER-PASSWORD").addContent(owner_password));
//        permission.addContent(new Element("USER-PASSWORD").addContent(user_password));
//        element.addContent(permission);
//        return element;
//
//    }

    public StandardProtectionPolicy getSpp() {
        return createProtectionPolicy(owner_password, user_password, ap);
    }

    private StandardProtectionPolicy createProtectionPolicy(String owner_pwd, String user_pwd, AccessPermission ap) {

        // Define the length of the encryption key.
        // Possible values are 40 or 128 (256 will be available in PDFBox 2.0).
        int keyLength = 128;
        StandardProtectionPolicy spp = new StandardProtectionPolicy(owner_pwd, user_pwd, ap);
        spp.setEncryptionKeyLength(keyLength);
        return spp;
    }

    public String getOwner_password() {
        return owner_password;
    }

    public String getUser_password() {
        return user_password;
    }

    public AccessPermission getAp() {
        return ap;
    }

}
