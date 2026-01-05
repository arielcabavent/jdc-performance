package mx.upiicsa.jdc.repository;

import io.vavr.control.Either;
import mx.upiicsa.jdc.dto.SesionDto;
import mx.upiicsa.jdc.entity.Cliente;
import mx.upiicsa.jdc.entity.Empleado;
import mx.upiicsa.jdc.entity.Usuario;

public interface LoginRepository {
    Either<Integer, SesionDto> login(String login, String password);
    Usuario saveUsuario(Usuario usuario);
    Cliente saveCliente(Cliente persona);
    Empleado saveEmpleado(Empleado empleado);
}
