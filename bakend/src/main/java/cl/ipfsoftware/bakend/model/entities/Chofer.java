package cl.ipfsoftware.bakend.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "chofer")
@Data
public class Chofer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chofer")
    private Integer idChofer;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "rut")
    private String rut;

    @Column(name = "numero_licencia")
    private String numeroLicencia;

    @OneToMany(mappedBy = "chofer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos;



}