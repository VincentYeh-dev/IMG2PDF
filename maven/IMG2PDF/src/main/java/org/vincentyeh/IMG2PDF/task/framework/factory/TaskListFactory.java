package org.vincentyeh.IMG2PDF.task.framework.factory;

import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.util.List;

public interface TaskListFactory {
    List<Task> create() throws Exception;
}
