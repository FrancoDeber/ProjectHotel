package deber.usuarios.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import deber.usuarios.entities.Usuario;
import deber.usuarios.services.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/info/id/{id}")
    public ResponseEntity<String> obtenerInfoUsuarioPorId(
        @PathVariable int id
        ) {
        Usuario entity = service.getEntityById(id);
        return ResponseEntity.ok(entity.getNombre());
    }

    @GetMapping("/info/nombre/{nombre}")
    public ResponseEntity<String> obtenerInfoUsuarioPorNombre(
        @PathVariable String nombre
        ) {
        Usuario entity = service.getEntityByNombre(nombre);
        return ResponseEntity.ok(Integer.toString(entity.getId()));
    }

    @GetMapping("/checkIfExist/{id}")
    public ResponseEntity<Boolean> checkIfExist(
        @PathVariable int id
        ) {
        return ResponseEntity.ok(service.checkIfExist(id));
    }

    @PostMapping("/validar")
    public ResponseEntity<Boolean> validarUsuario(
        @RequestBody Map<String,String> requestBody
        ) {
        return ResponseEntity.ok(service.validarUsuario(requestBody));
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> crearUsuario(
        @RequestBody Map<String,String> requestBody
        ) {
        return ResponseEntity.ok(service.createEntity(requestBody));
    }

    @PutMapping("/registrar")
    public ResponseEntity<String> actualizarUsuario(
        @RequestBody Map<String,String> requestBody
        ) {
        Usuario entity = service.getEntityByRequestBody(requestBody, "id");
        return ResponseEntity.ok(service.updateEntity(entity, requestBody));
    }

    @DeleteMapping
    public ResponseEntity<String> eliminarUsuario(
        @RequestBody Map<String, String> requestBody
        ) {
        service.validarUsuarioByRequestBody(requestBody, "nombre");
        Usuario entity = service.getEntityByRequestBody(requestBody, "nombre");
        return ResponseEntity.ok(service.deleteEntity(entity));
    }
}
