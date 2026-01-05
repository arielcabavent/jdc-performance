package mx.upiicsa.jdc.service;

import io.vavr.control.Either;
import mx.upiicsa.jdc.dto.AutoDto;
import mx.upiicsa.jdc.dto.CitaDto;
import java.util.List;

public interface TallerService {
    Either<String, Boolean> registrarAuto(AutoDto autoDto);
    List<AutoDto> obtenerAutosDisponibles();
    Either<String, Boolean> agendarCita(CitaDto citaDto, Integer idCliente);
}
