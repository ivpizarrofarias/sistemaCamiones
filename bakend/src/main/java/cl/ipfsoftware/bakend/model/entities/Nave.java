package cl.ipfsoftware.bakend.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "nave")
@Data
public class Nave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nave")
    private Integer idNave;

    @Column(name = "nombre_barco")
    private String nombreBarco;

    @Column(name = "numero_viaje")
    private String numeroViaje;
    @Column(name = "linea_naviera")
    private String naviera;
    @OneToMany(mappedBy = "nave",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Movimiento> movimientos;
    @OneToMany(mappedBy = "nave", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Valorizacion> valorizaciones;
}