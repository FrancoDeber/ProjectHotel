package deber.reservas.dtos;

import java.time.LocalDate;

import deber.reservas.entities.Reserva;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservaDTO {
    private int habitacion_id;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;

    public ReservaDTO(Reserva reserva){
        this.habitacion_id = reserva.getHabitacion().getId();
        this.fecha_inicio = reserva.getFecha_inicio();
        this.fecha_fin = reserva.getFecha_fin();
    }
}
