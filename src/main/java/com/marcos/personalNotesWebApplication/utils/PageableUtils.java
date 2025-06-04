package com.marcos.personalNotesWebApplication.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PageableUtils {

    // Configuración por defecto para paginación
    public final int MAX_PAGE_SIZE = 100;
    public final String DEFAULT_SORT_FIELD = "createdAt";
    public final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;

    /**
     * Crea un objeto Pageable validando y limitando los parámetros.
     */
    public Pageable createPageable(int page, int size, String sortBy) {
        // Validar página
        int validPage = Math.max(0, page);

        // Limitar y validar tamaño de página
        int validSize = Math.min(Math.max(1, size), MAX_PAGE_SIZE);

        // Validar campo de ordenación
        String validSortBy = StringUtils.hasText(sortBy) ? sortBy : DEFAULT_SORT_FIELD;

        // Crear el sort con dirección por defecto
        Sort sort = Sort.by(DEFAULT_SORT_DIRECTION, validSortBy);

        return PageRequest.of(validPage, validSize, sort);
    }
}
