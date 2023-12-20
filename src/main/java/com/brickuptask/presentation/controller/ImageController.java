package com.brickuptask.presentation.controller;

import com.brickuptask.domain.dto.CreateImageDTO;
import com.brickuptask.domain.dto.ImageDTO;
import com.brickuptask.domain.entity.ImageEntity;
import com.brickuptask.domain.response.ApiResponse;
import com.brickuptask.service.interfaces.ImageServiceInterface;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Base64;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageServiceInterface imageService;
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    public ImageController(ImageServiceInterface imageService, ModelMapper modelMapper) {
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ImageDTO>> createImage(@RequestBody CreateImageDTO createImageDTO) {
        logger.info("Recebida uma solicitação para criar uma nova imagem.");

        byte[] imageData = Base64.getDecoder().decode(createImageDTO.getImageData());
        ImageEntity image = modelMapper.map(createImageDTO, ImageEntity.class);
        image.setImageData(imageData);

        ImageEntity savedImage = imageService.saveImage(image);
        ImageDTO savedImageDTO = modelMapper.map(savedImage, ImageDTO.class);

        logger.info("Imagem criada com sucesso.");
        return ResponseEntity.ok(ApiResponse.success("Imagem criada com sucesso.", savedImageDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ImageDTO>> getImageById(@PathVariable Integer id) {
        logger.info("Recebida uma solicitação para obter uma imagem pelo ID: {}", id);

        try {
            ImageEntity imageEntity = imageService.getImageById(id).orElse(null);

            if (imageEntity != null) {
                ImageDTO imageDTO = modelMapper.map(imageEntity, ImageDTO.class);
                imageDTO.setImageData(Base64.getEncoder().encodeToString(imageEntity.getImageData()));
                return ResponseEntity.ok(ApiResponse.success("Imagem encontrada.", imageDTO));
            } else {
                return ResponseEntity.ok(ApiResponse.error("Imagem não encontrada."));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("Erro ao obter a imagem: " + e.getMessage()));
        }
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<ApiResponse<List<ImageDTO>>> getImagesByTaskId(@PathVariable Integer taskId) {
        logger.info("Recebida uma solicitação para obter imagens pela tarefa ID: {}", taskId);

        try {
            List<ImageEntity> images = imageService.getImagesByTaskId(taskId);
            List<ImageDTO> imageDTOs = images.stream()
                    .map(image -> {
                        ImageDTO imageDTO = modelMapper.map(image, ImageDTO.class);
                        imageDTO.setImageData(Base64.getEncoder().encodeToString(image.getImageData()));
                        return imageDTO;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Imagens encontradas.", imageDTOs));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("Erro ao obter as imagens: " + e.getMessage()));
        }
    }
}
