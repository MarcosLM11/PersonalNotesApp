package com.marcos.personalNotesWebApplication.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic DTO for paginated responses.
 * This provides a consistent structure for all paginated endpoints.
 *
 * @param <T> the type of content in the page
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDto<T> {
    
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;
    
    /**
     * Creates a PageResponseDto from a Spring Data Page object.
     *
     * @param page the Spring Data Page
     * @param <T> the type of content
     * @return a PageResponseDto with the same data
     */
    public static <T> PageResponseDto<T> from(Page<T> page) {
        PageResponseDto<T> response = new PageResponseDto<>();
        response.setContent(page.getContent());
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        response.setHasNext(page.hasNext());
        response.setHasPrevious(page.hasPrevious());
        return response;
    }
}
