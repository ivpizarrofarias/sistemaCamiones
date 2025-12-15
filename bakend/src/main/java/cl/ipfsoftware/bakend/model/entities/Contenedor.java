package cl.ipfsoftware.bakend.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "contenedor")
public class Contenedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contenedor")
    private Integer idContenedor;
    @Column(name = "nombre_Contenedor")
    private String codigoContenedor;
    @OneToMany(mappedBy = "contenedor",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Movimiento> movimientos;
    @OneToMany(mappedBy = "contenedor",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Valorizacion>valorizaciones;
}
