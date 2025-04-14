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

import java.util.List;

@SpringBootApplication
public class DemoDepartmentsApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry ->
			System.setProperty(entry.getKey(), entry.getValue()));

		ConfigurableApplicationContext context = SpringApplication.run(DemoDepartmentsApplication.class, args);
        
        // Print confirmation message
        System.out.println("\n\n====================================");
        System.out.println("Application started successfully!");
        System.out.println("You can now access Swagger UI at: ");
        System.out.println("  http://localhost:8081/swagger-ui/index.html");
        System.out.println("Or the direct API docs at:");
        System.out.println("  http://localhost:8081/v3/api-docs");
        System.out.println("====================================\n\n");
	}

    /**
     * OpenAPI configuration defining API information directly in the main application class
     * to avoid group configuration issues
     */
    @Bean
    @Primary
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Demo Departments API")
                        .description("REST API for managing departments, persons, contacts, addresses, roles, and permissions")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Demo Team")
                                .email("demo@example.com")
                                .url("https://github.com/demo-departments"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Default Server URL")))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
    }
}
