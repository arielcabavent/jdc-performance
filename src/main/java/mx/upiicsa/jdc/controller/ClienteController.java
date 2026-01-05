package mx.upiicsa.jdc.controller;

import jakarta.servlet.http.HttpSession;
import mx.upiicsa.jdc.dto.CitaDto;
import mx.upiicsa.jdc.dto.SesionDto;
import mx.upiicsa.jdc.entity.Cita;
import mx.upiicsa.jdc.entity.Cliente;
import mx.upiicsa.jdc.entity.Empleado;
import mx.upiicsa.jdc.repository.CitaRepository;
import mx.upiicsa.jdc.repository.ClienteRepository;
import mx.upiicsa.jdc.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/agendar-cita")
    public String mostrarFormularioCita(
            @RequestParam(required = false) String servicio,
            HttpSession session,
            Model model) {

        SesionDto sesion = (SesionDto) session.getAttribute("usuarioSesion");
        if (sesion == null) {
            return "redirect:/login";
        }

        CitaDto citaDto = new CitaDto();
        if (servicio != null) {
            citaDto.setServicio(servicio);
        }
        model.addAttribute("citaDto", citaDto);
        return "agendar-cita";
    }

    @PostMapping("/agendar-cita")
    public String procesarCita(
            @ModelAttribute CitaDto citaDto,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        SesionDto usuario = (SesionDto) session.getAttribute("usuarioSesion");
        if (usuario == null) return "redirect:/login";
        String puestoBuscado = citaDto.getServicio();
        if (citaDto.getServicio() != null &&
                (citaDto.getServicio().startsWith("Interés") || citaDto.getServicio().startsWith("Venta"))) {
            puestoBuscado = "Vendedor";
        }
        List<Empleado> disponibles = empleadoRepository.findEmpleadosDisponibles(
                puestoBuscado,
                citaDto.getFecha(),
                citaDto.getHora()
        );

        if (disponibles.isEmpty()) {
            model.addAttribute("error", "Lo sentimos, no hay especialistas (" + puestoBuscado + ") disponibles para esa fecha y hora.");
            return "agendar-cita";
        }
        Empleado empleadoAsignado = disponibles.get(0);
        Cita nuevaCita = new Cita();
        nuevaCita.setFechaCita(citaDto.getFecha());
        nuevaCita.setHoraCita(citaDto.getHora());
        nuevaCita.setServicio(citaDto.getServicio());

        Cliente clienteEncontrado = clienteRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        if (clienteEncontrado == null) {
            model.addAttribute("error", "Error: No se encontró la información del cliente.");
            return "agendar-cita";
        }

        nuevaCita.setCliente(clienteEncontrado);
        nuevaCita.setEmpleado(empleadoAsignado);
        nuevaCita.setEstado("Pendiente");

        citaRepository.save(nuevaCita);

        redirectAttributes.addFlashAttribute("exito", "¡Cita Agendada! Te atenderá nuestro asesor: "
                + empleadoAsignado.getNombre() + " " + empleadoAsignado.getApellidoPaterno());

        return "redirect:/welcome";
    }
}