package deber.comentarios.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDTO {
    private String nombreHotel;
    private int reservaId;
    private Float puntuacion;
    private String comentario;
}
