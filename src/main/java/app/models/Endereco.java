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

    @PreUpdate
    public void preUpdate() {
        this.dataHoraAlteracao = LocalDateTime.now();  // Define a data de alteração automaticamente antes de atualizar
    }

    @PrePersist
    public void prePersist() {
        this.dataHoraInclusao = LocalDateTime.now();
    }

}
