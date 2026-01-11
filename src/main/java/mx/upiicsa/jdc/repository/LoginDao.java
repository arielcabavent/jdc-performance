package mx.upiicsa.jdc.repository;

import io.vavr.control.Either;
import mx.upiicsa.jdc.dto.SesionDto;
import mx.upiicsa.jdc.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LoginDao implements LoginRepository {
    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private EmpleadoRepository empleadoRepo;
    @Autowired
    private ClienteRepository clienteRepo;

    @Override
    public Either<Integer, SesionDto> login(String login, String password) {

        Optional<Usuario> usuarioOpt = usuarioRepo.findByLoginAndPassword(login, password);

        // error 1 no existe o contraseña mal
        if (usuarioOpt.isEmpty()) {
            return Either.left(1);
        }

        Usuario u = usuarioOpt.get();

        // erro 2 existe, pero está f
        if (!u.getActivo()) {
            return Either.left(2);
        }

        SesionDto sesion = null;
        int idRol = u.getRol().getIdRol();

        //  logica para ADMIN
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
        //logica para EMPLEADO
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
        //logica para CLIENTE
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
        if (sesion == null) {
            return Either.left(1);
        }

        return Either.right(sesion);
    }


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