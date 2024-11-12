package app.domain.medicos.model;

import app.domain.endereco.Endereco;
import app.domain.medicos.dto.MedicoRequestCreateDTO;
import app.domain.medicos.dto.MedicoRequestUpdateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_medicos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

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
    private String crm;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    private Boolean status;
    private LocalDateTime dataHoraInclusao;
    private LocalDateTime dataHoraAlteracao;

    public Medico(MedicoRequestCreateDTO medicoRequestCreateDTO) {
        this.nome = medicoRequestCreateDTO.nome();
        this.email = medicoRequestCreateDTO.email();
        this.telefone = medicoRequestCreateDTO.telefone();
        this.endereco = new Endereco(medicoRequestCreateDTO.endereco());
        this.crm = medicoRequestCreateDTO.crm();
        this.especialidade = medicoRequestCreateDTO.especialidade();
        this.status = true;
        this.dataHoraInclusao = LocalDateTime.now();
    }

    public void atualizaDadosMedico(MedicoRequestUpdateDTO medicoRequestUpdateDTO) {
        if (medicoRequestUpdateDTO.nome() != null) {
            this.nome = medicoRequestUpdateDTO.nome();
        }
        if (medicoRequestUpdateDTO.telefone() != null) {
            this.telefone = medicoRequestUpdateDTO.telefone();
        }
        if (medicoRequestUpdateDTO.endereco() != null) {
            this.endereco.atualizaDadosEndereco(medicoRequestUpdateDTO.endereco());
        }
        if (medicoRequestUpdateDTO.especialidade() != null) {
            this.especialidade = medicoRequestUpdateDTO.especialidade();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.dataHoraAlteracao = LocalDateTime.now();  // Define a data de alteração automaticamente antes de atualizar
    }

    @PrePersist
    public void prePersist() {
        this.dataHoraInclusao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getCrm() {
        return crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }
}

