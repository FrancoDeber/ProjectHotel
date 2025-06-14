package deber.comentarios.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearComentarioRequestInput {

    private String nombre;
    private String contrasena;
    private String nombreHotel;
    private int reservaId;
    private Float puntuacion;
    private String comentario;

}
