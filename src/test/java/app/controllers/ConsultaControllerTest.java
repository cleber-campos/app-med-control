package app.controllers;

import app.dtos.consulta.ConsultaRequestCreateDTO;
import app.dtos.consulta.ConsultaResponseDTO;
import app.dtos.medicos.MedicoResponseDTO;
import app.dtos.pacientes.PacienteResponseDTO;
import app.services.ConsultaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<ConsultaRequestCreateDTO> dadosCreateConsultaJson;

    @Autowired
    private JacksonTester<ConsultaResponseDTO> dadosResponseConsultaJson;

    @MockitoBean
    private ConsultaService consultaService;

    //Teste de Unidade
    @Test
    @DisplayName("Cenario 1: Cadastrar Erro 400 para informacoes invalidas")
    @WithMockUser
    void cadastrarResponseHttp400() throws Exception {
        //given
        //when
        var response = mvc.perform(post("/api/consultas"))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Cenario 2: Cadastrar Ret201 para informacoes validas")
    @WithMockUser
    void cadastrarResponseHttp201() throws Exception {
        //given
        var data = LocalDateTime.now().plusHours(12);
        var consultaResponse = ConsultaResponseDTO.builder()
                                .id(null)
                                .dataConsulta(data)
                                .paciente(PacienteResponseDTO.builder().build())
                                .medico(MedicoResponseDTO.builder().build())
                                .build();
        //when
        when(consultaService.criarConsulta(any())).thenReturn(consultaResponse);

        var response = mvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCreateConsultaJson.write(
                                new ConsultaRequestCreateDTO(2L, 5L, data)
                        ).getJson())
                ).andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonEsperado = dadosResponseConsultaJson.write(
                ConsultaResponseDTO.builder()
                        .id(null)
                        .dataConsulta(data)
                        .paciente(PacienteResponseDTO.builder().build())
                        .medico(MedicoResponseDTO.builder().build())
                        .build()
        ).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

}