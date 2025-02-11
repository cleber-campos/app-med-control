package app.dtos.medicos;

import app.models.Especialidade;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MedicoResponseDTO{
        private Long id;
        private String nome;
        private String email;
        private String crm;
        private Especialidade especialidade;
        private Boolean status;
}