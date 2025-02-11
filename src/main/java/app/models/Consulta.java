package app.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "tb_consultas")
public class Consulta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;
    @Column(nullable = false)
    private LocalDateTime dataConsulta;
    private LocalDateTime dataHoraInclusao;
    private LocalDateTime dataHoraAlteracao;
    private MotivoCancelamento motivoCancelamento;
    private Boolean status;

//    public Consulta(Medico medico, Paciente paciente, LocalDateTime dataConsulta) {
//        this.paciente = paciente;
//        this.medico = medico;
//        this.dataConsulta = dataConsulta;
//        this.dataHoraInclusao = LocalDateTime.now();
//    }

    @PreUpdate
    public void preUpdate() {
        this.dataHoraAlteracao = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.dataHoraInclusao = LocalDateTime.now();
    }

}
