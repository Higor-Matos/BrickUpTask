package com.brickuptask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrickUpTaskApplication {

	private static final Logger logger = LoggerFactory.getLogger(BrickUpTaskApplication.class);

	public static void main(String[] args) {
		try {
			logger.info("Iniciando a aplicação...");
			SpringApplication.run(BrickUpTaskApplication.class, args);
			logger.info("Aplicação iniciada com sucesso.");
		} catch (Exception e) {
			logger.error("Erro ao iniciar a aplicação: " + e.getMessage(), e);
		}
	}
}
