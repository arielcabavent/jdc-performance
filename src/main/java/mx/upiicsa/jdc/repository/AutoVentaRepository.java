package mx.upiicsa.jdc.repository;

import mx.upiicsa.jdc.entity.AutoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AutoVentaRepository extends JpaRepository<AutoVenta, Integer>{
    List<AutoVenta> findByEstado(String estado);
}
