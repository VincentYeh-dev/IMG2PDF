package org.vincentyeh.IMG2PDF.commandline.concrete.converter;

import org.vincentyeh.IMG2PDF.commandline.framework.converter.BasicCheckConverter;
import org.vincentyeh.IMG2PDF.pdf.parameter.Permission;
import picocli.CommandLine;

public class PermissionConverter extends BasicCheckConverter<Permission> {
    @Override
    protected Permission doConvert(String s) {
        if (!s.matches("-?[0-9]+")) {
            throw new CommandLine.TypeConversionException("AccessPermission contain no-numeric character.");
        }
        int integer = Integer.parseInt(s);
        if (integer > 255 || integer < 0)
            throw new CommandLine.TypeConversionException("AccessPermission out of range[0-255].");

        byte data = (byte) integer;
        boolean[] value = new boolean[8];

        for (int i = 0; i < 8; i++) {
            value[i] = (data & 128) != 0;
            data <<= 1;
        }
        Permission permission=new Permission();
        permission.setCanAssembleDocument(value[0]);
        permission.setCanExtractContent(value[1]);
        permission.setCanExtractForAccessibility(value[2]);
        permission.setCanFillInForm(value[3]);
        permission.setCanModify(value[4]);
        permission.setCanModifyAnnotations(value[5]);
        permission.setCanPrint(value[6]);
        permission.setCanPrintDegraded(value[7]);

        return permission;
    }
}
