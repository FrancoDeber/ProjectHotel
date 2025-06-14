package deber.comentarios.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntuacionMediaHotelRequest {
    private String nombre;
    private String contrasena;
    private String nombreHotel;
}
