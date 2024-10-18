package med.api.adapters.integration.paciente.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import med.api.adapters.integration.endereco.EnderecoRequestCreateDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PacienteRequestUpdateDTO(
        @NotNull
        Long id,
        @NotNull
        String nome,
        String telefone,
        EnderecoRequestCreateDTO endereco) {
}
