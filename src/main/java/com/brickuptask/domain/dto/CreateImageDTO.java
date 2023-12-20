package com.brickuptask.domain.dto;
public class CreateImageDTO {
    private String imageData;
    private Integer taskId;
    public CreateImageDTO() {
    }
    public CreateImageDTO(String imageData, Integer taskId) {
        this.imageData = imageData;
        this.taskId = taskId;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
