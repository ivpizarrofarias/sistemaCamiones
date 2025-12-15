package cl.ipfsoftware.bakend.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "puerto")
@Data
public class Puerto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puerto")
    private Integer idPuerto;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "puerto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos;

    @OneToMany(mappedBy = "puerto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Valorizacion> valorizaciones;



}