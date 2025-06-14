drop database if exists reservasProyecto;
create database reservasProyecto;
use reservasProyecto;

-- Crear la tabla de Hoteles
CREATE TABLE hotel (
    hotel_id INT PRIMARY KEY AUTO_INCREMENT, 
    nombre VARCHAR(100),
    direccion VARCHAR(255)
);

-- Crear la tabla de Habitaciones
CREATE TABLE habitacion (
    habitacion_id INT PRIMARY KEY AUTO_INCREMENT,
    hotel_id INT,
    numero_habitacion INT,
    tipo VARCHAR(50), -- Individual, Doble, Triple, Suite
    precio DECIMAL(10, 2),
    disponible BOOLEAN,
    FOREIGN KEY (hotel_id) REFERENCES hotel(hotel_id)
);

-- Crear la tabla de Reservas
CREATE TABLE reserva (
    reserva_id INT PRIMARY KEY auto_increment,
    usuario_id INT,
    habitacion_id INT,
    fecha_inicio DATE,
    fecha_fin DATE,
    estado VARCHAR(20), -- Puede ser "Pendiente", "Confirmada" o "Cancelada".
    FOREIGN KEY (habitacion_id) REFERENCES habitacion(habitacion_id)
);

INSERT INTO hotel (hotel_id, nombre, direccion)
VALUES
  (1, 'Hotel A', 'Calle Principal 123'),
  (2, 'Hotel B', 'Avenida Secundaria 456'),
  (3, 'Hotel C', 'Plaza Central 789');

-- Insertar datos en la tabla Habitacion
INSERT INTO habitacion (habitacion_id, hotel_id, numero_habitacion, tipo, precio, disponible)
VALUES
  (1, 1, 101, 'Individual', 75.00, true),
  (2, 1, 102, 'Doble', 120.00, true),
  (3, 2, 103, 'Suite', 200.00, false),
  (4, 3, 104,  'Individual', 80.00, true),
  (5, 3, 105,  'Doble', 130.00, true);

-- Insertar datos en la tabla Reserva
INSERT INTO reserva (usuario_id, habitacion_id, fecha_inicio, fecha_fin, estado)
VALUES
  (1, 1, '2024-02-15', '2024-02-20', 'Confirmada'),
  (2, 2, '2024-03-10', '2024-03-15', 'Pendiente'),
  (3, 3, '2024-04-01', '2024-04-05', 'Cancelada'),
  (1, 4, '2024-05-15', '2024-05-20', 'Pendiente'),
  (2, 5, '2024-06-01', '2024-06-05', 'Confirmada');