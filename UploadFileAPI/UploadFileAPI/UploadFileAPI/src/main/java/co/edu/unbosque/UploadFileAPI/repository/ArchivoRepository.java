package co.edu.unbosque.UploadFileAPI.repository;

import co.edu.unbosque.UploadFileAPI.entities.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Integer> {

    /*@Query("SELECT a FROM Archivo a WHERE a.nombre = :nombre")*/
    Optional<Archivo> findByNombre(String nombre);
}
