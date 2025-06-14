package deber.comentarios.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import deber.comentarios.dtos.ComentarioDTO;
import deber.comentarios.entities.CrearComentarioRequestInput;
import deber.comentarios.entities.CrearComentarioRequestOutput;
import deber.comentarios.entities.EliminarComentarioDeUsuarioRequest;
import deber.comentarios.entities.ListarComentariosHotelRequestInput;
import deber.comentarios.entities.ListarComentariosHotelRequestOutput;
import deber.comentarios.entities.ListarComentariosUsuarioRequestInput;
import deber.comentarios.entities.MostrarComentarioUsuarioReservaRequest;
import deber.comentarios.entities.PuntuacionMediaHotelRequest;
import deber.comentarios.entities.PuntuacionesMediasUsuarioRequest;
import deber.comentarios.services.ComentarioService;

@Controller
public class ComentarioController {

    @Autowired ComentarioService service;

    @MutationMapping
    public CrearComentarioRequestOutput crearComentario(@Argument CrearComentarioRequestInput request){
        service.validarUsuario(request.getNombre(), request.getContrasena());
        service.crearComentario(request);
        CrearComentarioRequestOutput output = new CrearComentarioRequestOutput(request);
        return output;
    }

    @MutationMapping
    public String eliminarComentarios(){
        return service.eliminarComentarios();
    }

    @MutationMapping
    public String eliminarComentarioDeUsuario(@Argument EliminarComentarioDeUsuarioRequest request){
        service.validarUsuario(request.getNombre(), request.getContrasena());
        return service.eliminarComentarioUsuario(request);
    }

    @QueryMapping
    public List<ListarComentariosHotelRequestOutput> listarComentariosHotel(@Argument ListarComentariosHotelRequestInput request){
        service.validarUsuario(request.getNombre(), request.getContrasena());
        return service.listarComentariosHotel(request);
    }

    @QueryMapping
    public List<ListarComentariosHotelRequestOutput> listarComentariosUsuario(@Argument ListarComentariosUsuarioRequestInput request){
        service.validarUsuario(request.getNombre(), request.getContrasena());
        return service.listarComentariosUsuario(request);
    }

    @QueryMapping
    public ComentarioDTO mostrarComentarioUsuarioReserva(@Argument MostrarComentarioUsuarioReservaRequest request){
        service.validarUsuario(request.getNombre(), request.getContrasena());
        return service.mostrarComentarioUsuarioReserva(request);
    }

    @QueryMapping
    public Float puntuacionMediaHotel(@Argument PuntuacionMediaHotelRequest request){
        service.validarUsuario(request.getNombre(), request.getContrasena());
        return service.puntuacionMediaHotel(request);
    }

    @QueryMapping
    public Float puntuacionesMediasUsuario(@Argument PuntuacionesMediasUsuarioRequest request){
        service.validarUsuario(request.getNombre(), request.getContrasena());
        return service.puntuacionesMediasUsuario(request);
    }

}
