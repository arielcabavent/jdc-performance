package mx.upiicsa.jdc.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AutoDto {
    private Integer idAuto;
    private String marca;
    private String modelo;
    private BigDecimal precio;
    private String color;
    private String imagenUrl;
    private String estado;
}
