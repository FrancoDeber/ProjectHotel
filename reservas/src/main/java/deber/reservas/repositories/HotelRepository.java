package deber.reservas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deber.reservas.entities.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    Optional<Hotel> findById(int id);
    Optional<Hotel> findByNombre(String nombre);
}
