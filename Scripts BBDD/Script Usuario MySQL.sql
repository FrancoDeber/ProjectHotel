drop database if exists usuariosProyecto;
create database usuariosProyecto;

use usuariosProyecto;

CREATE TABLE usuario (
    usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    correo_electronico VARCHAR(255),
    direccion VARCHAR(255),
    contrasena VARCHAR(255)
);

INSERT INTO usuario (nombre, correo_electronico, direccion, contrasena)
VALUES
  ('Deber', 'deber@example.com', 'Calle Principal 123', '123asd'),
  ('Juan Pérez', 'juan@example.com', 'Calle Principal 123', 'clave123'),
  ('María López', 'maria@example.com', 'Avenida Secundaria 456', 'secreto456'),
  ('Carlos Rodriguez', 'carlos@example.com', 'Plaza Central 789', 'password789'),
  ('Ana Martínez', 'ana@example.com', 'Calle del Parque 987', 'abcxyz');