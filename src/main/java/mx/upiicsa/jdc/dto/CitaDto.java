package mx.upiicsa.jdc.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaDto {
    private Integer idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private String servicio;
    private String estado;
}
