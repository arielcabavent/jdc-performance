package mx.upiicsa.jdc.service;

import io.vavr.control.Either;
import mx.upiicsa.jdc.dto.AutoDto;
import mx.upiicsa.jdc.dto.CitaDto;
import mx.upiicsa.jdc.entity.AutoVenta;
import mx.upiicsa.jdc.entity.Cita;
import mx.upiicsa.jdc.entity.Cliente;
import mx.upiicsa.jdc.repository.AutoVentaRepository;
import mx.upiicsa.jdc.repository.CitaRepository;
import mx.upiicsa.jdc.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TallerServiceImpl implements TallerService {
    @Autowired
    private AutoVentaRepository autoRepository;
    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Either<String, Boolean> registrarAuto(AutoDto autoDto) {
        try {
            AutoVenta auto = AutoVenta.builder()
                    .marca(autoDto.getMarca())
                    .modelo(autoDto.getModelo())
                    .precio(autoDto.getPrecio())
                    .color(autoDto.getColor())
                    .imagenUrl(autoDto.getImagenUrl())
                    .estado("DISPONIBLE")
                    .build();
            autoRepository.save(auto);
            return Either.right(true);
        } catch (Exception e) {
            return Either.left("Error al guardar el auto: " + e.getMessage());
        }
    }

    @Override
    public List<AutoDto> obtenerAutosDisponibles() {
        List<AutoVenta> autosDB = autoRepository.findByEstado("DISPONIBLE");
        List<AutoDto> autosDto = new ArrayList<>();

        for (AutoVenta auto : autosDB) {
            AutoDto dto = new AutoDto();
            dto.setIdAuto(auto.getIdAuto());
            dto.setMarca(auto.getMarca());
            dto.setModelo(auto.getModelo());
            dto.setPrecio(auto.getPrecio());
            dto.setColor(auto.getColor());
            dto.setEstado(auto.getEstado());
            dto.setImagenUrl(auto.getImagenUrl());
            autosDto.add(dto);
        }
        return autosDto;
    }

    @Override
    public Either<String, Boolean> agendarCita(CitaDto dto, Integer idCliente) {
        try {
        Cliente cliente = clienteRepository.findById(idCliente).orElse(null);

        if (cliente == null) {
            return Either.left("El cliente no fue encontrado");
        }

        Cita cita = Cita.builder()
                .fechaCita(dto.getFecha())
                .horaCita(dto.getHora())
                .servicio(dto.getServicio())
                .estado("PENDIENTE")
                .cliente(cliente)
                .build();

        citaRepository.save(cita);
        return Either.right(true);

    } catch(Exception e){
        return Either.left("ERROR al agendar cita: " + e.getMessage());
    }
  }
}
