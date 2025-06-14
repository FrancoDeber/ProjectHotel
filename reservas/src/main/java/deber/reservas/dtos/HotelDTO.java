package deber.reservas.dtos;

import java.util.List;
import java.util.stream.Collectors;

import deber.reservas.entities.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelDTO {
    private int id;
    private String nombre;
    private String direccion;
    private List<HabitacionDTO> habitaciones;

    public HotelDTO(Hotel hotel){
        this.id = hotel.getId();
        this.nombre = hotel.getNombre();
        this.direccion = hotel.getDireccion();
        this.habitaciones = hotel.getHabitaciones() == null ? List.of() :
            hotel.getHabitaciones().stream()
                 .map(HabitacionDTO::new)
                 .collect(Collectors.toList());
    }
}
