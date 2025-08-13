package co.edu.sena.arkosystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración para el manejo de recursos web.
 * <p>
 * Esta clase implementa {@link WebMvcConfigurer} para personalizar la configuración
 * de Spring MVC, en particular para exponer el directorio de subida de imágenes
 * como un recurso estático accesible desde la web.
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Registra manejadores de recursos para servir contenido estático.
     * <p>
     * Este método configura un manejador de recursos para que el directorio local
     * `src/main/resources/static/assets/img/uploads/` sea accesible a través de la
     * URL `/assets/img/uploads/`. Esto es necesario para que las imágenes subidas
     * dinámicamente sean servidas correctamente por el servidor.
     * </p>
     *
     * @param registry El registro de manejadores de recursos de Spring.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/img/uploads/**")
                .addResourceLocations("file:src/main/resources/static/assets/img/uploads/");
    }
}