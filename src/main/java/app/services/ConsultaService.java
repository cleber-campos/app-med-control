package app.services;

import app.dtos.PageDTO;
import app.dtos.consulta.ConsultaRequestCancelDTO;
import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.dtos.consulta.ConsultaRequestUpdateDTO;
import app.dtos.consulta.ConsultaResponseDTO;
import app.mappers.ConsultaMapper;
import app.models.Consulta;
import app.repositories.ConsultaRepository;
import app.validations.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ConsultaMapper consultaMapper;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private List<ValicadaoConsulta> validadores;


    @Transactional
    public ConsultaResponseDTO criarConsulta(ConsultaRequestCreateDTO request) {
        Consulta consulta = consultaMapper.toEntity(request);
        validarRegrasDeNegocio(request);
        var medico = request.idMedico() == null ?
                medicoService.buscarMedicoAleatorio(request.dataConsulta()) :
                medicoService.obterMedicoPorId(request.idMedico());
        consulta.setMedico(medico);
        var paciente = pacienteService.obterPacientePorId(request.idPaciente());
        consulta.setPaciente(paciente);
        var consultaCriada = consultaRepository.save(consulta);
        return consultaMapper.toResponseDTO(consultaCriada);
    }

    @Transactional
    public ConsultaResponseDTO atualizarConsulta(Long id, ConsultaRequestCreateDTO request) {
        var consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada."));
        var dataConsulta = request.dataConsulta() != null
                ? request.dataConsulta() : consulta.getDataConsulta();
        var idMedico = request.idMedico() != null
                ? request.idMedico() : consulta.getMedico().getId();
       validarRegrasEspecificas(request, HorarioFuncionamento.class);
       validarRegrasEspecificas(request, AntecedenciaMinima.class);
       validarRegrasEspecificas(request, DisponibilidadeMedico.class);

        consulta.setDataConsulta(dataConsulta);
        consulta.setMedico(medicoService.obterMedicoPorId(idMedico));
        var consultaAlterada = consultaRepository.save(consulta);
        return consultaMapper.toResponseDTO(consultaAlterada);
    }

    @Transactional
    public void cancelarConsulta(Long id, ConsultaRequestCancelDTO request) {
        if (id == null) throw new IllegalArgumentException("O ID da consulta não pode ser nulo.");
        var consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada."));
        consulta.setMotivoCancelamento(request.motivoCancelamento());
        consulta.setStatus(false);
        consultaRepository.save(consulta);
    }

    public PageDTO<ConsultaResponseDTO> listarConsultas(Pageable paginacao) {
        Page<Consulta> consultas = consultaRepository.findAll(paginacao);
        Page<ConsultaResponseDTO> consultasPageDTO =
                consultas.map(consultaMapper::toResponseDTO );
        return new PageDTO<>(consultasPageDTO);
    }

    public ConsultaResponseDTO obterConsultaPorId(Long id) {
        var consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrado"));
        return consultaMapper.toResponseDTO(consulta);
    }

    private void validarRegrasDeNegocio(ConsultaRequestCreateDTO request) {
        //Design Pattern - Strategy
        validadores.forEach(v -> v.validar(request));
    }

    private void validarRegrasEspecificas(
            ConsultaRequestCreateDTO request,
            Class<? extends ValicadaoConsulta> tipoValidacao) {
        validadores.stream()
                .filter(v -> v.getClass().equals(tipoValidacao))
                .findFirst()
                .ifPresent(v -> v.validar(request));
    }
}