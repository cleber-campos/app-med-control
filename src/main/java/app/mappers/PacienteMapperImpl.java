package app.mappers;

import app.dtos.pacientes.PacienteRequestCreateDTO;
import app.dtos.pacientes.PacienteRequestUpdateDTO;
import app.dtos.pacientes.PacienteResponseDTO;
import app.models.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapperImpl implements PacienteMapper{

    private final EnderecoMapper enderecoMapper;

    public PacienteMapperImpl(EnderecoMapper enderecoMapper) {
        this.enderecoMapper = enderecoMapper;
    }

    @Override
    public Paciente toEntity(PacienteRequestCreateDTO requestDTO) {
        return Paciente.builder()
                .nome(requestDTO.nome())
                .email(requestDTO.email())
                .telefone(requestDTO.telefone())
                .cpf(requestDTO.cpf())
                .endereco(enderecoMapper.toEntity(requestDTO.endereco()))
                .build();
    }

    @Override
    public Paciente updateFromDTO(PacienteRequestUpdateDTO requestDTO) {
        return Paciente.builder()
                .nome(requestDTO.nome())
                .telefone(requestDTO.telefone())
                .endereco(enderecoMapper.updadeFromDTO(requestDTO.endereco()))
                .build();
    }

    @Override
    public PacienteResponseDTO toResponse(Paciente paciente) {
        return PacienteResponseDTO.builder()
                .id(paciente.getId())
                .nome(paciente.getNome())
                .email(paciente.getEmail())
                .cpf(paciente.getCpf())
                .status(paciente.getStatus())
                .build();
    }

}
