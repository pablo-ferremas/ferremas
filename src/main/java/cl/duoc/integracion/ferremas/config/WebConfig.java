package cl.duoc.integracion.ferremas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                // Servir archivos estáticos de Angular
                registry.addResourceHandler("/assets/**")
                                .addResourceLocations("classpath:/static/assets/")
                                .setCachePeriod(3600);

                // Configuración específica para la carpeta media (fuentes FontAwesome, etc.)
                registry.addResourceHandler("/media/**")
                                .addResourceLocations("classpath:/static/media/")
                                .setCachePeriod(31536000); // Cache de 1 año para fuentes

                // Configuración específica para fuentes en la raíz
                registry.addResourceHandler("*.woff", "*.woff2", "*.ttf", "*.eot", "*.svg")
                                .addResourceLocations("classpath:/static/")
                                .setCachePeriod(31536000); // Cache de 1 año para fuentes

                // Archivos estáticos generales (JS, CSS, imágenes)
                registry.addResourceHandler("*.js", "*.css", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.ico", "*.webp",
                                "*.avif")
                                .addResourceLocations("classpath:/static/")
                                .setCachePeriod(3600); // Cache de 1 hora

                // HTML sin cache para permitir actualizaciones inmediatas
                registry.addResourceHandler("*.html")
                                .addResourceLocations("classpath:/static/")
                                .setCachePeriod(0);

                // Fallback para otros archivos
                registry.addResourceHandler("/**")
                                .addResourceLocations("classpath:/static/")
                                .setCachePeriod(0);
        }

        @Override
        public void configurePathMatch(PathMatchConfigurer configurer) {
                // No es obligatorio, pero ayuda a evitar problemas con rutas sin extensión
        }

        @Override
        public void addViewControllers(@NonNull ViewControllerRegistry registry) {
                // Ruta raíz
                registry.addViewController("/").setViewName("forward:/index.html");

                // Rutas de un nivel (sin parámetros)
                registry.addViewController("/registro").setViewName("forward:/index.html");
                registry.addViewController("/login").setViewName("forward:/index.html");
                registry.addViewController("/ofertas").setViewName("forward:/index.html");
                registry.addViewController("/ventas").setViewName("forward:/index.html");
                registry.addViewController("/bodega").setViewName("forward:/index.html");
                registry.addViewController("/auditor").setViewName("forward:/index.html");
                registry.addViewController("/admin").setViewName("forward:/index.html");

                // Rutas con parámetros
                registry.addViewController("/categoria/{id:[\\w\\-]+}").setViewName("forward:/index.html");
                registry.addViewController("/marca/{id:[\\w\\-]+}").setViewName("forward:/index.html");

                // Regla genérica para rutas de primer nivel que no tenemos listadas
                // explícitamente
                registry.addViewController("/{path:[\\w\\-]+}").setViewName("forward:/index.html");
        }
}