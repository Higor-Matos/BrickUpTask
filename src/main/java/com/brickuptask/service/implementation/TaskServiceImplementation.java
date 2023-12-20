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

@Service
public class TaskServiceImplementation implements TaskServiceInterface {
    private final TaskRepository taskRepository;
    private final Logger logger = LoggerFactory.getLogger(TaskServiceImplementation.class);

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
        logger.info("Tentando obter uma tarefa pelo ID: " + taskId);

        try {
            Optional<TaskEntity> task = taskRepository.findById(taskId);

            if (!task.isPresent()) {
                logger.warn("Tarefa não encontrada para o ID: " + taskId);
            }

            return task;
        } catch (Exception e) {
            handleException("Erro ao obter a tarefa pelo ID.", e);
            throw e;
        }
    }

    @Override
    public TaskEntity updateTaskStatus(Integer taskId, TaskEntity.TaskStatus newStatus) {
        logger.info("Tentando atualizar o status da tarefa com ID: " + taskId);

        try {
            Optional<TaskEntity> optionalTask = taskRepository.findById(taskId);
            if (optionalTask.isPresent()) {
                TaskEntity task = optionalTask.get();
                task.setStatus(newStatus);
                TaskEntity updatedTask = taskRepository.save(task);
                logger.info("Status da tarefa atualizado com sucesso.");
                return updatedTask;
            } else {
                logger.warn("Tarefa não encontrada para o ID: " + taskId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada");
            }
        } catch (Exception e) {
            handleException("Erro ao atualizar o status da tarefa.", e);
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
        logger.info("Tentando excluir uma tarefa pelo ID: " + taskId);

        try {
            taskRepository.deleteById(taskId);
            logger.info("Tarefa excluída com sucesso.");
        } catch (Exception e) {
            handleException("Erro ao excluir a tarefa pelo ID.", e);
            throw e;
        }
    }

    private void handleException(String message, Exception e) {
        logger.error(message + " " + e.getMessage(), e);
    }
}
