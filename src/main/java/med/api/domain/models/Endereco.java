package med.api.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.api.adapters.integration.endereco.EnderecoRequestCreateDTO;

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

    @OneToOne
    @JoinColumn(name = "id_medico")
    private Medico medico;

    @OneToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    public Endereco(EnderecoRequestCreateDTO enderecoRequestCreateDTO, Medico medico){
        this.logradouro = enderecoRequestCreateDTO.logradouro();
        this.numero = enderecoRequestCreateDTO.numero();
        this.complemento = enderecoRequestCreateDTO.complemento();
        this.bairro = enderecoRequestCreateDTO.bairro();
        this.cidade = enderecoRequestCreateDTO.cidade();
        this.uf = enderecoRequestCreateDTO.uf();
        this.cep = enderecoRequestCreateDTO.cep();
        this.medico = medico;
    }

    public Endereco(EnderecoRequestCreateDTO enderecoRequestCreateDTO, Paciente paciente) {
        this.logradouro = enderecoRequestCreateDTO.logradouro();
        this.numero = enderecoRequestCreateDTO.numero();
        this.complemento = enderecoRequestCreateDTO.complemento();
        this.bairro = enderecoRequestCreateDTO.bairro();
        this.cidade = enderecoRequestCreateDTO.cidade();
        this.uf = enderecoRequestCreateDTO.uf();
        this.cep = enderecoRequestCreateDTO.cep();
        this.paciente = paciente;
    }

    public void atualizaDadosEndereco(EnderecoRequestCreateDTO endereco) {
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
}
