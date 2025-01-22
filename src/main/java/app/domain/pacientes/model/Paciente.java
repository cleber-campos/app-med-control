package app.domain.pacientes.model;

import app.domain.endereco.model.Endereco;
import app.domain.pacientes.dto.PacienteRequestCreatedDTO;
import app.domain.pacientes.dto.PacienteRequestUpdateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pacientes")
@Getter
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

    public Paciente(PacienteRequestCreatedDTO pacienteRequestCreatedDTO) {
        this.nome = pacienteRequestCreatedDTO.nome();
        this.email = pacienteRequestCreatedDTO.email();
        this.telefone = pacienteRequestCreatedDTO.telefone();
        this.endereco = new Endereco(pacienteRequestCreatedDTO.endereco());
        this.cpf = pacienteRequestCreatedDTO.cpf();
        this.status = true;
        this.dataHoraInclusao = LocalDateTime.now();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

