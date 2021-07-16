package org.vincentyeh.IMG2PDF.task.concrete;

import org.apache.pdfbox.pdmodel.encryption.AccessPermission;

public class DocumentArgument implements org.vincentyeh.IMG2PDF.task.framework.DocumentArgument {

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


    @Override
    public String getOwnerPassword() {
        return owner_password;
    }

    @Override
    public String getUserPassword() {
        return user_password;
    }

    @Override
    public AccessPermission getAccessPermission() {
        return ap;
    }
}
