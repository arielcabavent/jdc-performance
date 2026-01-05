package mx.upiicsa.jdc.repository;

import mx.upiicsa.jdc.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsuario_IdUsuario(Integer idUsuario);
}
