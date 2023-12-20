package com.brickuptask.presentation.controller;

import com.brickuptask.domain.dto.TaskDTO;
import com.brickuptask.domain.entity.TaskEntity;
import com.brickuptask.domain.response.ApiResponse;
import com.brickuptask.service.interfaces.TaskServiceInterface;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskServiceInterface taskService;
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public TaskController(TaskServiceInterface taskService, ModelMapper modelMapper) {
        this.taskService = taskService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@RequestBody TaskDTO taskDTO) {
        logger.info("Recebida uma solicitação para criar uma nova tarefa.");

        try {
            TaskEntity task = modelMapper.map(taskDTO, TaskEntity.class);
            TaskEntity savedTask = taskService.saveTask(task);
            TaskDTO savedTaskDTO = modelMapper.map(savedTask, TaskDTO.class);
            logger.info("Tarefa criada com sucesso.");
            return ResponseEntity.ok(ApiResponse.success("Tarefa criada com sucesso.", savedTaskDTO));
        } catch (Exception e) {
            logger.error("Erro ao criar a tarefa: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("Erro ao criar a tarefa: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@PathVariable Integer id) {
        logger.info("Recebida uma solicitação para obter uma tarefa pelo ID: {}", id);

        try {
            return taskService.getTaskById(id)
                    .map(task -> ResponseEntity.ok(ApiResponse.success("Tarefa encontrada.", modelMapper.map(task, TaskDTO.class))))
                    .orElseGet(() -> {
                        logger.info("Tarefa não encontrada.");
                        return ResponseEntity.ok(ApiResponse.error("Tarefa não encontrada."));
                    });
        } catch (Exception e) {
            logger.error("Erro ao obter a tarefa: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("Erro ao obter a tarefa: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getAllTasks() {
        logger.info("Recebida uma solicitação para obter todas as tarefas.");

        try {
            List<TaskDTO> tasks = taskService.getAllTasks().stream()
                    .map(task -> modelMapper.map(task, TaskDTO.class))
                    .collect(Collectors.toList());
            logger.info("Tarefas encontradas.");
            return ResponseEntity.ok(ApiResponse.success("Tarefas encontradas.", tasks));
        } catch (Exception e) {
            logger.error("Erro ao obter as tarefas: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("Erro ao obter as tarefas: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTaskStatus(@PathVariable Integer id, @RequestBody TaskDTO updatedTaskDTO) {
        logger.info("Recebida uma solicitação para atualizar o status da tarefa pelo ID: {}", id);

        try {
            TaskEntity updatedTask = taskService.updateTaskStatus(id, updatedTaskDTO.getStatus());
            TaskDTO updatedTaskResponse = modelMapper.map(updatedTask, TaskDTO.class);
            logger.info("Status da tarefa atualizado com sucesso.");
            return ResponseEntity.ok(ApiResponse.success("Status da tarefa atualizado com sucesso.", updatedTaskResponse));
        } catch (ResponseStatusException e) {
            logger.warn("Tarefa não encontrada para o ID: {}", id);
            return ResponseEntity.ok(ApiResponse.error("Tarefa não encontrada."));
        } catch (Exception e) {
            logger.error("Erro ao atualizar o status da tarefa: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("Erro ao atualizar o status da tarefa: " + e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Integer id) {
        logger.info("Recebida uma solicitação para excluir uma tarefa pelo ID: {}", id);

        try {
            taskService.deleteTask(id);
            logger.info("Tarefa excluída com sucesso.");
            return ResponseEntity.ok(ApiResponse.success("Tarefa excluída com sucesso.", null));
        } catch (Exception e) {
            logger.error("Erro ao excluir a tarefa: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("Erro ao excluir a tarefa: " + e.getMessage()));
        }
    }
}
