package org.vincentyeh.IMG2PDF.task.parser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.vincentyeh.IMG2PDF.pdf.page.PageArgument;
import org.vincentyeh.IMG2PDF.task.Task;
import org.vincentyeh.IMG2PDF.task.parser.converter.DocumentArgumentConverter;
import org.vincentyeh.IMG2PDF.task.parser.converter.PageArgumentConverter;

public class TaskConverter {

    private final XStream xStream;

    public TaskConverter() {
        xStream = new XStream(new DomDriver());
        xStream.processAnnotations(Task.class);
        xStream.registerConverter(new PageArgumentConverter());
        xStream.registerConverter(new DocumentArgumentConverter());

    }

    public String toXml(Task task){
        return xStream.toXML(task);
    }
    public Task parse(String str){
        return (Task) xStream.fromXML(str);
    }




}
