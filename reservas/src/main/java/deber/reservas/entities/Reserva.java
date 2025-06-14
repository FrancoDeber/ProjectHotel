package deber.reservas.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserva_id")
    private int id;

    @Column(name = "usuario_id")
    private int usuarioId;

    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    @JsonBackReference
    @ToString.Exclude
    private Habitacion habitacion;

    @Column(name = "fecha_inicio")
    private LocalDate fecha_inicio;

    @Column(name = "fecha_fin")
    private LocalDate fecha_fin;

    @Column(name = "estado")
    private String estado;

    public Reserva(int usuarioId, Habitacion habitacion, LocalDate fecha_inicio, LocalDate fecha_fin, String estado) {
        this.usuarioId = usuarioId;
        this.habitacion = habitacion;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado = estado;
    }
    
    public void setHabitacion(Habitacion habitacion){
        this.habitacion = habitacion;
    }

    @Override
    public String toString(){
        return ("Reserva de habitacion: " + habitacion.getId() + ", fecha de inicio: " + fecha_inicio + ", fecha de fin: " + fecha_fin);
    }

}
