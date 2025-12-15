package cl.ipfsoftware.bakend.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "transporte_terrestre")
@Data
public class TransporteTerrestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transporte")
    private Integer idTransporte;
    @Column(name = "nombre_transportista")
    private String nombreTransportista;

    @OneToMany(mappedBy = "transporteTerrestre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos;

    @OneToMany(mappedBy = "transporteTerrestre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Valorizacion> valorizaciones;

}