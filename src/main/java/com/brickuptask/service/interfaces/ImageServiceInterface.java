package com.brickuptask.service.interfaces;

import com.brickuptask.domain.entity.ImageEntity;

import java.util.List;
import java.util.Optional;

public interface ImageServiceInterface {
    ImageEntity saveImage(ImageEntity image);

    Optional<ImageEntity> getImageById(Integer imageId);

    List<ImageEntity> getImagesByTaskId(Integer taskId);

    void deleteImage(Integer imageId);
    ImageEntity updateImage(Integer imageId, ImageEntity updatedImage);

}
