package mx.upiicsa.jdc.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jdc_usuario")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "activo")
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;

    public String getDescripcionRol() {
        if (rol == null || rol.getNombre() == null) {
            return "Sin Rol";
        }
        return rol.getNombre();
    }

    public Boolean getActivo() {
        return this.activo;
    }
}
