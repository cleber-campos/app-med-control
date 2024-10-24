package med.api.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.api.adapters.integration.medico.dto.MedicoRequestCreateDTO;
import med.api.adapters.integration.medico.dto.MedicoRequestUpdateDTO;

@Entity
@Table(name = "tb_medicos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    private String email;
    private String telefone;
    @Column(unique = true, nullable = false)
    private String crm;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    @OneToOne(mappedBy = "medico", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Endereco endereco;
    @Column(nullable = false)
    private Boolean ativo;

    public Medico(MedicoRequestCreateDTO medicoRequestCreateDTO) {
    this.nome = medicoRequestCreateDTO.nome();
    this.email = medicoRequestCreateDTO.email();
    this.telefone = medicoRequestCreateDTO.telefone();
    this.crm = medicoRequestCreateDTO.crm();
    this.especialidade = medicoRequestCreateDTO.especialidade();
    this.endereco = new Endereco(medicoRequestCreateDTO.endereco(), this);
    this.ativo = true;
    }

    public void atualizaDadosMedico(MedicoRequestUpdateDTO medicoRequestUpdateDTO) {
        if(medicoRequestUpdateDTO.nome() != null){
            this.nome = medicoRequestUpdateDTO.nome();
        }
        if(medicoRequestUpdateDTO.telefone() != null) {
            this.telefone = medicoRequestUpdateDTO.telefone();
        }
        if(medicoRequestUpdateDTO.especialidade() != null) {
            this.especialidade = medicoRequestUpdateDTO.especialidade();
        }
        if(medicoRequestUpdateDTO.endereco() != null) {
            this.endereco.atualizaDadosEndereco(medicoRequestUpdateDTO.endereco());
        }
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo( Boolean status) {
        this.ativo = status;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCrm() {
        return crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public Endereco getEndereco() {
        return endereco;
    }

}

