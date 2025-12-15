package cl.ipfsoftware.bakend.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name="nombre", length = 60, nullable = false)
    private String nombre;

    @Column(name="a_paterno", nullable = false, length = 50)
    private String paterno;

    @Column(name = "a_materno", length = 50, nullable = false)
    private String materno;

    @Column(name = "correo", length = 90, nullable = false, unique = true)
    private String correo;

    @Column(name = "clave", length = 150, nullable = false)
    private String clave;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_usuario")
    private RolUsuario rolUsuario;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

}
