package deber.reservas.dtos;

import java.math.BigDecimal;

import deber.reservas.entities.Habitacion;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HabitacionDTO {
    private int id;
    private String hotel;
    private int numeroHabitacion;
    private String tipo;
    private BigDecimal precio;
    private boolean disponible;

    public HabitacionDTO(Habitacion habitacion){
        this.id = habitacion.getId();
        this.hotel = habitacion.getHotel().getNombre();
        this.numeroHabitacion = habitacion.getNumeroHabitacion();
        this.tipo = habitacion.getTipo();
        this.precio = habitacion.getPrecio();
        this.disponible = habitacion.isDisponible();
    }

}
