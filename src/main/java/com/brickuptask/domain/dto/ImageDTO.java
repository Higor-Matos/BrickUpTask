package com.brickuptask.domain.dto;

public class ImageDTO {
    private Integer imageId;
    private String imageData;
    private Integer taskId;

    public ImageDTO() {}

    public ImageDTO(String imageData, Integer taskId) {
        this.imageData = imageData;
        this.taskId = taskId;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
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
