package co.edu.unicauca.sesion.presentacion;

import co.edu.unicauca.sesion.dto.UserDTO;
import co.edu.unicauca.sesion.model.User;
import co.edu.unicauca.sesion.proxy.UserServiceProxy;
import co.edu.unicauca.sesion.servicio.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los
 * usuarios. Proporciona endpoints para registrar, autenticar y recuperar
 * información de usuarios. Utiliza un proxy para añadir validaciones
 * adicionales antes de delegar las solicitudes al servicio de usuarios.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceProxy userServiceProxy;

    /**
     * Constructor para inicializar el controlador con el proxy del servicio.
     *
     * @param userService Servicio real de usuarios que será envuelto por el
     * proxy.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userServiceProxy = new UserServiceProxy(userService);
    }

    /**
     * Endpoint para registrar un nuevo usuario. Realiza validaciones
     * adicionales a través del proxy antes de registrar al usuario.
     *
     * @param userDTO El DTO que contiene la información del nuevo usuario.
     * @return Una respuesta HTTP con el nuevo usuario registrado.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        try {
            // Usamos el DTO para crear un usuario a través del proxy
            User newUser = userServiceProxy.registerUser(
                    userDTO.getName(), 
                    userDTO.getEmail(), 
                    userDTO.getPassword(), 
                    userDTO.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Endpoint para autenticar un usuario. Realiza validaciones adicionales a
     * través del proxy.
     *
     * @param userDTO El DTO que contiene el correo electrónico y la
     * contraseña del usuario.
     * @return Una respuesta HTTP que indica el estado de la autenticación.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        Optional<User> foundUser = userServiceProxy.loginUser(userDTO.getEmail(), userDTO.getPassword());
        if (foundUser.isPresent()) {
            return ResponseEntity.ok("Login correcto");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    /**
     * Endpoint para recuperar un usuario por su ID.
     *
     * @param id El ID del usuario a recuperar.
     * @return Una respuesta HTTP con el usuario encontrado o un estado 404 si
     * no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userServiceProxy.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para obtener el rol de un usuario a partir de su correo
     * electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return Una respuesta HTTP con el rol del usuario encontrado o un estado
     * 404 si no se encuentra.
     */
    @GetMapping("/role")
    public ResponseEntity<String> getUserRoleByEmail(@RequestParam String email) {
        String role = userServiceProxy.getRoleByEmail(email);
        if (role != null) {
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra el rol
        }
    }

    /**
     * Endpoint para obtener el ID de un usuario a partir de su correo
     * electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return Una respuesta HTTP con el ID del usuario encontrado o un estado
     * 404 si no se encuentra.
     */
    @GetMapping("/id")
    public ResponseEntity<Long> getUserIdByEmail(@RequestParam String email) {
        Long userId = userServiceProxy.getIdByEmail(email);
        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra el ID
        }
    }
    
     @GetMapping("/evaluators")
    public ResponseEntity<List<User>> getEvaluators() {
        try {
            List<User> evaluators = userServiceProxy.getEvaluators();
            return ResponseEntity.ok(evaluators);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
