package app.domain.endereco;

import app.domain.pacientes.model.Paciente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_enderecos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    //@OneToOne//(mappedBy = "endereco", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //private Paciente paciente;
    private LocalDateTime dataHoraInclusao;
    private LocalDateTime dataHoraAlteracao;

    public Endereco(EnderecoRequestCreateDTO enderecoRequestCreateDTO){
        this.logradouro = enderecoRequestCreateDTO.logradouro();
        this.numero = enderecoRequestCreateDTO.numero();
        this.complemento = enderecoRequestCreateDTO.complemento();
        this.bairro = enderecoRequestCreateDTO.bairro();
        this.cidade = enderecoRequestCreateDTO.cidade();
        this.uf = enderecoRequestCreateDTO.uf();
        this.cep = enderecoRequestCreateDTO.cep();
        this.dataHoraInclusao = LocalDateTime.now(); // Define a data de inclusão na criação
    }


    public void atualizaDadosEndereco(EnderecoRequestUpdateDTO endereco) {
       if(endereco.logradouro() != null){
           this.logradouro = endereco.logradouro();
       }
        if(endereco.numero() != null) {
            this.numero = endereco.numero();
        }
        if(endereco.complemento() != null) {
            this.complemento = endereco.complemento();
        }
        if(endereco.bairro() != null) {
            this.bairro = endereco.bairro();
        }
        if(endereco.cidade() != null) {
            this.cidade = endereco.cidade();
        }
        if(endereco.uf() != null) {
            this.uf = endereco.uf();
        }
        if(endereco.cep() != null) {
            this.cep = endereco.cep();
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

}
