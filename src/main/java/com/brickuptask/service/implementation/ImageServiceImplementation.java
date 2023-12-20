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
import java.util.List;
import java.util.Optional;
import java.util.Base64;

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

        return saveImageToRepository(image);
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

    private ImageEntity saveImageToRepository(ImageEntity image) {
        logger.info("Salvando imagem no repositório.");
        ImageEntity savedImage = imageRepository.save(image);
        logger.info("Imagem salva com sucesso com ID: {}", savedImage.getImageId());
        return savedImage;
    }

    @Override
    public Optional<ImageEntity> getImageById(Integer imageId) {
        logger.info("Tentando obter uma imagem pelo ID: {}", imageId);

        try {
            Optional<ImageEntity> image = imageRepository.findById(imageId);
            logger.info("Imagem obtida com sucesso.");
            return image;
        } catch (Exception e) {
            logger.error("Erro ao obter a imagem pelo ID: {}", imageId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter a imagem pelo ID: " + imageId, e);
        }
    }


    @Override
    public List<ImageEntity> getImagesByTaskId(Integer taskId) {
        logger.info("Tentando obter imagens pela tarefa com ID: {}", taskId);

        try {
            List<ImageEntity> images = imageRepository.findByTaskTaskId(taskId);
            logger.info("Imagens obtidas com sucesso.");
            return images;
        } catch (Exception e) {
            logger.error("Erro ao obter imagens pela tarefa com ID: {}", taskId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter imagens pela tarefa com ID: " + taskId, e);
        }
    }


    @Override
    public void deleteImage(Integer imageId) {
        logger.info("Tentando excluir uma imagem pelo ID: {}", imageId);

        try {
            imageRepository.deleteById(imageId);
            logger.info("Imagem excluída com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao excluir a imagem pelo ID: {}", imageId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao excluir a imagem pelo ID: " + imageId, e);
        }
    }

    @Override
    public ImageEntity updateImage(Integer imageId, ImageEntity updatedImage) {
        Optional<ImageEntity> existingImageOptional = imageRepository.findById(imageId);
        if (existingImageOptional.isPresent()) {
            ImageEntity existingImage = existingImageOptional.get();

            // Atualize os campos da imagem com os valores do objeto atualizado
            existingImage.setImageData(updatedImage.getImageData());
            // Você pode adicionar mais campos a serem atualizados, se necessário

            // Salve a imagem atualizada no repositório
            return saveImageToRepository(existingImage);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagem não encontrada com o ID: " + imageId);
        }
    }

}
