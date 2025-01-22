package app.domain.medicos.dto;

import app.domain.endereco.dto.EnderecoRequestUpdateDTO;
import app.domain.medicos.model.Especialidade;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicoRequestUpdateDTO(
        String nome,
        String telefone,
        Especialidade especialidade,
        EnderecoRequestUpdateDTO endereco) {
}
