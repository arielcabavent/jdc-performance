package mx.upiicsa.jdc.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jdc_auto_venta")

public class AutoVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auto")
    private Integer idAuto;

    private String marca;
    private String modelo;
    private BigDecimal precio;
    private String color;

    private String estado;
    @Column(name = "imagen_url")
    private String imagenUrl;

}
