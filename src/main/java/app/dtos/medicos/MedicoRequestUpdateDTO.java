package app.dtos.medicos;

import app.dtos.endereco.EnderecoRequestUpdateDTO;
import app.models.Especialidade;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicoRequestUpdateDTO(
        String nome,
        String telefone,
        Especialidade especialidade,
        EnderecoRequestUpdateDTO endereco) {
}
