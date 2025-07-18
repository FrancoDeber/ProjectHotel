package deber.comentarios.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListarComentariosHotelRequestOutput {

    private String nombreHotel;
    private int reservaId;
    private Float puntuacion;
    private String comentario;
}
