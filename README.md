# Proyecto: Unidad05

**Autor:** De Bernardis Gonzalez Franco

## Microservicios:
- Comentarios  
- EurekaServer  
- Reservas  
- Usuarios  
- Gateway

## Base de datos:
- **MySQL:** Usuarios / Reservas  
- **MongoDB:** Comentarios

Los scripts de las BBDD estarán en la carpeta **"Scripts BBDD"** en la raíz del proyecto.

---

## Comentarios (IP: `localhost:8503`)

Este microservicio implementa una API accesible desde `localhost:8503/comentarios` o, a través del gateway, desde `localhost:8080/comentarios`. Permite realizar consultas y peticiones con **GraphQL**. Se conecta a la base de datos `comentariosProyecto` y a la colección `comentarios`.

### Mutations:
- `crearComentarios(nombre, contrasena, nombreHotel, reservaID, puntuacion, comentario)`
- `eliminarComentarios()`
- `eliminarComentarioDeUsuario(nombre, contrasena, comentarioId)`

### Queries:
- `listarComentariosUsuario(nombre, contrasena)`
- `mostrarComentarioUsuarioReserva(nombre, contrasena, reservaId)`
- `puntuacionMediaHotel(nombre, contrasena, nombreHotel)`
- `puntuacionMediaUsuario(nombre, contrasena)`

---

## Reservas (IP: `localhost:8501`)

Este microservicio implementa una API accesible desde `localhost:8501/reservas` o desde el gateway `localhost:8080/reservas`. Utiliza **API REST** y se conecta a la base de datos `reservasProyectos`.

### Controladores principales:

#### Reservas (`/reservas`)
- `POST /reservas` → crearReserva  
  **RequestBody:** (nombre, contraseña, fecha_inicio, fecha_fin, habitacion_id)
- `PATCH /reservas` → cambiarEstado  
  **RequestBody:** (nombre, contraseña, reserva_id, estado)
- `GET /reservas` → listarReservasUsuarios  
  **RequestBody:** (nombre, contraseña)
- `GET /reservas/{estado}` → listarReservasSegunEstado  
  **RequestBody:** (nombre, contraseña)
- `GET /reservas/check/{idUsuario}/{idHotel}/{idReserva}` → checkReserva

#### Hoteles (`/reservas/hotel`)
- `POST /reservas/hotel` → crearHotel  
  **RequestBody:** (nombre, contraseña, nombreHotel, direccion)
- `PATCH /reservas/hotel` → actualizarHotel  
  **RequestBody:** (nombre, contraseña, id, nombre, direccion)
- `DELETE /reservas/hotel/{id}` → eliminarHotel  
  **RequestBody:** (nombre, contraseña)
- `POST /reservas/hotel/id/{nombre}` → obtenerIdApartirNombre  
  **RequestBody:** (nombre, contraseña)
- `POST /reservas/hotel/nombre/{id}` → obtenerNombreAPartirId  
  **RequestBody:** (nombre, contraseña)

#### Habitacion (`/reservas/habitacion`)
- `POST /reservas/habitacion` → crearHabitacion  
  **RequestBody:** (nombre, contraseña, numeroHabitacion, tipo, precio, idHotel)
- `PATCH /reservas/habitacion` → actualizarHabitacion  
  **RequestBody:** (nombre, contraseña, id, numeroHabitacion, tipo, precio, idHotel, disponible)
- `DELETE /reservas/habitacion/{id}`  
  **RequestBody:** (nombre, contraseña)

---

## Usuarios (IP: `localhost:8502`)

Este microservicio implementa una API accesible desde `localhost:8502/usuarios` o desde el gateway `localhost:8080/usuarios`. Utiliza **API REST** y se conecta a la base de datos `usuariosProyecto`.

### Usuarios (`/usuarios`)
- `POST /usuarios/registrar` → crearUsuario  
  **RequestBody:** (nombre, correo_electronico, direccion, contraseña)
- `PUT /usuarios/registrar` → actualizarUsuario  
  **RequestBody:** (id, nombre, correo_electornico, direccion, contraseña)
- `DELETE /usuarios` → eliminarUsuario  
  **RequestBody:** (nombre, contraseña)
- `POST /usuarios/validar` → validarUsuario  
  **RequestBody:** (nombre, contraseña)
- `GET /usuarios/info/id/{id}` → obtenerInfoUsuarioPorId
- `GET /usuarios/info/nombre/{nombre}` → obtenerInfoUsuarioPorNombre
- `GET /usuarios/checkIfExist/{id}` → checkIfExist

---

## Gateway (IP: `localhost:8080`)

Este microservicio funciona como un **Gateway** para redireccionar las peticiones del puerto 8080 hacia los demás microservicios dependiendo de la URL recibida.

---

## EurekaServer (IP: `localhost:8500`)

Este microservicio registra los demás microservicios. Sirve para gestionar y permitir el descubrimiento de los mismos y que puedan comunicarse entre sí.
