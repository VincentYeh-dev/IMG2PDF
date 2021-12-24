package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.factory.exception.TaskBuilderException;
import org.vincentyeh.IMG2PDF.task.framework.factory.exception.TaskListFactoryException;
import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.util.LinkedList;
import java.util.List;

public abstract class TaskListFactory<SOURCE, LIST_SOURCE> {
    private final TaskBuilder<SOURCE> builder;

    protected TaskListFactory(TaskBuilder<SOURCE> builder) {
        this.builder = builder;
    }

    protected abstract List<SOURCE> getSources(LIST_SOURCE list) throws TaskListFactoryException;

    public final List<Task> create(LIST_SOURCE list) throws TaskListFactoryException, TaskBuilderException {
        List<Task> tasks = new LinkedList<>();
        for (SOURCE source : getSources(list)) {
            tasks.add(builder.build(source));
        }
        return tasks;
    }
}
