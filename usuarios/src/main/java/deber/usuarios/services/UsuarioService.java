package deber.usuarios.services;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import deber.usuarios.entities.Usuario;
import deber.usuarios.repositories.UsuarioRepository;
import deber.usuarios.exceptions.CustomExceptions.*;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    private final Class<Usuario> entityClass = Usuario.class;

    // Retorna el objeto de la Usuario a partir del ID del objeto, comprueba que existe y sino tira CustomException
    public Usuario getEntityById(int id) {
        Optional<Usuario> entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new NotFoundException("ERROR: " + entityClass.getSimpleName() + " not found by ID");
        }
        return entity.get();
    }
    
    // Retorna el objeto de la Usuario a partir del ID del objeto, comprueba que existe y sino tira CustomException
    public Usuario getEntityByNombre(String nombre) {
        Optional<Usuario> entity = repository.findByNombre(nombre);
        if (entity.isEmpty()) {
            throw new NotFoundException("ERROR: " + entityClass.getSimpleName() + " not found by Nombre");
        }
        return entity.get();
    }

    public Usuario getEntityByRequestBody(Map<String,String> request, String campoValidador){
        if(campoValidador.equals("id")){
            if (request.containsKey("id")) {
                return getEntityById(Integer.parseInt(request.get("id")));
            }
        }else if(campoValidador.equals("nombre")){
            if (request.containsKey("nombre")) {
                return getEntityByNombre(request.get("nombre"));
            }
        }else{
            throw new RuntimeException("Error: Campor Validador incorrecto");
        }
        throw new NotFoundException("Error: No se encuentra al usuario con el " + campoValidador + " brindado");
    }

    public Boolean checkIfExist(int id){
        Optional<Usuario> entity = repository.findById(id);
        if (entity.isEmpty()) {
            return false;
        }
        return true;
    }

    public Boolean validarUsuario(Map<String,String> request){
        Optional<Usuario> entity = repository.findByNombre(request.get("nombre"));
        if(entity.isEmpty()){
            return false;
        }
        if (entity.get().getContraseña().equals(request.get("contraseña"))) {
            return true;
        }
        return false;
    }

    public void validarUsuarioByRequestBody(Map<String,String> request, String campoValidador){
        if(campoValidador.equals("id")){
            Optional<Usuario> entity = repository.findById(Integer.parseInt(request.get("id")));
            if(entity.isPresent() && entity.get().getContraseña().equals(request.get("contraseña"))) {
                return;
            }
            throw new NotFoundException("Error: Credenciales incorrectas");
        }else if(campoValidador.equals("nombre")){
            Optional<Usuario> entity = repository.findByNombre(request.get("nombre"));
            if(entity.isPresent() && entity.get().getContraseña().equals(request.get("contraseña"))) {
                return;
            }
            throw new NotFoundException("Error: Credenciales incorrectas");
        }else{
            throw new RuntimeException("Error: Campor Validador incorrecto");
        }
    }
    
    private Map<String, String> modifyKeyRequest(Map<String,String> request, String keyOld, String keyNew){
        String valor = request.get(keyOld);
        request.remove(keyOld);
        request.put(keyNew, valor);
        return request;
    }

    //CREAR Usuario
    //Retorna la entity nueva
    public String createEntity(Map<String,String> request) {
        try {
            request = modifyKeyRequest(request, "correo_electronico", "correo");
            Usuario entity = entityClass.getDeclaredConstructor().newInstance();
            mapToEntity(request, entity);
            repository.save(entity);
            return "Creado usuario con exito";
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | OptimisticLockingFailureException e) {
            throw new RuntimeException("Error creando la entidad " + entityClass.getClass().getSimpleName() + e);
        }
    }

    //ACTUALIZAR Usuario
    public String updateEntity(Usuario entity, Map<String,String> request){
        try{
            request = modifyKeyRequest(request, "correo_electronico", "correo");
            mapToEntity(request, entity);
            repository.save(entity);
        }catch(OptimisticLockingFailureException e){
            throw new RuntimeException("Error actualizando la entidad " + entityClass.getClass().getSimpleName(), e);
        }
        return "Actualizado el usuario con exito";
    }
    

    //ELIMINAR Usuario
    public String deleteEntity(Usuario entity){
        repository.delete(entity);
        return entityClass.getSimpleName() + " eliminado con exito";
    }

    public void saveEntity(Usuario entity){
        repository.save(entity);
    }

    //Sobreescribre el valor de Usuario a partir de los valores del Request
    //Si un campo no esta en el Request y no es NonNull entonces le pone Null al valor de la Entity
    protected void mapToEntity(Map<String, String> request, Usuario entity) {
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

}
