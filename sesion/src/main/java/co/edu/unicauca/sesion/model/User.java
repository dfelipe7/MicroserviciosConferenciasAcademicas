/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.sesion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa a un usuario en el sistema.
 * 
 * Cada usuario tiene un identificador único, nombre, correo electrónico, 
 * contraseña y un rol asociado, que determina sus permisos en el sistema.
 * 
 * @author Jesus Iles
 * @author Daniel Muñoz
 * @author Esteban Martinez 
 * @author Felipe Armero
 * 
 *
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Identificador único del usuario en el sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del usuario. Este campo es obligatorio.
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    /**
     * Correo electrónico del usuario. Debe ser válido y es obligatorio.
     */
    @Email(message = "Email no es válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    /**
     * Contraseña del usuario. Este campo es obligatorio.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    /**
     * Rol del usuario en el sistema (por ejemplo, 'ADMIN', 'USER'). Este campo es obligatorio.
     */
    @NotBlank(message = "El rol es obligatorio")
    private String role;

    /**
     * Obtiene el identificador único del usuario.
     * 
     * @return el ID del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     * 
     * @param id el nuevo ID del usuario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario.
     * 
     * @return el nombre del usuario
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del usuario.
     * 
     * @param name el nuevo nombre del usuario
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return el correo electrónico del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param email el nuevo correo electrónico del usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return la contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param password la nueva contraseña del usuario
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el rol del usuario.
     * 
     * @return el rol del usuario
     */
    public String getRole() {
        return role;
    }

    /**
     * Establece el rol del usuario.
     * 
     * @param role el nuevo rol del usuario
     */
    public void setRole(String role) {
        this.role = role;
    }
}
