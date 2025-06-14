package deber.reservas.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "habitacion")
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habitacion_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @JsonBackReference
    @ToString.Exclude
    private Hotel hotel;

    @Column(name = "numero_habitacion")
    private int numeroHabitacion;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "disponible")
    private boolean disponible;

    @OneToMany(mappedBy = "habitacion", cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType. REMOVE})
    @JsonManagedReference
    private List<Reserva> reservas = new ArrayList<>();

    public Habitacion(Hotel hotel, int numeroHabitacion, String tipo, BigDecimal precio, boolean disponible) {
        this.hotel = hotel;
        this.numeroHabitacion = numeroHabitacion;
        this.tipo = tipo;
        this.precio = precio;
        this.disponible = disponible;
    }

    public void setHotel(Hotel hotel){
        this.hotel = hotel;
    }

    public void setReservaBID(Reserva reserva){
        reservas.add(reserva);
        reserva.setHabitacion(this);
    }

}
