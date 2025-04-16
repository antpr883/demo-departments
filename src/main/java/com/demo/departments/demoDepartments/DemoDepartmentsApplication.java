package com.demo.departments.demoDepartments;

import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class DemoDepartmentsApplication implements WebMvcConfigurer {

	/**
	 * Initialize logging directory to ensure it exists before logging starts
	 */
	private static void initializeLoggingDirectory() {
		// First check Spring's logging.file.path property
		String logDir = System.getProperty("logging.file.path");
		
		// Then try system property logging.dir
		if (logDir == null || logDir.isEmpty()) {
			logDir = System.getProperty("logging.dir");
		}
		
		// Then try environment variable LOG_PATH
		if (logDir == null || logDir.isEmpty()) {
			logDir = System.getenv("LOG_PATH");
		}
		
		// If nothing is set, use ./logs as default
		if (logDir == null || logDir.isEmpty()) {
			logDir = "./logs";
		}
		
		// Create directory if it doesn't exist
		File logDirFile = new File(logDir);
		if (!logDirFile.exists()) {
			boolean created = logDirFile.mkdirs();
			if (created) {
				System.out.println("Created log directory: " + logDirFile.getAbsolutePath());
			} else {
				System.err.println("Failed to create log directory: " + logDirFile.getAbsolutePath());
			}
		}
	}

	public static void main(String[] args) {
//		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
//		dotenv.entries().forEach(entry ->
//			System.setProperty(entry.getKey(), entry.getValue()));
		
		// Initialize logging directory
		initializeLoggingDirectory();
		
		SpringApplication.run(DemoDepartmentsApplication.class, args);
	}
}
