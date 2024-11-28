/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.sesion.model;

/**
 *
 * @author Unicauca
 */

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

/**
 * Usuario Autor.
 * Representa a los usuarios con el rol AUTOR.
 */
@Entity
@DiscriminatorValue("Autor")
public class AuthorUser extends User {
    public AuthorUser(String name, String email, String password) {
        super(name, email, password);
    }

    public AuthorUser() {
        super();
    }

    @Override
    public String getRole() {
        return "Autor";
    }
}
