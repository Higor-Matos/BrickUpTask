package com.brickuptask.service.implementation;

import com.brickuptask.domain.entity.ImageEntity;
import com.brickuptask.domain.entity.TaskEntity;
import com.brickuptask.repository.ImageRepository;
import com.brickuptask.repository.TaskRepository;
import com.brickuptask.service.interfaces.ImageServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImplementation implements ImageServiceInterface {

    private final ImageRepository imageRepository;
    private final TaskRepository taskRepository;
    private final Logger logger = LoggerFactory.getLogger(ImageServiceImplementation.class);

    @Autowired
    public ImageServiceImplementation(ImageRepository imageRepository, TaskRepository taskRepository) {
        this.imageRepository = imageRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public ImageEntity saveImage(ImageEntity image) {
        validateTaskId(image);
        TaskEntity task = findTask(image.getTask().getTaskId());
        image.setTask(task);

        ImageEntity savedImage = saveImageToRepository(image);
        return savedImage;
    }

    private void validateTaskId(ImageEntity image) {
        if (image.getTask() == null || image.getTask().getTaskId() == null) {
            logger.error("ID da tarefa não fornecido.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da tarefa é necessário.");
        }
    }

    private TaskEntity findTask(Integer taskId) {
        logger.info("Buscando tarefa com ID: {}", taskId);
        return taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    logger.error("Tarefa com ID {} não encontrada.", taskId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada");
                });
    }

    private byte[] decodeImageData(byte[] imageData) {
        try {
            String imageDataString = new String(imageData, StandardCharsets.UTF_8);
            logger.info("Decodificando os dados da imagem.");
            return Base64.getDecoder().decode(imageDataString);
        } catch (IllegalArgumentException e) {
            logger.error("Erro na decodificação dos dados da imagem.", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da imagem inválidos.");
        }
    }

    private ImageEntity saveImageToRepository(ImageEntity image) {
        logger.info("Salvando imagem no repositório.");
        ImageEntity savedImage = imageRepository.save(image);
        logger.info("Imagem salva com sucesso com ID: {}", savedImage.getImageId());
        return savedImage;
    }

    @Override
    public Optional<ImageEntity> getImageById(Integer imageId) {
        logger.info("Tentando obter uma imagem pelo ID: " + imageId);

        try {
            Optional<ImageEntity> image = imageRepository.findById(imageId);
            logger.info("Imagem obtida com sucesso.");
            return image;
        } catch (Exception e) {
            logger.error("Erro ao obter a imagem pelo ID: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<ImageEntity> getImagesByTaskId(Integer taskId) {
        logger.info("Tentando obter imagens pela tarefa com ID: " + taskId);

        try {
            List<ImageEntity> images = imageRepository.findByTaskTaskId(taskId);
            logger.info("Imagens obtidas com sucesso.");
            return images;
        } catch (Exception e) {
            logger.error("Erro ao obter imagens pela tarefa com ID: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteImage(Integer imageId) {
        logger.info("Tentando excluir uma imagem pelo ID: " + imageId);

        try {
            imageRepository.deleteById(imageId);
            logger.info("Imagem excluída com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao excluir a imagem pelo ID: " + e.getMessage(), e);
            throw e;
        }
    }
}
