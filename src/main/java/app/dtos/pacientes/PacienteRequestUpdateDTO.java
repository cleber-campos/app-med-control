package app.dtos.pacientes;

import app.dtos.endereco.EnderecoRequestUpdateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PacienteRequestUpdateDTO(
        String nome,
        String telefone,
        EnderecoRequestUpdateDTO endereco) {
}
