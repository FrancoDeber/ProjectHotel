package deber.usuarios.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private int id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "correo_electronico")
    private String correo;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "contrasena")
    private String contraseña;

    public Usuario(String nombre, String correo, String direccion, String contraseña) {
        this.nombre = nombre;
        this.correo = correo;
        this.direccion = direccion;
        this.contraseña = contraseña;
    }
    
}
