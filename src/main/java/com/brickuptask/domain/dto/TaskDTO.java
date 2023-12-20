package com.brickuptask.domain.dto;

import com.brickuptask.domain.entity.TaskEntity.TaskStatus;

public class TaskDTO {
    private String title;
    private String description;
    private TaskStatus status;
    private Integer taskId;

    public TaskDTO() {
    }
    public TaskDTO(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

}
