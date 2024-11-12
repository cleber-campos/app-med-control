package app.domain.medicos.dto;

import app.domain.endereco.EnderecoRequestUpdateDTO;
import app.domain.medicos.model.Especialidade;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicoRequestUpdateDTO(
        @NotNull String nome,
        String telefone,
        Especialidade especialidade,
        EnderecoRequestUpdateDTO endereco) {
}
