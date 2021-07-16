package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskListTemplateFactory<T>{
    private final TaskFactory factory;

    protected TaskListTemplateFactory(TaskFactory factory) {
        this.factory = factory;
    }

    protected abstract List<T> getList();

    public final List<Task> create(){
        List<Task> tasks=new ArrayList<>();
        for(T t:getList())
            tasks.add(factory.create(t));
        return tasks;
    }
}
