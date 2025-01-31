package app.models;

import app.dtos.pacientes.PacienteRequestCreateDTO;
import app.dtos.pacientes.PacienteRequestUpdateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String telefone;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;
    @Column(unique = true, nullable = false)
    private String cpf;
    private Boolean status;
    private LocalDateTime dataHoraInclusao;
    private LocalDateTime dataHoraAlteracao;

    public Paciente(PacienteRequestCreateDTO pacienteRequestCreateDTO) {
        this.nome = pacienteRequestCreateDTO.nome();
        this.email = pacienteRequestCreateDTO.email();
        this.telefone = pacienteRequestCreateDTO.telefone();
        this.endereco = new Endereco(pacienteRequestCreateDTO.endereco());
        this.cpf = pacienteRequestCreateDTO.cpf();
        this.status = true;
    }

    public void atualizaDadosPaciente(PacienteRequestUpdateDTO pacienteRequestUpdateDTO) {
        if (pacienteRequestUpdateDTO.nome() != null) {
            this.nome = pacienteRequestUpdateDTO.nome();
        }
        if (pacienteRequestUpdateDTO.telefone() != null) {
            this.telefone = pacienteRequestUpdateDTO.telefone();
        }
        if (pacienteRequestUpdateDTO.endereco() != null) {
            this.endereco.atualizaDadosEndereco(pacienteRequestUpdateDTO.endereco());
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.dataHoraAlteracao = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.dataHoraInclusao = LocalDateTime.now();
    }

}

