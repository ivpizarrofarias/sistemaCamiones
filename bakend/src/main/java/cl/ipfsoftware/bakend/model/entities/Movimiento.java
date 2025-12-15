package cl.ipfsoftware.bakend.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")
@Data
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @Column(name = "numero_documento", unique = true)
    private String numeroDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_fisico")
    private EstadoFisico estadoFisico;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tamanio")
    private TipoTamanio tipoTamanio;

    @Column(name = "fecha_hora_entrada", nullable = false)
    @NotNull(message = "La fecha y hora de entrada no pueden ser nulas")
    private LocalDateTime fechaHoraEntrada;

    @Column(name = "fecha_hora_salida", nullable = false)
    @NotNull(message = "La fecha y hora de salida no pueden ser nulas")
    private LocalDateTime fechaHoraSalida;

    // Relaciones con otras entidades

    @ManyToOne
    @JoinColumn(name = "id_camion", nullable = false)
    private Camion camion;

    @ManyToOne
    @JoinColumn(name = "id_chofer", nullable = false)
    private Chofer chofer;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_nave", nullable = false)
    private Nave nave;

    @ManyToOne
    @JoinColumn(name = "id_puerto", nullable = false)
    private Puerto puerto;

    @ManyToOne
    @JoinColumn(name = "id_transporte", nullable = false)
    private TransporteTerrestre transporteTerrestre;

    @ManyToOne
    @JoinColumn(name = "id_contenedor",nullable = false)
    private Contenedor contenedor;
}