package app.mappers;

import app.dtos.endereco.EnderecoRequestCreateDTO;
import app.dtos.endereco.EnderecoRequestUpdateDTO;
import app.models.Endereco;

public interface EnderecoMapper {

    Endereco toEntity(EnderecoRequestCreateDTO requestDTO);
    Endereco updadeFromDTO(EnderecoRequestUpdateDTO requestDTO);

}
