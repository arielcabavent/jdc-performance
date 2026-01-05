package mx.upiicsa.jdc.controller;

import jakarta.servlet.http.HttpSession;
import mx.upiicsa.jdc.dto.LoginDto;
import mx.upiicsa.jdc.dto.SigninDto;
import mx.upiicsa.jdc.dto.SesionDto;
import mx.upiicsa.jdc.repository.CitaRepository;
import mx.upiicsa.jdc.repository.EmpleadoRepository;
import mx.upiicsa.jdc.service.LoginService;
import mx.upiicsa.jdc.entity.Cita;
import mx.upiicsa.jdc.dto.AutoDto;
import mx.upiicsa.jdc.service.TallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private TallerService tallerService;
    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;

    //inicio de sesion
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    //inicio de sesion
    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginDto loginDto, Model model, HttpSession session) {

        var resultado = loginService.login(loginDto);

        if (resultado.isRight()) {
            SesionDto sesionDto = resultado.get();

            session.setAttribute("usuarioSesion", sesionDto);
            return "redirect:/";


        } else {
            Integer codigoError = resultado.getLeft();


            if (codigoError == 1) {
                model.addAttribute("error", "Usuario y/o contraseña incorrectos");
            } else {
                model.addAttribute("error", "El usuario está inactivo");
            }
            return "login";
        }
    }


    //registrarse
    @GetMapping("/signin")
    public String mostrarSignin(Model model) {
        model.addAttribute("signinDto", new SigninDto());
        return "signin";
    }

    //registrarse
    @PostMapping("/signin")
    public String registrarUsuario(@ModelAttribute SigninDto signinDto, Model model) {
        var resulado = loginService.signin(signinDto);
        if (resulado.isRight()) {
            model.addAttribute("loginDto", new LoginDto());
            model.addAttribute("exito", "Cuenta creada, Inicia sesión.");
            return "login";
        } else {
            model.addAttribute("error", resulado.getLeft());
            return "signin";
        }

            }

    //cerrar sesion
    @PostMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    //ver perfil
    @GetMapping("/welcome")
    public String irAPerfil(HttpSession session, Model model) {
        SesionDto sesion = (SesionDto) session.getAttribute("usuarioSesion");
        if (sesion == null) {
            return "redirect:/";
        }
        if ("Empleado".equalsIgnoreCase(sesion.getRol())) {

            var empleadoOpt = empleadoRepository.findByUsuario_IdUsuario(sesion.getIdUsuario());

            if (empleadoOpt.isPresent()) {
                Integer idEmpleado = empleadoOpt.get().getIdEmpleado();
                List<Cita> citas = citaRepository.findByEmpleado_IdEmpleadoOrderByFechaCitaAscHoraCitaAsc(idEmpleado);
                model.addAttribute("misCitas", citas);
            }
        }
        return "welcome";
    }

    //Catalogo autos
    @GetMapping("catalogo")
    public String mostrarCatalogo(Model model, HttpSession session) {
        List<AutoDto> autos = tallerService.obtenerAutosDisponibles();
        model.addAttribute("autos", autos);
        model.addAttribute("usuarioSesion", session.getAttribute("usuarioSesion"));

        return "catalogo";
    }
}

