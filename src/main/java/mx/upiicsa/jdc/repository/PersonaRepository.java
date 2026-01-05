package mx.upiicsa.jdc.repository;

import mx.upiicsa.jdc.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer>{
    Optional<Persona> findByUsuario_IdUsuario(Integer idUsuario);
}

//// class PersonaRepository {
///}
