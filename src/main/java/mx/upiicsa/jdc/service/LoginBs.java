package mx.upiicsa.jdc.service;

import io.vavr.control.Either;
import mx.upiicsa.jdc.dto.EmpleadoDto;
import mx.upiicsa.jdc.dto.LoginDto;
import mx.upiicsa.jdc.dto.SesionDto;
import mx.upiicsa.jdc.dto.SigninDto;
import mx.upiicsa.jdc.entity.Cliente;
import mx.upiicsa.jdc.entity.Empleado;
import mx.upiicsa.jdc.entity.Rol;
import mx.upiicsa.jdc.entity.Usuario;
import mx.upiicsa.jdc.repository.ClienteRepository;
import mx.upiicsa.jdc.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginBs implements LoginService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public Either<Integer, SesionDto> login(LoginDto loginDto) {
        var resultado = loginRepository.login(loginDto.getLogin(), loginDto.getPassword());

        if (resultado.isRight()) {
            SesionDto sesion = resultado.get();

            if ("Cliente".equalsIgnoreCase(sesion.getRol()) || "CLIENTE".equalsIgnoreCase(sesion.getRol())) {
                Cliente cliente = clienteRepository.findByUsuario_IdUsuario(sesion.getIdUsuario());
                if (cliente != null) {
                    sesion.setIdCliente(cliente.getIdCliente());
                }
            }
            return Either.right(sesion);
        } else {
            return Either.left(resultado.getLeft());
        }
    }

    @Override
    @Transactional
    public Either<String, Boolean> signin(SigninDto signinDto) {
        try {
            if (!signinDto.getPassword().equals(signinDto.getConfirmPassword())) {
                return Either.left("Las contraseñas no coinciden");
            }

            String correo = signinDto.getLogin().toLowerCase();
            if (!correo.matches("^[\\w\\.-]+@(gmail\\.com|hotmail\\.com|outlook\\.com|yahoo\\.com)$")) {
                return Either.left("Error: Correo no válido.");
            }

            Usuario nuevoUsuario = Usuario.builder()
                    .login(signinDto.getLogin())
                    .password(signinDto.getPassword())
                    .activo(true)
                    .rol(Rol.builder().idRol(3).build())
                    .build();

            Usuario usuarioGuardado = loginRepository.saveUsuario(nuevoUsuario);

            Cliente nuevoCliente = Cliente.builder()
                    .nombre(signinDto.getNombre())
                    .apellidoPaterno(signinDto.getPrimerApellido())
                    .apellidoMaterno(signinDto.getSegundoApellido())
                    .telefono(signinDto.getTelefono())
                    .usuario(usuarioGuardado)
                    .build();

            loginRepository.saveCliente(nuevoCliente);
            return Either.right(true);

        } catch (Exception e) {
            e.printStackTrace();
            return Either.left("Error al registrar: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Either<String, Boolean> registrarEmpleado(EmpleadoDto dto) {
        try {
            Usuario nuevoUsuario = Usuario.builder()
                    .login(dto.getCorreo())
                    .password(dto.getPassword())
                    .activo(true)
                    .rol(Rol.builder().idRol(2).build())
                    .build();

            Usuario usuarioGuardado = loginRepository.saveUsuario(nuevoUsuario);

            Empleado nuevoEmpleado = Empleado.builder()
                    .nombre(dto.getNombre())
                    .apellidoPaterno(dto.getPrimerApellido())
                    .apellidoMaterno(dto.getSegundoApellido())
                    .telefono(dto.getTelefono())
                    .puesto(dto.getPuesto())
                    .sueldo(dto.getSueldo())
                    .usuario(usuarioGuardado)
                    .build();

            loginRepository.saveEmpleado(nuevoEmpleado);
            return Either.right(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Either.left("Error al registrar: " + e.getMessage());
        }
    }
}