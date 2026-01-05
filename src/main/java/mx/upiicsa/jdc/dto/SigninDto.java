package mx.upiicsa.jdc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class SigninDto {
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String telefono;
    private String login;
    private String password;
    private  String confirmPassword;
}
