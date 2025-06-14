package deber.reservas.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deber.reservas.dtos.ReservaDTO;
import deber.reservas.entities.Reserva;
import deber.reservas.services.ReservaService;


@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService service;

     //ENDPOINT POST (CARGAR NUEVO OBJETO COMPLETO)
    @PostMapping
    public ResponseEntity<String> crearReserva(
        @RequestBody Map<String,String> requestBody
        ) {
        service.validarUsuarioByRequest(requestBody);
        return ResponseEntity.ok(service.createEntity(requestBody));
    }

    //ENDPOINT PATCH (MODIFICAR PARTES DE UN OBJETO)
    @PatchMapping
    public ResponseEntity<String> cambiarEstado(
        @RequestBody Map<String, String> requestBody
        ) {
        service.validarUsuarioByRequest(requestBody);
        service.checkReservaOfUser(requestBody);
        Reserva entity = service.getEntityById(Integer.parseInt(requestBody.get("reserva_id")));
        return ResponseEntity.ok(service.cambiarEstado(entity,requestBody));
    }

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarReservasUsuario(
        @RequestBody Map<String, String> requestBody
        ) {
        service.validarUsuarioByRequest(requestBody);
        int idUsuario = service.getUsuarioIdByNombre(requestBody.get("nombre"));
        return ResponseEntity.ok(service.listarReservasUsuario(idUsuario, requestBody.get("nombre")));
    }

    @GetMapping("/{estado}")
    public ResponseEntity<List<ReservaDTO>> listarReservasSegunEstado(
        @RequestBody Map<String, String> requestBody,
        @PathVariable String estado
        ) {
        service.validarUsuarioByRequest(requestBody);
        return ResponseEntity.ok(service.listarReservasPorEstado(estado));
    }

    @GetMapping("/check/{idUsuario}-{idHotel}-{idReserva}")
    public ResponseEntity<Boolean> checkReserva(
        @PathVariable int idUsuario,
        @PathVariable int idHotel,
        @PathVariable int idReserva
        ) {
        return ResponseEntity.ok(service.checkReserva(idUsuario, idHotel, idReserva));
    }
}