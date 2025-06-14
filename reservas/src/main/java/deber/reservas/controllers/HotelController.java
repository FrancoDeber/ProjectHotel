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

import deber.reservas.entities.Hotel;
import deber.reservas.services.HotelService;

@RestController
@RequestMapping("/reservas/hotel")
public class HotelController {

    @Autowired
    private HotelService service;

    //ENDPOINT GET (OBTENER UN OBJETO)
    @PostMapping
    public ResponseEntity<String> crearHotel(
        @RequestBody Map<String,String> requestBody
        ) {
        service.validarUsuarioByRequest(requestBody);
        return ResponseEntity.ok(service.createEntity(requestBody));
    }

     //ENDPOINT POST (CARGAR NUEVO OBJETO COMPLETO)
    @PatchMapping
    public ResponseEntity<String> actualizarHotel(
        @RequestBody Map<String,String> requestBody
        ) {
        service.validarUsuarioByRequest(requestBody);
        Hotel hotel = service.getEntityById(Integer.parseInt(requestBody.get("id")));
        return ResponseEntity.ok(service.updateEntity(hotel, requestBody));
    }

    //ENDPOINT DELETE (ELIMINAR UN OBJETO)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarHotel(
        @RequestBody Map<String,String> requestBody,
        @PathVariable int id
        ) {
        service.validarUsuarioByRequest(requestBody);
        Hotel entity = service.getEntityById(id);
        return ResponseEntity.ok(service.deleteEntity(entity));
    }

    //ENDPOINT GET (OBTENER UN OBJETO)
    @PostMapping("/id/{nombre}")
    public ResponseEntity<String> obtenerIdApartirNombre(
        @RequestBody Map<String,String> requestBody,
        @PathVariable String nombre
        ) {
        service.validarUsuarioByRequest(requestBody);
        Hotel hotel = service.getEntityByNombre(nombre);
        return ResponseEntity.ok(Integer.toString(hotel.getId()));
    }

    //ENDPOINT GET (OBTENER UN OBJETO)
    @PostMapping("/nombre/{id}")
    public ResponseEntity<String> obtenerNombreAPartirId(
        @RequestBody Map<String,String> requestBody,
        @PathVariable int id
        ) {
        service.validarUsuarioByRequest(requestBody);
        Hotel hotel = service.getEntityById(id);
        return ResponseEntity.ok(hotel.getNombre());
    }
}
