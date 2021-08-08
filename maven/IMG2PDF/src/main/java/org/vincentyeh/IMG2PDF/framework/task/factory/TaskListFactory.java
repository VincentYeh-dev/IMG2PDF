package org.vincentyeh.IMG2PDF.framework.task.factory;

import org.vincentyeh.IMG2PDF.framework.task.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskListFactory<SOURCE> {
    private final TaskFactory<SOURCE> factory;

    protected TaskListFactory(TaskFactory<SOURCE> factory) {
        this.factory = factory;
    }

    protected abstract List<SOURCE> getSourceList() throws Exception;

    public final List<Task> create() throws Exception {
        List<Task> tasks = new ArrayList<>();
        for (SOURCE source : getSourceList()) {
            tasks.add(factory.create(source));
        }
        return tasks;
    }
}
