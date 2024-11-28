/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.sesion.proxy;

import co.edu.unicauca.sesion.servicio.UserService;
import co.edu.unicauca.sesion.model.User;
import java.util.Optional;

/**
 * Proxy para el servicio de usuarios.
 * Añade validaciones para `registerUser` y `loginUser` y delega otros métodos al `UserService`.
 */
public class UserServiceProxy extends UserService {
    private final UserService userService;

    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User registerUser(String name, String email, String password, String role) {
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Rol no permitido: " + role);
        }

        if (userService.getIdByEmail(email) != null) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        return userService.registerUser(name, email, password, role);
    }

    @Override
    public Optional<User> loginUser(String email, String password) {
        return userService.loginUser(email, password);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userService.findById(id);
    }

    @Override
    public String getRoleByEmail(String email) {
        return userService.getRoleByEmail(email);
    }

    @Override
    public Long getIdByEmail(String email) {
        return userService.getIdByEmail(email);
    }

    private boolean isValidRole(String role) {
        return role.equalsIgnoreCase("Organizador") || role.equalsIgnoreCase("Autor") || role.equalsIgnoreCase("Evaluador");
    }
}
