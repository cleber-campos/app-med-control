package app.mappers;

import app.dtos.usuarios.UsuarioCreateDTO;
import app.dtos.usuarios.UsuarioResponseDTO;
import app.dtos.usuarios.UsuarioUpdateDTO;
import app.models.Usuario;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import java.util.List;

@Mapper(componentModel = "Spring")
public interface UsuarioMapper {


    @Mapping(target = "status", constant = "true") //status true na criacao do usuario
    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    Usuario toEntity(UsuarioCreateDTO usuarioRequest); //criacao do usuario

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateEntity(UsuarioUpdateDTO usuarioRequest,
                        @MappingTarget Usuario usuario); //atualizacao do usuario apenas com dados enviados

    UsuarioResponseDTO toResponseDTO(Usuario usuario); // converte entidade em DTO de saida

    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios); // Lista de usuários

}
