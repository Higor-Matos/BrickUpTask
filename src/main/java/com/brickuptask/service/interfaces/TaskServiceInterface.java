package com.brickuptask.service.interfaces;
import com.brickuptask.domain.entity.TaskEntity;

import java.util.List;
import java.util.Optional;

public interface TaskServiceInterface {
    TaskEntity saveTask(TaskEntity task);

    Optional<TaskEntity> getTaskById(Integer taskId);

    List<TaskEntity> getAllTasks();

    void deleteTask(Integer taskId);
}
