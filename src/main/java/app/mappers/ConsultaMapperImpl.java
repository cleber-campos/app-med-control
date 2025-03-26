package app.mappers;

import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.dtos.consulta.ConsultaRequestUpdateDTO;
import app.dtos.consulta.ConsultaResponseDTO;
import app.models.Consulta;
import app.models.Medico;
import app.models.Paciente;
import org.springframework.stereotype.Component;

@Component
public class ConsultaMapperImpl implements ConsultaMapper{

    private final MedicoMapper medicoMapper;
    private final PacienteMapper pacienteMapper;

    public ConsultaMapperImpl(MedicoMapper medicoMapper,
                              PacienteMapper pacienteMapper) {
        this.medicoMapper = medicoMapper;
        this.pacienteMapper = pacienteMapper;
    }

    @Override
    public Consulta toEntity(ConsultaRequestCreateDTO requestDTO) {
        return Consulta.builder()
                .paciente(Paciente.builder().id(requestDTO.idPaciente()).build())
                .medico(Medico.builder().id(requestDTO.idMedico()).build())
                .dataConsulta(requestDTO.dataConsulta())
                .status(true)
                .build();
    }

    @Override
    public Consulta updateFromDTO(ConsultaRequestUpdateDTO requestDTO, Medico medico) {
        if(requestDTO == null) return null;
        return Consulta.builder()
                .medico(Medico.builder().id(requestDTO.idMedico()).build())
                .dataConsulta(requestDTO.dataConsulta())
                .build();
    }

    @Override
    public ConsultaResponseDTO toResponseDTO(Consulta consulta) {
        if (consulta == null) return null;
        return ConsultaResponseDTO.builder()
                .id(consulta.getId())
                .dataConsulta(consulta.getDataConsulta())
                .Status(consulta.getStatus())
                .paciente(pacienteMapper.toResponse(consulta.getPaciente()))
                .medico(medicoMapper.toResponseDTO(consulta.getMedico()))
                .build();
    }

}
