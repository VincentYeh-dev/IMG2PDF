package org.vincentyeh.IMG2PDF.xStream.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;

import java.util.HashMap;
import java.util.Map;

public class AccessPermissionConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        AccessPermission accessPermission = (AccessPermission) o;
        writeNode("canAssembleDocument", accessPermission.canAssembleDocument(), writer);
        writeNode("canExtractContent", accessPermission.canExtractContent(), writer);
        writeNode("canExtractForAccessibility", accessPermission.canExtractForAccessibility(), writer);
        writeNode("canFillInForm", accessPermission.canFillInForm(), writer);
        writeNode("canModify", accessPermission.canModify(), writer);
        writeNode("canModifyAnnotations", accessPermission.canModifyAnnotations(), writer);
        writeNode("canPrint", accessPermission.canPrint(), writer);
        writeNode("canPrintDegraded", accessPermission.canPrintDegraded(), writer);
    }

    private void writeNode(String node, boolean value, HierarchicalStreamWriter writer) {
        writer.startNode(node);
        writer.setValue(Boolean.toString(value));
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
        final Map<String,String> elements = new HashMap<>();
        while (reader.hasMoreChildren()){
            reader.moveDown();
            elements.put(reader.getNodeName(),reader.getValue());
            reader.moveUp();
        }

        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setCanAssembleDocument(Boolean.parseBoolean(elements.get("canAssembleDocument")));
        accessPermission.setCanExtractContent(Boolean.parseBoolean(elements.get("canExtractContent")));
        accessPermission.setCanExtractForAccessibility(Boolean.parseBoolean(elements.get("canExtractForAccessibility")));
        accessPermission.setCanFillInForm(Boolean.parseBoolean(elements.get("canFillInForm")));
        accessPermission.setCanModify(Boolean.parseBoolean(elements.get("canModify")));
        accessPermission.setCanModifyAnnotations(Boolean.parseBoolean(elements.get("canModifyAnnotations")));
        accessPermission.setCanPrint(Boolean.parseBoolean(elements.get("canPrint")));
        accessPermission.setCanPrintDegraded(Boolean.parseBoolean(elements.get("canPrintDegraded")));
        return accessPermission;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(AccessPermission.class);
    }
}
