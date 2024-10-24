package med.api.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.api.adapters.integration.paciente.dto.PacienteRequestCreatedDTO;
import med.api.adapters.integration.paciente.dto.PacienteRequestUpdateDTO;

@Entity
@Table(name = "tb_pacientes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    @Column(unique = true)
    private String cpf;
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Endereco endereco;
    private Boolean ativo;

    public Paciente(PacienteRequestCreatedDTO pacienteRequestCreatedDTO) {
        this.nome = pacienteRequestCreatedDTO.nome();
        this.email = pacienteRequestCreatedDTO.email();
        this.telefone = pacienteRequestCreatedDTO.telefone();
        this.cpf = pacienteRequestCreatedDTO.cpf();
        this.endereco = new Endereco(pacienteRequestCreatedDTO.endereco(), this);
        this.ativo = true;
    }

    //passar essas validacoes para a classe PacienteService
    public void atualizaDados(PacienteRequestUpdateDTO pacienteUpdateDTO) {
        {
            if(pacienteUpdateDTO.nome() != null){
                this.nome = pacienteUpdateDTO.nome();
            }
            if(pacienteUpdateDTO.telefone() != null) {
                this.telefone = pacienteUpdateDTO.telefone();
            }
            if(pacienteUpdateDTO.endereco() != null) {
                this.endereco.atualizaDadosEndereco(pacienteUpdateDTO.endereco());
            }
        }
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

    public String getCpf() {
        return cpf;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean status) {
        this.ativo = status;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
