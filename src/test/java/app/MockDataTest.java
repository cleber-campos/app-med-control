package app;

import app.models.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MockDataTest {

    private final EntityManager entityManager;

    public Medico gerarMedicoAtivo(String nome) {
        var medico = Medico.builder()
                .nome(nome)
                .email("medico@voll.med")
                .telefone("11 99999-7777")
                .crm("123456")
                .especialidade(Especialidade.CARDIOLOGIA)
                .status(true)
                .endereco(gerarEndereco())
                .build();
        entityManager.persist(medico);
        return medico;
    }

    public Paciente gerarPacienteAtivo(String nome) {
        var paciente = Paciente.builder()
                .nome(nome)
                .email("paciente@email.com")
                .telefone("11 99999-8888")
                .cpf("111.111.111-11")
                .status(true)
                .endereco(gerarEndereco())
                .build();
        entityManager.persist(paciente);
        return paciente;
    }

    public Consulta gerarConsultaAtiva(Medico medico, Paciente paciente, LocalDateTime data) {
        var consulta = Consulta.builder()
                .medico(medico)
                .paciente(paciente)
                .dataConsulta(data)
                .status(true)
                .build();
        entityManager.persist(consulta);
        return consulta;
    }

    public Endereco gerarEndereco() {
        return Endereco.builder()
                .logradouro("Rua Teste")
                .numero("100")
                .complemento("Próximo ao teste")
                .bairro("Bairro")
                .cidade("Cidade")
                .uf("UF")
                .cep("00000000")
                .build();
    }
}