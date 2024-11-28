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


import jakarta.persistence.*;

/**
 * Clase base abstracta para los usuarios.
 * Configura el nombre de la tabla en la base de datos para evitar conflictos con palabras reservadas.
 */
@Entity
@Table(name = "users") // Cambia el nombre de la tabla a "users" para evitar conflictos
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
        // Constructor vacío requerido por JPA
    }

    public abstract String getRole();

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
