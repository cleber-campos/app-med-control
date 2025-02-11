package app.mappers;

import app.dtos.medicos.MedicoRequestCreateDTO;
import app.dtos.medicos.MedicoRequestUpdateDTO;
import app.dtos.medicos.MedicoResponseDTO;
import app.models.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapperImpl implements MedicoMapper{

    private final EnderecoMapper enderecoMapper;

    public MedicoMapperImpl(EnderecoMapper enderecoMapper) {
        this.enderecoMapper = enderecoMapper;
    }

    @Override
    public Medico toEntity(MedicoRequestCreateDTO requestDTO) {
    return Medico.builder()
            .nome(requestDTO.nome())
            .email(requestDTO.email())
            .telefone(requestDTO.telefone())
            .crm(requestDTO.crm())
            .especialidade(requestDTO.especialidade())
            .endereco(enderecoMapper.toEntity(requestDTO.endereco()))
            .status(true)
            .build();
    }

    @Override
    public Medico updateFromDTO(MedicoRequestUpdateDTO requestDTO) {
        return Medico.builder()
                .nome(requestDTO.nome())
                .telefone(requestDTO.telefone())
                .especialidade(requestDTO.especialidade())
                .endereco(enderecoMapper.updadeFromDTO(requestDTO.endereco()))
                .build();
    }

    @Override
    public MedicoResponseDTO toResponseDTO(Medico medico) {
    return MedicoResponseDTO.builder()
            .id(medico.getId())
            .nome(medico.getNome())
            .email(medico.getEmail())
            .crm(medico.getCrm())
            .especialidade(medico.getEspecialidade())
            .status(medico.getStatus())
            .build();
    }
}
