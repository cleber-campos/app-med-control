package app.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "tb_enderecos")
public class Endereco {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private LocalDateTime dataHoraInclusao;
    private LocalDateTime dataHoraAlteracao;

//    public void atualizaDadosEndereco(EnderecoRequestUpdateDTO endereco) {
//       if(endereco.logradouro() != null){
//           this.logradouro = endereco.logradouro();
//       }
//        if(endereco.numero() != null) {
//            this.numero = endereco.numero();
//        }
//        if(endereco.complemento() != null) {
//            this.complemento = endereco.complemento();
//        }
//        if(endereco.bairro() != null) {
//            this.bairro = endereco.bairro();
//        }
//        if(endereco.cidade() != null) {
//            this.cidade = endereco.cidade();
//        }
//        if(endereco.uf() != null) {
//            this.uf = endereco.uf();
//        }
//        if(endereco.cep() != null) {
//            this.cep = endereco.cep();
//        }
//    }

    @PreUpdate
    public void preUpdate() {
        this.dataHoraAlteracao = LocalDateTime.now();  // Define a data de alteração automaticamente antes de atualizar
    }

    @PrePersist
    public void prePersist() {
        this.dataHoraInclusao = LocalDateTime.now();
    }

}
