package cl.ipfsoftware.bakend.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "viaje")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private Integer idViaje;

    @Column(name = "origen", nullable = false, length = 100)
    private String origen;
  @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL)
    private List<Valorizacion> valorizaciones;
}
