package com.camacho.formacion.polizas.formacion_polizas.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.camacho.formacion.polizas.formacion_polizas.entities.Poliza;
import com.camacho.formacion.polizas.formacion_polizas.entities.User;
import com.camacho.formacion.polizas.formacion_polizas.repositories.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    UserController() {
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Permite crear un nuevo usuario en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud mal formada")
    })
    public User createUser(@RequestBody User user) {
        Optional<User> optUser = userRepository.findByEmail(user.getEmail());
        if(optUser.isPresent()){
            User userGet = optUser.get();
            if (userGet.getEmail().equals(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Ya existe un usuario con este correo");
            }
        }

        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve la lista de todos los usuarios")
    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) String lastName) {

        if(lastName != null)
            return userRepository.findLikeLastName(lastName);

        return userRepository.findAll();
    }

    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve un usuario específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id){
        return userRepository.findById(id).orElseThrow();
    }

    @Operation(summary = "Actualizar un usuario", description = "Permite actualizar la información de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable String id){
        user.setId(id);
        
        userRepository.save(user);
        return user;
    }

    @Operation(summary = "Eliminar un usuario", description = "Permite eliminar un usuario de la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public Map<String, String> deleteUser(@PathVariable String id) {
        Map<String, String> respMap = new HashMap<String, String>();
        userRepository.deleteById(id);
        respMap.put("idUser", id);
        respMap.put("message", "Usuario eliminado");
        return respMap;
    }

    // Métodos para añadir pólizas

    /**
     * Retorna únicamente las pólizas del usuaio con el id indicado
     * @param id
     * @return ist<Poliza>
     */
    @Operation(summary = "Obtener pólizas de un usuario", description = "Devuelve las pólizas asociadas a un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pólizas encontradas"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}/polizas")
    public List<Poliza> getUserPoliza(@PathVariable String id) {
        Optional<User> optUser = userRepository.findById(id);

        if (optUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        User user = optUser.get();
        return user.getPolicies();
    }

    /**
     * Permite añadir una o más pólizas a la vez
     * @param id
     * @param polizas
     * @return ResponseEntity<User>
     */
    @Operation(summary = "Añadir pólizas a un usuario", description = "Permite añadir una o más pólizas a un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pólizas añadidas correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/{id}/polizas")
    public ResponseEntity<User> setUserPolizas(@PathVariable String id,  @RequestBody Poliza polizas) {
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isPresent()) {
            User user = optUser.get();
            List<Poliza> userP = user.getPolicies();
            boolean volver = false;

            if(userP == null){ // Si el usuario no tiene pólizas añadimos la enviada de una vez
                userP = new ArrayList<Poliza>();
                userP.add(polizas);
            } else {
                for(Poliza p : userP) {
                    if(p.getNumber() == polizas.getNumber()){
                        volver = true;
                        break;
                    }
                }
    
                if(!volver) {
                    userP.add(polizas);
                }
            }
            
            user.setPolicies(userP);
            userRepository.save(user);
            // Retornar 200 OK si todo salió bien
            return ResponseEntity.ok(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    /***
     * Elimina la póliza con el número indicado de un usuario en específico
     * @param id
     * @param idPoliza
     * @return ResponseEntity<String>
     */
    @Operation(summary = "Eliminar una póliza de un usuario", description = "Permite eliminar una póliza de un usuario determinado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Póliza eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Póliza no encontrada")
    })
    @DeleteMapping("/{id}/polizas/{idPoliza}")
    public ResponseEntity<Map<String, String>> deteleUserPoliza(@PathVariable String id, @PathVariable String idPoliza) {
        Map<String, String> resMap = new HashMap<String, String>();
        Optional<User> optUser = userRepository.findById(id);
        // Si el usuario no existe, devolver un 404 con un mensaje personalizado
        if (optUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        User user = optUser.get();
        List<Poliza> userP = user.getPolicies();

        Iterator<Poliza> iterator = userP.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Poliza p = iterator.next();
            if (p.getNumber() == Integer.parseInt(idPoliza)) {
                iterator.remove(); // Eliminar la póliza
                found = true;
                break; // Salir del bucle una vez encontrada la póliza
            }
        }

        // Si se eliminaron todas las pólizas, el atributo pasa a ser null;
        if(userP.size() == 0) userP = null;

        if (found) {
            // Si se eliminó la póliza, guardar el usuario con la lista actualizada
            user.setPolicies(userP);
            userRepository.save(user);
            resMap.put("user", user.toString());
            resMap.put("message", "Póliza eliminada con éxito");
            return ResponseEntity.ok(resMap);
        } else {
            // Si no se encontró la póliza, devolver un 404
            resMap.put("user", user.toString());
            resMap.put("message", "Póliza no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resMap);
        }
    }

    /**
     * Actualiza una póliza con un id específico de un usuario determinado. Elimina la póliza existente
     * con el mismo Id y luego añade la que se encuentra en el RequestBody
     * @param id
     * @param idPoliza
     * @param poliza
     * @return User
     */
    @Operation(summary = "Actualizar una póliza de un usuario", description = "Permite actualizar una póliza de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Póliza actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario o póliza no encontrados")
    })
    @PutMapping("/{id}/polizas/{idPoliza}")
    public User updatePolizaUser(@PathVariable String id, @PathVariable String idPoliza, @RequestBody Poliza poliza) {
        Optional<User> optUser = userRepository.findById(id);
        // Si el usuario no existe, devolver un 404 con un mensaje personalizado
        if (optUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        User user = optUser.get();
        List<Poliza> userP = user.getPolicies();

        Iterator<Poliza> iterator = userP.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Poliza p = iterator.next();
            if (p.getNumber() == Integer.parseInt(idPoliza)) {
                iterator.remove(); // Eliminar la póliza
                found = true;
                break; // Salir del bucle una vez encontrada la póliza
            }
        }

        if (found) {
            // Si se eliminó la póliza, guarda la nueva póliza y persiste el usuario con la lista actualizada
            userP.add(poliza);
            user.setPolicies(userP);
            userRepository.save(user);
            return user;
        } else {
            // Si no se encontró la póliza, devolver un 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Póliza no encontrada");
        }
    }
}
