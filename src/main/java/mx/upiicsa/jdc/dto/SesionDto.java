package mx.upiicsa.jdc.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder

public class SesionDto {
    private Integer idUsuario;
    private String nombreCompleto;
    private String rol;
    private String correo;


    private String puesto;
    private BigDecimal sueldo;
    private Integer idCliente;
    private Integer idEmpleado;
}
