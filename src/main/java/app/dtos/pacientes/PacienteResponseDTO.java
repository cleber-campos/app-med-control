package app.dtos.pacientes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PacienteResponseDTO{
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private Boolean status;
}
