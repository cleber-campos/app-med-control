package app.mappers;

import app.dtos.endereco.EnderecoRequestCreateDTO;
import app.dtos.endereco.EnderecoRequestUpdateDTO;
import app.models.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapperImpl implements EnderecoMapper{

    @Override
    public Endereco toEntity(EnderecoRequestCreateDTO requestDTO) {
        return Endereco.builder()
                .logradouro(requestDTO.logradouro())
                .numero(requestDTO.numero())
                .complemento(requestDTO.complemento())
                .bairro(requestDTO.bairro())
                .cidade(requestDTO.cidade())
                .uf(requestDTO.uf())
                .cep(requestDTO.cep())
                .build();
    }

    @Override
    public Endereco updadeFromDTO(EnderecoRequestUpdateDTO requestDTO) {
        return Endereco.builder()
                .logradouro(requestDTO.logradouro())
                .numero(requestDTO.numero())
                .complemento(requestDTO.complemento())
                .bairro(requestDTO.bairro())
                .cidade(requestDTO.cidade())
                .uf(requestDTO.uf())
                .cep(requestDTO.cep())
                .build();
    }

}
