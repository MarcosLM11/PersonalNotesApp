package com.marcos.personalNotesWebApplication.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration for web-related settings including pagination defaults.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        
        // Configuración por defecto para paginación
        pageableResolver.setPageParameterName("page");
        pageableResolver.setSizeParameterName("size");
        pageableResolver.setFallbackPageable(PageRequest.of(0, 10));
        pageableResolver.setMaxPageSize(100); // Limitar tamaño máximo de página
        
        resolvers.add(pageableResolver);
    }
}
