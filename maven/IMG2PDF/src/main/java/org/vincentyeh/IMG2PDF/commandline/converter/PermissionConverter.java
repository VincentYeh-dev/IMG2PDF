package org.vincentyeh.IMG2PDF.commandline.converter;

import org.vincentyeh.IMG2PDF.commandline.converter.core.BasicConverter;
import org.vincentyeh.IMG2PDF.parameter.Permission;
import picocli.CommandLine;

public class PermissionConverter extends BasicConverter<Permission> {
    @Override
    public Permission convert(String s) throws Exception {
        checkNull(s,getClass().getName()+".s");
        checkEmpty(s,getClass().getName()+".s");
        if(!s.matches("-?[0-9]+")){
            throw new CommandLine.TypeConversionException("AccessPermission contain no-numeric character.");
        }
        int integer=Integer.parseInt(s);
        if(integer>255||integer<0)
            throw new CommandLine.TypeConversionException("AccessPermission out of range[0-255].");

        byte data=(byte)integer;
        boolean[] value=new boolean[8];

        for(int i=0;i<8;i++){
            value[i]=(data&128)!=0;
            data<<=1;
        }

        return new Permission() {
            @Override
            public boolean getCanAssembleDocument() {
                return value[0];
            }

            @Override
            public boolean getCanExtractContent() {
                return value[1];
            }

            @Override
            public boolean getCanExtractForAccessibility() {
                return value[2];
            }

            @Override
            public boolean getCanFillInForm() {
                return value[3];
            }

            @Override
            public boolean getCanModify() {
                return value[4];
            }

            @Override
            public boolean getCanModifyAnnotations() {
                return value[5];
            }

            @Override
            public boolean getCanPrint() {
                return value[6];
            }

            @Override
            public boolean getCanPrintDegraded() {
                return value[7];
            }
        };
    }
}
