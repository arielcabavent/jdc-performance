package mx.upiicsa.jdc.repository;

import mx.upiicsa.jdc.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findByUsuario_IdUsuario(Integer idUsuario);

    @Query("SELECT e FROM Empleado e " +
            "WHERE e.puesto = :puesto " +
            "AND e.idEmpleado NOT IN " +
            "(SELECT c.empleado.idEmpleado FROM Cita c WHERE c.fechaCita = :fecha AND c.horaCita = :hora)")
    List<Empleado> findEmpleadosDisponibles(
            @Param("puesto") String puesto,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora);


}





