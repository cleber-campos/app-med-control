package app.repositories;

import app.MockDataTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
//@SpringBootTest
@Transactional
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MockDataTest mockDataTest;

    @Test
    @DisplayName("Cenario 1: Deve retornar um medico aleatorio")
    void BuscarMedicoAleatorioCenario1() {
        //given
        var proximaSegundaAs10 = LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        //when
        var medicoLivre = medicoRepository.BuscarMedicoAleatorio(proximaSegundaAs10);
        // then
        assertThat(medicoLivre).isPresent();
    }

    @Test
    @DisplayName("Cenario 2: Deve retornar empy, pois o medico tem agendamento")
    void MedicoDisponivelNaDataCenario2() {
        var proximaSegundaAs10 = LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        var medico = mockDataTest.gerarMedicoAtivo("Medico1");
        var paciente = mockDataTest.gerarPacienteAtivo("Paciente1");
        mockDataTest.gerarConsultaAtiva(medico, paciente, proximaSegundaAs10);

        var medicoDisponivel = medicoRepository.MedicoDisponivelNaData(
                medico.getId(), proximaSegundaAs10);

        assertThat(medicoDisponivel).isEmpty();
    }

    @Test
    @DisplayName("Cenario 3: Deve retornar o medico disponivel e sem agendamento")
    void MedicoDisponivelNaDataCenario3() {
        var proximaSegundaAs10 = LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        var medico = mockDataTest.gerarMedicoAtivo("Medico1");
        var paciente = mockDataTest.gerarPacienteAtivo("Paciente1");
        var medicoDisponivel = medicoRepository.MedicoDisponivelNaData(
                medico.getId(), proximaSegundaAs10);
        assertThat(medicoDisponivel).isPresent();
    }

}