/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.sesion.servicio;

import co.edu.unicauca.sesion.dao.UserRepository;
import co.edu.unicauca.sesion.factory.UserFactory;
import co.edu.unicauca.sesion.model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los usuarios.
 * Proporciona métodos para registrar y autenticar usuarios, así como para
 * recuperar información sobre el rol y el ID del usuario a partir del correo electrónico.
 * 
 * Este servicio utiliza el repositorio de usuarios para realizar operaciones 
 * CRUD y consultas personalizadas.
 * 
 * @author Jesus Iles
 * @author Daniel Muñoz
 * @author Esteban Martinez
 * @author Felipe Armero
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registramos un nuevo usuario utilizando la fábrica.
     */
    public User registerUser(String name, String email, String password, String role) {
        if (userRepository.findByEmail(email).isPresent()) {
           // throw new IllegalArgumentException("El email ya está registrado");
        }

        User user = UserFactory.createUser(name, email, hashPassword(password), role);
        return userRepository.save(user);
    }

    /**
     * Intentamos autenticar un usuario con las credenciales proporcionadas.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return Un {@link Optional} que contiene el usuario autenticado si las credenciales son válidas, o vacío si no.
     */
    public Optional<User> loginUser(String email, String password) {
        String hashedPassword = hashPassword(password);
        return userRepository.findByEmail(email)
            .filter(user -> user.getPassword().equals(hashedPassword));
    }

    /**
     * Hashes la contraseña utilizando el algoritmo SHA-256.
     *
     * @param password La contraseña en texto plano a hashear.
     * @return La contraseña hasheada.
     * @throws RuntimeException si ocurre un error durante el hashing.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error de hashing", e);
        }
    }

    /**
     * Buscamos un usuario por su ID.
     *
     * @param id El ID del usuario.
     * @return Un {@link Optional} que contiene el usuario si se encuentra, o vacío si no.
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id); 
    }

    /**
     * Obtiene el rol de un usuario a partir de su ID.
     *
     * @param id El ID del usuario.
     * @return El rol del usuario, o null si no se encuentra.
     */
    public String getRoleById(Long id) {
        return userRepository.findById(id)
            .map(User::getRole)
            .orElse(null); 
    }

    /**
     * Obtenemos el ID de un usuario a partir de su correo electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return El ID del usuario, o null si no se encuentra.
     */
    public Long getIdByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(User::getId) 
            .orElse(null); 
    }

    /**
     * Obtenemos el rol de un usuario a partir de su correo electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return El rol del usuario, o null si no se encuentra.
     */
    public String getRoleByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(User::getRole) 
            .orElse(null);
    }
}
