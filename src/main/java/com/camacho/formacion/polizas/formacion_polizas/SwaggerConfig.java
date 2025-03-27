package com.camacho.formacion.polizas.formacion_polizas;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "User API", version = "v1", description = "Documentación de la API para gestión de usuarios y pólizas"))
public class SwaggerConfig {
}
