package app.domain.pacientes.dto;

import app.domain.endereco.dto.EnderecoRequestUpdateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PacienteRequestUpdateDTO(
        String nome,
        String telefone,
        EnderecoRequestUpdateDTO endereco) {
}
