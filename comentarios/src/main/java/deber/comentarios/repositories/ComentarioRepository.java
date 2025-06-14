package deber.comentarios.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import deber.comentarios.entities.Comentario;

@Repository
public interface ComentarioRepository extends MongoRepository<Comentario, ObjectId> {
    boolean existsByUsuarioIdAndHotelIdAndReservaId(int idUsuario, int idHotel, int idReserva);
    List<Comentario> findByHotelId(int hotelId);
    List<Comentario> findByUsuarioId(int usuarioId);
    Optional<Comentario> findByUsuarioIdAndReservaId(int usuarioId, int reservaId);
}
