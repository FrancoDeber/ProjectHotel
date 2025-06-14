package deber.reservas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import deber.reservas.entities.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    Optional<Reserva> findById(int id);
    List<Reserva> findByEstado(String estado);
    @Query("SELECT u FROM Reserva u WHERE u.usuarioId = :usuarioId")
    List<Reserva> findReservasOfUser(@Param("usuarioId") int idUsuario);
    @Query("SELECT u FROM Reserva u WHERE u.usuarioId = :usuarioId and u.habitacion.hotel.id = :hotelId and u.id = :reservaId")
    List<Reserva> findReservasOfUserAndHotelAndReserva(@Param("usuarioId") int idUsuario, @Param("hotelId") int hotelId, @Param("reservaId") int idReserva);
    @Query("SELECT u FROM Reserva u WHERE u.usuarioId = :usuarioId and u.id = :reservaId")
    Optional<Reserva> findReservasOfUserAndReserva(@Param("usuarioId") int idUsuario, @Param("reservaId") int idReserva);
}
