package mx.upiicsa.jdc.controller;

import jakarta.servlet.http.HttpSession;
import mx.upiicsa.jdc.dto.EmpleadoDto;
import mx.upiicsa.jdc.dto.SesionDto;
import mx.upiicsa.jdc.dto.AutoDto;
import mx.upiicsa.jdc.service.TallerService;
import mx.upiicsa.jdc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")

public class AdminController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private TallerService tallerService;

    @GetMapping("/alta-empleado")
    public String mostrarFormulario(HttpSession session, Model model) {
        SesionDto sesion = (SesionDto) session.getAttribute("usuarioSesion");
        if(sesion == null || !sesion.getRol().equalsIgnoreCase("ADMIN")){
            return "redirect:/login";
        }
        model.addAttribute("empleadoDto", new EmpleadoDto());
        return "alta-empleado";
    }

    @PostMapping("/alta-empleado")
    public String registrarEmpleado(@ModelAttribute EmpleadoDto empleadoDto, Model model, HttpSession session) {
        SesionDto sesion = (SesionDto) session.getAttribute("usuarioSesion");

        if(sesion == null || !sesion.getRol().equalsIgnoreCase("ADMIN")){
            return "redirect:/login";
        }

        var resultado = loginService.registrarEmpleado(empleadoDto);
        if(resultado.isRight()){
            model.addAttribute("exito", "EMPLEADO AÑADIDO CORRECTAMENTE");
            model.addAttribute("empleadoDto", new EmpleadoDto());
        } else {
            model.addAttribute("error", resultado.getLeft());

        }
        return "alta-empleado";
    }

    @GetMapping("/alta-auto")
    public String mostrarFormularioAuto(HttpSession session, Model model) {
        SesionDto sesion = (SesionDto) session.getAttribute("usuarioSesion");

        if(sesion == null || !sesion.getRol().equalsIgnoreCase("ADMIN")){
            return "redirect:/login";
        }

        model.addAttribute("autoDto", new AutoDto());
        return "alta-auto";
    }

    @PostMapping("/alta-auto")
    public String registrarAuto(@ModelAttribute AutoDto autoDto, Model model, HttpSession session) {
        SesionDto sesion = (SesionDto) session.getAttribute("usuarioSesion");

        if(sesion == null || !sesion.getRol().equalsIgnoreCase("ADMIN")){
            return "redirect:/login";
        }

        var resultado = tallerService.registrarAuto(autoDto);

        if(resultado.isRight()){
            model.addAttribute("exito", "Auto registrado CORRECTAMENTE");
            model.addAttribute("autoDto", new AutoDto());
        }else {
            model.addAttribute("error", resultado.getLeft());
        }
        return "alta-auto";
    }
}
