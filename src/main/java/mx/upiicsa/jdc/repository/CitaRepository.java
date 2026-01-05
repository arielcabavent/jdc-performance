package mx.upiicsa.jdc.repository;

import mx.upiicsa.jdc.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer>{
    List<Cita> findByEmpleado_IdEmpleadoOrderByFechaCitaAscHoraCitaAsc(Integer idEmpleado);
}