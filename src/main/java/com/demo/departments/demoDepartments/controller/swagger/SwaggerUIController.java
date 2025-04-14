package com.demo.departments.demoDepartments.controller.swagger;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller providing a custom Swagger UI page that doesn't rely on the grouped API docs
 */
@Controller
public class SwaggerUIController {

    @Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
    private String swaggerUiPath;

    /**
     * Redirect from /api-docs to the API documentation
     */
    @GetMapping("/api-docs")
    public void apiDocs(HttpServletResponse response) throws Exception {
        response.sendRedirect("/v3/api-docs");
    }

    /**
     * Provides a custom Swagger UI page that doesn't use groups
     */
    @GetMapping("/custom-swagger")
    public String customSwaggerUI() {
        return "redirect:" + swaggerUiPath + "?url=/v3/api-docs";
    }
}