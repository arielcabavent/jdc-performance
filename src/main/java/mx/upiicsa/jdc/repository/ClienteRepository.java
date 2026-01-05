package mx.upiicsa.jdc.repository;

import mx.upiicsa.jdc.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Cliente findByUsuario_IdUsuario(Integer idUsuario);
}