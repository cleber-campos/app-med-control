package app.domain.pacientes.dto;

import app.domain.endereco.EnderecoRequestUpdateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PacienteRequestUpdateDTO(
        @NotNull String nome,
        String telefone,
        EnderecoRequestUpdateDTO endereco) {
}
