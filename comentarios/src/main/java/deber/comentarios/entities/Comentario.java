package deber.comentarios.entities;

import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "comentarios")
public class Comentario {
    private ObjectId id;

    private int usuarioId;
    private int hotelId;
    private int reservaId;
    private Float puntuacion;
    private String comentario;
    private String fechaCreacion;
}
