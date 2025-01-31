package app.dtos.usuarios;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonIgnoreProperties
public record UsuarioPageDTO<U>(
        List<UsuarioResponseDTO> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages) {

    public UsuarioPageDTO (Page<UsuarioResponseDTO> usuariosPage) {
        this(usuariosPage.getContent(),
                usuariosPage.getNumber(),
                usuariosPage.getSize(),
                usuariosPage.getTotalElements(),
                usuariosPage.getTotalPages());
    }
}