package deber.reservas.services;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import deber.reservas.dtos.UsuarioDTO;
import deber.reservas.entities.Hotel;
import deber.reservas.exceptions.CustomExceptions.*;
import deber.reservas.repositories.HotelRepository;


@Service
public class HotelService {

    @Autowired
    private HotelRepository repository;
    private final Class<Hotel> entityClass = Hotel.class;


    private Map<String, String> modifyKeyRequest(Map<String,String> request, String keyOld, String keyNew){
        String valor = request.get(keyOld);
        request.remove(keyOld);
        request.put(keyNew, valor);
        return request;
    }

    //CREAR Hotel
    //Retorna la entity nueva
    public String createEntity(Map<String,String> request) {
        try {
            request.remove("nombre");
            request = modifyKeyRequest(request, "nombreHotel", "nombre");
            Hotel entity = entityClass.getDeclaredConstructor().newInstance();
            mapToEntity(request, entity);
            repository.save(entity);
            return "Hotel creada con exito";
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | OptimisticLockingFailureException e) {
            throw new RuntimeException("Error actualizando la entidad " + entityClass.getClass().getSimpleName(), e);
        }
    }

    //ACTUALIZAR Hotel
    public String updateEntity(Hotel entity, Map<String,String> request){
        try{
            request.remove("nombre");
            request = modifyKeyRequest(request, "nombreHotel", "nombre");
            mapToEntity(request, entity);
            repository.save(entity);
        }catch(OptimisticLockingFailureException e){
            throw new RuntimeException("Error actualizando la entidad " + entityClass.getClass().getSimpleName(), e);
        }
        return "Actualizado el usuario con exito";
    }
    

    //ELIMINAR Hotel
    public String deleteEntity(Hotel entity){
        repository.delete(entity);
        return entityClass.getSimpleName() + " eliminado con exito";
    }

    //Sobreescribre el valor de Hotel a partir de los valores del Request
    //Si un campo no esta en el Request y no es NonNull entonces le pone Null al valor de la Entity
    protected void mapToEntity(Map<String, String> request, Hotel entity) {
        try {
            for (Field entityField : entityClass.getDeclaredFields()) {
                entityField.setAccessible(true);
                String fieldName = entityField.getName();
    
                if (!fieldName.equals("id") && !request.containsKey(fieldName)) {
                    entityField.set(entity, null);
                    continue;
                }

                // Si el campo está en el request, convertir el valor y asignarlo
                if (request.containsKey(fieldName)) {
                    String fieldValue = request.get(fieldName);
                    Object convertedValue = convertValue(fieldValue, entityField.getType());
                    entityField.set(entity, convertedValue);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error actualizando la entidad " + entity.getClass().getSimpleName(), e);
        }
    }

    //Convierte un String al tipo de dato indicado
    protected Object convertValue(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        } else if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(value);
        } else if (targetType == Double.class || targetType == double.class) {
            return Double.parseDouble(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (targetType == Long.class || targetType == long.class) {
            return Long.parseLong(value);
        } else if (targetType == LocalDateTime.class) {
            return LocalDateTime.parse(value);
        } else if (targetType == LocalDate.class) {
            return LocalDate.parse(value);
        }
        throw new BadRequestException("No se puede convertir el valor '" + value + "' al tipo " + targetType.getSimpleName());
    }

    // Retorna el objeto de la Hotel a partir del ID del objeto, comprueba que existe y sino tira CustomException
    public Hotel getEntityById(int id) {
        Optional<Hotel> entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new NotFoundException("ERROR: " + entityClass.getSimpleName() + " not found by ID");
        }
        return entity.get();
    }
    
    // Retorna el objeto de la Usuario a partir del ID del objeto, comprueba que existe y sino tira CustomException
    public Hotel getEntityByNombre(String nombre) {
        Optional<Hotel> entity = repository.findByNombre(nombre);
        if (entity.isEmpty()) {
            throw new NotFoundException("ERROR: " + entityClass.getSimpleName() + " not found by Nombre");
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
}
