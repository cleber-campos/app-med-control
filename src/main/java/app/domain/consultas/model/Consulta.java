package app.domain.consultas.model;

import app.domain.medicos.model.Medico;
import app.domain.pacientes.model.Paciente;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "tb_consultas")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;
    @Column(nullable = false)
    private LocalDateTime dataConsulta;
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataHoraInclusao;
    private LocalDateTime dataHoraAlteracao;
    private MotivoCancelamento motivoCancelamento;
    private Boolean status;

    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataConsulta) {
        this.paciente = paciente;
        this.medico = medico;
        this.dataConsulta = dataConsulta;
        this.dataHoraInclusao = LocalDateTime.now();
        this.status = true;
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

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public MotivoCancelamento getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(MotivoCancelamento motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
