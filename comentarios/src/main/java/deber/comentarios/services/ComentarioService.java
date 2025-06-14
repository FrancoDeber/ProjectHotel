package deber.comentarios.services;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import deber.comentarios.exceptions.CustomExceptions.*;
import deber.comentarios.dtos.ComentarioDTO;
import deber.comentarios.dtos.UsuarioDTO;
import deber.comentarios.entities.Comentario;
import deber.comentarios.entities.CrearComentarioRequestInput;
import deber.comentarios.entities.EliminarComentarioDeUsuarioRequest;
import deber.comentarios.entities.ListarComentariosHotelRequestInput;
import deber.comentarios.entities.ListarComentariosHotelRequestOutput;
import deber.comentarios.entities.ListarComentariosUsuarioRequestInput;
import deber.comentarios.entities.MostrarComentarioUsuarioReservaRequest;
import deber.comentarios.entities.PuntuacionMediaHotelRequest;
import deber.comentarios.entities.PuntuacionesMediasUsuarioRequest;
import deber.comentarios.entities.UsuarioBodyRequest;
import deber.comentarios.repositories.ComentarioRepository;

@Service
public class ComentarioService {

    @Autowired
    ComentarioRepository repository;

    public void crearComentario(CrearComentarioRequestInput request){
        int idUsuario = getUsuarioIdByNombre(request.getNombre());
        int idHotel = getHotelIdByNombre(request.getNombreHotel(), request.getNombre(), request.getContrasena());
        int idReserva = request.getReservaId();
        if(!checkReserva(idReserva, idUsuario, idHotel)){
            throw new BadRequestException("No puede generar un comentario porque la reserva no existe con los datos brindados");
        }
        if(repository.existsByUsuarioIdAndHotelIdAndReservaId(idUsuario, idHotel, idReserva)){
            throw new BadRequestException("No puede generar un comentario porque ya creo un comentario anteriormente para esta reserva");
        }
        try{
            Comentario comentario = new Comentario();
            comentario.setId(new ObjectId());
            comentario.setUsuarioId(idUsuario);
            comentario.setHotelId(idHotel);
            comentario.setReservaId(request.getReservaId());
            comentario.setPuntuacion(request.getPuntuacion());
            comentario.setComentario(request.getComentario());
            repository.save(comentario);
        }catch(Exception e){
            throw new RuntimeException("Error actualizando la entidad", e);
        }
    }

    public String eliminarComentarios(){
        try{
            repository.deleteAll();
            return "Eliminado todos los comentarios con exito";
        }catch(Exception e){
            throw new RuntimeException("Error eliminado todos los comentarios", e);
        }
    }

    public String eliminarComentarioUsuario(EliminarComentarioDeUsuarioRequest request){
        try{
            if (!ObjectId.isValid(request.getComentarioId())) {
                return "ID de comentario no válido";
            }
            Optional<Comentario> comentario = repository.findById(new ObjectId(request.getComentarioId()));
            int usuarioId = getUsuarioIdByNombre(request.getNombre());
            if(comentario.isPresent()){
                if(comentario.get().getUsuarioId() == usuarioId){
                    repository.deleteById(comentario.get().getId());
                    return "Eliminado comentario con exito";
                }else{
                    return "El comentario no es suyo";
                }
                
            }else{
                return "Comentario no encontrado";
            }
        }catch(Exception e){
            return "Error eliminado comentario del usuario";
        }
    }

    public List<ListarComentariosHotelRequestOutput> listarComentariosHotel(ListarComentariosHotelRequestInput request){
        try{
            int hotelId = getHotelIdByNombre(request.getNombreHotel(),request.getNombre(),request.getContrasena());
            List<Comentario> comentarios = repository.findByHotelId(hotelId);
            if(comentarios.isEmpty()){
                throw new NotFoundException("No se encuentran comentarios para dicho hotel");
            }
            List<ListarComentariosHotelRequestOutput> comentariosOutput = new ArrayList<>();
            for(Comentario comentario : comentarios){
                ListarComentariosHotelRequestOutput newComentario = new ListarComentariosHotelRequestOutput();
                newComentario.setNombreHotel(request.getNombreHotel());
                newComentario.setReservaId(comentario.getReservaId());
                newComentario.setPuntuacion(comentario.getPuntuacion());
                newComentario.setComentario(comentario.getComentario());
                comentariosOutput.add(newComentario);
            }
            return comentariosOutput;
        }catch(Exception e){
            throw new RuntimeException("Error listando comentarios del hotel brindado", e);
        }
    }

    public List<ListarComentariosHotelRequestOutput> listarComentariosUsuario(ListarComentariosUsuarioRequestInput request){
        try{
            List<Comentario> comentarios = repository.findByUsuarioId(getUsuarioIdByNombre(request.getNombre()));
            if(comentarios.isEmpty()){
                throw new NotFoundException("No se encuentran comentarios para dicho hotel");
            }
            List<ListarComentariosHotelRequestOutput> comentariosOutput = new ArrayList<>();
            for(Comentario comentario : comentarios){
                ListarComentariosHotelRequestOutput newComentario = new ListarComentariosHotelRequestOutput();
                newComentario.setNombreHotel(getHotelNombredById(comentario.getHotelId(), request.getNombre(), request.getContrasena()));
                newComentario.setReservaId(comentario.getReservaId());
                newComentario.setPuntuacion(comentario.getPuntuacion());
                newComentario.setComentario(comentario.getComentario());
                comentariosOutput.add(newComentario);
            }
            return comentariosOutput;
        }catch(Exception e){
            throw new RuntimeException("Error listando comentarios del usuario brindado", e);
        }
    }

    public ComentarioDTO mostrarComentarioUsuarioReserva(MostrarComentarioUsuarioReservaRequest request){
        Optional<Comentario> comentario = repository.findByUsuarioIdAndReservaId(getUsuarioIdByNombre(request.getNombre()),request.getReservaId());
        if(comentario.isEmpty()){
            throw new NotFoundException("El comentario seleccionado no es del usuario o no existe");
        }
        try{
            ComentarioDTO newComentario = new ComentarioDTO();
                newComentario.setNombreHotel(getHotelNombredById(comentario.get().getHotelId(), request.getNombre(), request.getContrasena()));
                newComentario.setReservaId(comentario.get().getReservaId());
                newComentario.setPuntuacion(comentario.get().getPuntuacion());
                newComentario.setComentario(comentario.get().getComentario());
            return newComentario;
        }catch(Exception e){
            throw new RuntimeException("Error mostrando comentarios del usuario", e);
        }
    }

    public Float puntuacionMediaHotel(PuntuacionMediaHotelRequest request){
        try{
            int idHotel = getHotelIdByNombre(request.getNombreHotel(), request.getNombre(), request.getContrasena());
            List<Comentario> comentarios = repository.findByHotelId(idHotel);
            return (float) comentarios.stream()
            .mapToDouble(Comentario::getPuntuacion)
            .average()
            .orElse(0.0);
        }catch(Exception e){
            throw new RuntimeException("Error mostrando la puntuacion media del hotel brindado", e);
        }
    }


    public Float puntuacionesMediasUsuario(PuntuacionesMediasUsuarioRequest request){
        try{
            int idUsuario = getUsuarioIdByNombre(request.getNombre());
            List<Comentario> comentarios = repository.findByUsuarioId(idUsuario);
            return (float) comentarios.stream()
                .mapToDouble(Comentario::getPuntuacion)
                .average()
                .orElse(0.0);
        }catch(Exception e){
            throw new RuntimeException("Error mostrando la puntuacion media del usuario", e);
        }
    }

    //CONVERTIR Reserva EN ReservaDTO
    public ComentarioDTO convertEntityToDTO(Comentario entity) {
        try {
            // Busca el constructor adecuado para el ReservaDTO
            Constructor<ComentarioDTO> constructor = findMatchingConstructor(ComentarioDTO.class, entity);
            // Crea una nueva instancia del ReservaDTO usando los argumentos
            return constructor.newInstance(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error converting comentario to ReservaDTO", e);
        }
    }

    //CONVIERTE UNA LIST<Reserva> A LIST<ReservaDTO>
    public List<ComentarioDTO> convertEntityListToDTO(List<Comentario> entityList){
        List<ComentarioDTO> entityListDTO = new ArrayList<>();
        for(Comentario entity: entityList){
            entityListDTO.add(convertEntityToDTO(entity));
        }
        return entityListDTO;
    }

    //Retorna el contructor correcto a partir de los atributos pasados
    @SuppressWarnings("unchecked")
    protected <E> Constructor<E> findMatchingConstructor(Class<E> targetClass, Object... args) {
        for (Constructor<?> constructor : targetClass.getConstructors()) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
    
            // Verificar que los tipos de parámetros coincidan con los argumentos
            if (paramTypes.length == args.length) {
                boolean compatible = true;
                for (int i = 0; i < paramTypes.length; i++) {
                    if (!paramTypes[i].isAssignableFrom(args[i].getClass())) {
                        compatible = false;
                        break;
                    }
                }
                if (compatible) {
                    return (Constructor<E>) constructor;
                }
            }
        }
        throw new BadRequestException("No matching constructor found for " + targetClass.getSimpleName());
    }

    @SuppressWarnings("null")
    public int getUsuarioIdByNombre(String nombre){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String urlServicio = "http://localhost:8080/usuarios/info/nombre/{nombre}";
            ResponseEntity<Integer> responseEntity1 = restTemplate.getForEntity(urlServicio, Integer.class, Map.of("nombre",nombre));
            if(responseEntity1.getStatusCode().is2xxSuccessful() && responseEntity1.getBody() != null){
                return responseEntity1.getBody();
            }
            throw new NotFoundException("ERROR: Al acceder al usuario con el nombre brindado");
        }catch(RuntimeException e){
            throw new NotFoundException("ERROR: Al acceder al usuario con el nombre brindado");
        }
    }

    @SuppressWarnings("null")
    public int getHotelIdByNombre(String nombre, String nombreUsuario, String contrasenaUsuario){
        try{
            RestTemplate restTemplate = new RestTemplate();
            URI url = UriComponentsBuilder
            .fromHttpUrl("http://localhost:8080/reservas/hotel/id/{nombre}")
            .buildAndExpand(nombre)  // Expande la variable 'nombre'
            .toUri();
            UsuarioBodyRequest usuario = new UsuarioBodyRequest(nombreUsuario, contrasenaUsuario);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UsuarioBodyRequest> entity = new HttpEntity<>(usuario, headers);
            ResponseEntity<Integer> responseEntity1 = restTemplate.exchange(url, HttpMethod.POST, entity, Integer.class);
            if(responseEntity1.getStatusCode().is2xxSuccessful() && responseEntity1.getBody() != null){
                return responseEntity1.getBody();
            }
            throw new NotFoundException("ERROR: Al acceder al hotel con el nombre brindado " + nombre);
        }catch(RuntimeException e){
            throw new NotFoundException("ERROR: Al acceder al hotel con el nombre brindado " + e);
        }
    }

    @SuppressWarnings("null")
    public String getHotelNombredById(int hotelId, String nombreUsuario, String contrasenaUsuario){
        try{
            RestTemplate restTemplate = new RestTemplate();
            URI url = UriComponentsBuilder
            .fromHttpUrl("http://localhost:8080/reservas/hotel/nombre/{id}")
            .buildAndExpand(hotelId)  // Expande la variable 'nombre'
            .toUri();
            UsuarioBodyRequest usuario = new UsuarioBodyRequest(nombreUsuario, contrasenaUsuario);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UsuarioBodyRequest> entity = new HttpEntity<>(usuario, headers);
            ResponseEntity<String> responseEntity1 = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if(responseEntity1.getStatusCode().is2xxSuccessful() && responseEntity1.getBody() != null){
                return responseEntity1.getBody();
            }
            throw new NotFoundException("ERROR: Al acceder al hotel con el id brindado " + hotelId);
        }catch(RuntimeException e){
            throw new NotFoundException("ERROR: Al acceder al hotel con el id brindado " + e);
        }
    }

    @SuppressWarnings("null")
    public Boolean checkReserva(int idReserva, int idUsuario, int idHotel){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String urlServicio = "http://localhost:8080/reservas/check/{idUsuario}-{idHotel}-{idReserva}";
            ResponseEntity<Boolean> responseEntity1 = restTemplate.getForEntity(urlServicio, Boolean.class, Map.of("idUsuario",idUsuario,"idHotel", idHotel, "idReserva", idReserva));
            if(responseEntity1.getStatusCode().is2xxSuccessful() && responseEntity1.getBody() != null){
                return responseEntity1.getBody();
            }
            throw new NotFoundException("ERROR: Al checkear la reserva");
        }catch(RuntimeException e){
            throw new NotFoundException("ERROR: Al checkear la reserva");
        }
    }

    @SuppressWarnings("null")
    public void validarUsuario(String nombre, String contrasena){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String urlServicio = "http://localhost:8080/usuarios/validar";
            UsuarioDTO usuario = new UsuarioDTO(nombre, contrasena);
            ResponseEntity<Boolean> responseEntity1 = restTemplate.postForEntity(urlServicio, usuario, Boolean.class);
            if(responseEntity1.getStatusCode().is2xxSuccessful() && responseEntity1.getBody()){
                return;
            }
            throw new NotFoundException("ERROR: Credenciales de usuario incorrectas");
        }catch(RuntimeException e){
            throw new NotFoundException("ERROR: Credenciales de usuario incorrectas");
        }
    }

}
