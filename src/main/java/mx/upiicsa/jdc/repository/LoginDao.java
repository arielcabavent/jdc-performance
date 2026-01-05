package mx.upiicsa.jdc.repository;

import io.vavr.control.Either;
import mx.upiicsa.jdc.dto.SesionDto;
import mx.upiicsa.jdc.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LoginDao implements LoginRepository {

    // 1. Los Autowired van AQUÍ, al nivel de la clase
    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private EmpleadoRepository empleadoRepo;
    @Autowired
    private ClienteRepository clienteRepo;

    // 2. Este es el método nuevo que reemplaza al anterior
    @Override
    public Either<Integer, SesionDto> login(String login, String password) {

        // PASO A: Buscamos al usuario por credenciales (SIN importar si está activo o no)
        // Nota: Asegúrate de haber agregado findByLoginAndPassword en UsuarioRepository como te dije antes.
        Optional<Usuario> usuarioOpt = usuarioRepo.findByLoginAndPassword(login, password);

        // ERROR 1: No existe o contraseña mal
        if (usuarioOpt.isEmpty()) {
            return Either.left(1);
        }

        Usuario u = usuarioOpt.get();

        // ERROR 2: Existe, pero está INACTIVO
        if (!u.getActivo()) {
            return Either.left(2);
        }

        // PASO B: Si llegamos aquí, está todo bien. Construimos la sesión.
        SesionDto sesion = null;
        int idRol = u.getRol().getIdRol();

        // --- Lógica para ADMIN ---
        if (idRol == 1) {
            var adminOpt = adminRepo.findByUsuario_IdUsuario(u.getIdUsuario());
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                sesion = SesionDto.builder()
                        .idUsuario(u.getIdUsuario())
                        .nombreCompleto(admin.getNombre() + " " + admin.getApellidoPaterno())
                        .rol("Admin")
                        .correo(u.getLogin())
                        .puesto(admin.getPuesto())
                        .build();
            }
        }
        // --- Lógica para EMPLEADO ---
        else if (idRol == 2) {
            var empOpt = empleadoRepo.findByUsuario_IdUsuario(u.getIdUsuario());
            if (empOpt.isPresent()) {
                Empleado emp = empOpt.get();
                sesion = SesionDto.builder()
                        .idUsuario(u.getIdUsuario())
                        .nombreCompleto(emp.getNombre() + " " + emp.getApellidoPaterno())
                        .rol("Empleado")
                        .correo(u.getLogin())
                        .puesto(emp.getPuesto())
                        .sueldo(emp.getSueldo())
                        .build();
            }
        }
        // --- Lógica para CLIENTE ---
        else if (idRol == 3) {
            Cliente cli = clienteRepo.findByUsuario_IdUsuario(u.getIdUsuario());
            if (cli != null) {
                sesion = SesionDto.builder()
                        .idUsuario(u.getIdUsuario())
                        .nombreCompleto(cli.getNombre() + " " + cli.getApellidoPaterno())
                        .rol("CLIENTE")
                        .idCliente(cli.getIdCliente())
                        .correo(u.getLogin())
                        .build();
            }
        }

        // Validación final por si falló la carga de datos del rol
        if (sesion == null) {
            return Either.left(1);
        }

        return Either.right(sesion);
    }

    // --- Métodos de Guardado (Se quedan igual) ---

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    @Override
    public Cliente saveCliente(Cliente cliente) {
        return clienteRepo.save(cliente);
    }

    @Override
    public Empleado saveEmpleado(Empleado empleado) {
        return empleadoRepo.save(empleado);
    }
}