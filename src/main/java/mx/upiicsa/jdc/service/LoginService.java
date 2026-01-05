package mx.upiicsa.jdc.service;

import io.vavr.control.Either;
import mx.upiicsa.jdc.dto.EmpleadoDto;
import mx.upiicsa.jdc.dto.LoginDto;
import mx.upiicsa.jdc.dto.SesionDto;
import mx.upiicsa.jdc.dto.SigninDto;

public interface LoginService {
    Either<Integer, SesionDto> login(LoginDto login);
    Either<String, Boolean> signin(SigninDto signinDto);
    Either<String, Boolean> registrarEmpleado(EmpleadoDto dto);
}