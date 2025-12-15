package cl.ipfsoftware.bakend.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "valorizacion")
public class Valorizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valorizacion")
    private Integer idValorizacion;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "valor", nullable = false)
    private String valor;

    @ManyToOne()
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne()
    @JoinColumn(name = "id_nave", nullable = false)
    private Nave nave;

    @ManyToOne()
    @JoinColumn(name = "id_puerto", nullable = false)
    private Puerto puerto;

    @ManyToOne()
    @JoinColumn(name = "id_viaje", nullable = false)
    private Viaje viaje;

    @ManyToOne()
    @JoinColumn(name = "id_contenedor")
    private Contenedor contenedor;
    @ManyToOne()
    @JoinColumn(name = "id_transporte")
    private TransporteTerrestre transporteTerrestre;

}