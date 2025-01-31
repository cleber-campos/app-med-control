package app.models;

import app.dtos.medicos.MedicoRequestCreateDTO;
import app.dtos.medicos.MedicoRequestUpdateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_medicos")
@Getter
@Setter
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
    }

    public void atualizaDados(MedicoRequestUpdateDTO medicoRequestUpdateDTO) {
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
        this.dataHoraAlteracao = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.dataHoraInclusao = LocalDateTime.now();
    }

}

