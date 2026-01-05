package mx.upiicsa.jdc.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data

public class EmpleadoDto {
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String telefono;
    private String correo;
    private String password;
    private String puesto;
    private BigDecimal sueldo;
}
