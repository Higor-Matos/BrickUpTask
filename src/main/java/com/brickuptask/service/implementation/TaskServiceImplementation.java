package com.brickuptask.service.implementation;

import com.brickuptask.domain.entity.TaskEntity;
import com.brickuptask.repository.TaskRepository;
import com.brickuptask.service.interfaces.TaskServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class TaskServiceImplementation implements TaskServiceInterface {
    private final TaskRepository taskRepository;
    private final Logger logger = LoggerFactory.getLogger(TaskServiceImplementation.class);

    private static final String TASK_NOT_FOUND_MESSAGE = "Tarefa não encontrada";

    @Autowired
    public TaskServiceImplementation(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskEntity saveTask(TaskEntity task) {
        logger.info("Tentando salvar uma tarefa.");
        try {
            return taskRepository.save(task);
        } catch (Exception e) {
            handleException("Erro ao salvar a tarefa.", e);
            throw e;
        }
    }

    @Override
    public Optional<TaskEntity> getTaskById(Integer taskId) {
        logger.info("Tentando obter uma tarefa pelo ID: {}", taskId);

        try {
            Optional<TaskEntity> task = taskRepository.findById(taskId);

            if (!task.isPresent()) {
                logger.warn("Tarefa não encontrada para o ID: {}", taskId);
            }

            return task;
        } catch (Exception e) {
            handleException("Erro ao obter a tarefa pelo ID.", e);
            throw e;
        }
    }

    @Override
    public List<TaskEntity> getAllTasks() {
        logger.info("Tentando obter todas as tarefas.");

        try {
            List<TaskEntity> tasks = taskRepository.findAll();
            logger.info("Todas as tarefas obtidas com sucesso.");
            return tasks;
        } catch (Exception e) {
            handleException("Erro ao obter todas as tarefas.", e);
            throw e;
        }
    }

    @Override
    public void deleteTask(Integer taskId) {
        logger.info("Tentando excluir uma tarefa pelo ID: {}", taskId);

        try {
            taskRepository.deleteById(taskId);
            logger.info("Tarefa excluída com sucesso.");
        } catch (Exception e) {
            handleException("Erro ao excluir a tarefa pelo ID.", e);
            throw e;
        }
    }

    // Método genérico para atualizar um campo da tarefa
    private TaskEntity updateTaskField(Integer taskId, Function<TaskEntity, TaskEntity> updateFunction, String errorMessage) {
        logger.info("Tentando atualizar o campo da tarefa com ID: {}", taskId);

        try {
            Optional<TaskEntity> optionalTask = taskRepository.findById(taskId);
            if (optionalTask.isPresent()) {
                TaskEntity task = optionalTask.get();
                TaskEntity updatedTask = updateFunction.apply(task);
                TaskEntity savedTask = taskRepository.save(updatedTask);
                logger.info("Campo da tarefa atualizado com sucesso.");
                return savedTask;
            } else {
                logger.warn("Tarefa não encontrada para o ID: {}", taskId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, TASK_NOT_FOUND_MESSAGE);
            }
        } catch (Exception e) {
            handleException(errorMessage, e);
            throw e;
        }
    }

    @Override
    public TaskEntity updateTaskTitle(Integer taskId, String newTitle) {
        return updateTaskField(taskId, task -> {
            task.setTitle(newTitle);
            return task;
        }, "Erro ao atualizar o título da tarefa.");
    }

    @Override
    public TaskEntity updateTaskDescription(Integer taskId, String newDescription) {
        return updateTaskField(taskId, task -> {
            task.setDescription(newDescription);
            return task;
        }, "Erro ao atualizar a descrição da tarefa.");
    }

    @Override
    public TaskEntity updateTaskStatus(Integer taskId, TaskEntity.TaskStatus newStatus) {
        return updateTaskField(taskId, task -> {
            task.setStatus(newStatus);
            return task;
        }, "Erro ao atualizar o status da tarefa.");
    }

    private void handleException(String message, Exception e) {
        logger.error(message, e);
    }
}
