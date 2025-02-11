package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Page;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Garante a estrutura do Json de saida das APIs
@JsonIgnoreProperties(ignoreUnknown = true)
public record PageDTO<U>(
        List<U> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        Map <String, Object> metadada) {

    public PageDTO(Page<U> page) {
        this(page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                new HashMap<>());
    }
}