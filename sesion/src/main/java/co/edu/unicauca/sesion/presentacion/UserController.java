/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.sesion.presentacion;

import co.edu.unicauca.sesion.model.User;
import co.edu.unicauca.sesion.servicio.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los usuarios.
 * Proporciona endpoints para registrar, autenticar y recuperar información de usuarios.
 * 
 * @author Jesus Iles
 * @author Daniel Muñoz
 * @author Esteban Martinez
 * @author Felipe Armero
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param user El objeto User que contiene la información del nuevo usuario.
     * @return Una respuesta HTTP con el nuevo usuario registrado.
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User newUser = userService.registerUser(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
        return ResponseEntity.ok(newUser);
    }

    /**
     * Endpoint para autenticar un usuario.
     *
     * @param user El objeto User que contiene el correo electrónico y la contraseña del usuario.
     * @return Una respuesta HTTP que indica el estado de la autenticación.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> foundUser = userService.loginUser(user.getEmail(), user.getPassword());
        if (foundUser.isPresent()) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Endpoint para recuperar un usuario por su ID.
     *
     * @param id El ID del usuario a recuperar.
     * @return Una respuesta HTTP con el usuario encontrado o un estado 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id); 
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para obtener el rol de un usuario a partir de su correo electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return Una respuesta HTTP con el rol del usuario encontrado o un estado 404 si no se encuentra.
     */
    @GetMapping("/role")
    public ResponseEntity<String> getUserRoleByEmail(@RequestParam String email) {
        String role = userService.getRoleByEmail(email); 
        if (role != null) {
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra el rol
        }
    }

    /**
     * Endpoint para obtener el ID de un usuario a partir de su correo electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return Una respuesta HTTP con el ID del usuario encontrado o un estado 404 si no se encuentra.
     */
    @GetMapping("/id")
    public ResponseEntity<Long> getUserIdByEmail(@RequestParam String email) {
        Long userId = userService.getIdByEmail(email); // Método que debes implementar en UserService
        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra el ID
        }
    }
}
