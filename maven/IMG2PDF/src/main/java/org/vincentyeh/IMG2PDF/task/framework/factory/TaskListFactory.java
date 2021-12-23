package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.factory.exception.TaskBuilderException;
import org.vincentyeh.IMG2PDF.task.framework.factory.exception.TaskListFactoryException;
import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskListFactory<SOURCE> {
    private final TaskBuilder<SOURCE> builder;

    protected TaskListFactory(TaskBuilder<SOURCE> builder) {
        this.builder = builder;
    }

    protected abstract List<SOURCE> getSourceList() throws TaskListFactoryException;

    public final List<Task> create() throws TaskListFactoryException, TaskBuilderException {
        List<Task> tasks = new ArrayList<>();
        for (SOURCE source : getSourceList()) {
            tasks.add(builder.build(source));
        }
        return tasks;
    }
}
