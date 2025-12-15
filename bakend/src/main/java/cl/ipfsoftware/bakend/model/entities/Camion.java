package cl.ipfsoftware.bakend.model.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Table(name = "camion")
@Data
public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_camion")
    private Integer idCamion;

    @Column(name = "patente", unique = true)
    private String patente;

    @OneToMany(mappedBy = "camion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos;

}