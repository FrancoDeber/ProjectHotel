package deber.comentarios.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearComentarioRequestOutput {

    private String nombreHotel;
    private int reservaId;
    private Float puntuacion;
    private String comentario;

    public CrearComentarioRequestOutput(CrearComentarioRequestInput input){
        this.nombreHotel = input.getNombreHotel();
        this.reservaId = input.getReservaId();
        this.puntuacion = input.getPuntuacion();
        this.comentario = input.getComentario();
    }

}
