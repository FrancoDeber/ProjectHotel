package deber.reservas.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import deber.reservas.dtos.ReservaDTO;
import deber.reservas.dtos.UsuarioDTO;
import deber.reservas.entities.Habitacion;
import deber.reservas.entities.Reserva;
import deber.reservas.exceptions.CustomExceptions.*;
import deber.reservas.repositories.ReservaRepository;

@Service
public class ReservaService {
    
    @Autowired
    private ReservaRepository repository;
    private final Class<Reserva> entityClass = Reserva.class;
    private final Class<ReservaDTO> dtoClass = ReservaDTO.class;

    @Autowired
    HabitacionService habitacionService;
    

    //CREAR Reserva
    //Retorna la entity nueva
    public String createEntity(Map<String,String> request) {
        try {
            Reserva entity = entityClass.getDeclaredConstructor().newInstance();
            entity.setHabitacion(getHabitacionById(Integer.parseInt(request.get("habitacion_id"))));
            entity.setFecha_inicio(LocalDate.parse(request.get("fecha_inicio")));
            entity.setFecha_fin(LocalDate.parse(request.get("fecha_fin")));
            entity.setUsuarioId(getUsuarioIdByNombre(request.get("nombre")));
            entity.setEstado("Confirmada");
            repository.save(entity);
            return "Reserva creada con exito";
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | OptimisticLockingFailureException e) {
            throw new RuntimeException("Error actualizando la entidad " + entityClass.getClass().getSimpleName(), e);
        }
    }

    //ACTUALIZAR Reserva
    public String cambiarEstado(Reserva entity, Map<String,String> request){
        try{
            entity.setEstado(request.get("estado"));
            repository.save(entity);
        }catch(OptimisticLockingFailureException e){
            throw new RuntimeException("Error actualizando la entidad " + entityClass.getClass().getSimpleName(), e);
        }
        return "Actualizado el estado con exito";
    }

    public List<ReservaDTO> listarReservasUsuario(int idUsuario, String nombreUsuario){
        List<Reserva> reservasDeUsuario = repository.findReservasOfUser(idUsuario);
        if(!reservasDeUsuario.isEmpty()){
            List<ReservaDTO> reservasDTO = convertEntityListToDTO(reservasDeUsuario);
            return reservasDTO;
        }else{
            throw new NotFoundException("No se encuentran reservas para el usuario con nombre: " + nombreUsuario);
        }
    }

    public List<ReservaDTO> listarReservasPorEstado(String estado){
        List<Reserva> reservas = repository.findByEstado(estado);
        if(!reservas.isEmpty()){
            List<ReservaDTO> reservasDTO = convertEntityListToDTO(reservas);
            return reservasDTO;
        }else{
            throw new NotFoundException("No se encuentran reservas en estado: " + estado);
        }
    }

    public Boolean checkReserva(int idUsuario, int idHotel, int idReserva){
        List<Reserva> reservas = repository.findReservasOfUserAndHotelAndReserva(idUsuario, idHotel, idReserva);
        if(!reservas.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public void checkReservaOfUser(Map<String,String> request){
        int idUsuario = getUsuarioIdByNombre(request.get("nombre"));
        int idReserva = Integer.parseInt(request.get("reserva_id"));
        repository.findReservasOfUserAndReserva(idUsuario, idReserva).orElseThrow(() -> new NotFoundException("Error: Reserva no encontrada para el usuario indicado"));;
    }


    //CONVERTIR Reserva EN ReservaDTO
    public ReservaDTO convertEntityToDTO(Reserva entity) {
        try {
            // Busca el constructor adecuado para el ReservaDTO
            Constructor<ReservaDTO> constructor = findMatchingConstructor(dtoClass, entity);
            // Crea una nueva instancia del ReservaDTO usando los argumentos
            return constructor.newInstance(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error converting "+  entityClass.getSimpleName() +" to ReservaDTO", e);
        }
    }

    //CONVIERTE UNA LIST<Reserva> A LIST<ReservaDTO>
    public List<ReservaDTO> convertEntityListToDTO(List<Reserva> entityList){
        List<ReservaDTO> entityListDTO = new ArrayList<>();
        for(Reserva entity: entityList){
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

    // Retorna el objeto de la Reserva a partir del ID del objeto, comprueba que existe y sino tira CustomException
    public Reserva getEntityById(int id) {
        Optional<Reserva> entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new NotFoundException("ERROR: " + entityClass.getSimpleName() + " not found by ID");
        }
        return entity.get();
    }

    @SuppressWarnings("null")
    public void validarUsuarioByRequest(Map<String,String> request){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String urlServicio = "http://localhost:8080/usuarios/validar";
            UsuarioDTO usuario = new UsuarioDTO(request.get("nombre"), request.get("contraseña"));
            ResponseEntity<Boolean> responseEntity1 = restTemplate.postForEntity(urlServicio, usuario, Boolean.class);
            if(responseEntity1.getStatusCode().is2xxSuccessful() && responseEntity1.getBody()){
                return;
            }
            throw new NotFoundException("ERROR: Credenciales de usuario incorrectas");
        }catch(RuntimeException e){
            throw new NotFoundException("ERROR: Credenciales de usuario incorrectas");
        }
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

    

    public Habitacion getHabitacionById(int id){
        return habitacionService.getEntityById(id);
    }
}
