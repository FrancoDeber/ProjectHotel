package deber.reservas.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deber.reservas.entities.Habitacion;
import deber.reservas.services.HabitacionService;

@RestController
@RequestMapping("/reservas/habitacion")
public class HabitacionController {

    @Autowired
    private HabitacionService service;

    //ENDPOINT GET (OBTENER UN OBJETO)
    @PostMapping
    public ResponseEntity<String> crearHabitacion(
        @RequestBody Map<String,String> requestBody
        ) {
        service.validarUsuarioByRequest(requestBody);
        return ResponseEntity.ok(service.createEntity(requestBody));
    }

     //ENDPOINT POST (CARGAR NUEVO OBJETO COMPLETO)
    @PatchMapping
    public ResponseEntity<String> actualizarHabitacion(
        @RequestBody Map<String,String> requestBody
        ) {
        service.validarUsuarioByRequest(requestBody);
        Habitacion habitacion = service.getEntityById(Integer.parseInt(requestBody.get("id")));
        return ResponseEntity.ok(service.updateEntity(habitacion, requestBody));
    }

    //ENDPOINT DELETE (ELIMINAR UN OBJETO)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarHabitacion(
        @RequestBody Map<String,String> requestBody,
        @PathVariable int id
        ) {
        service.validarUsuarioByRequest(requestBody);
        Habitacion entity = service.getEntityById(id);
        return ResponseEntity.ok(service.deleteEntity(entity));
    }
}
