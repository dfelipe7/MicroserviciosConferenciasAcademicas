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
 * Usuario Administrador.
 * Representa a los usuarios con el rol ADMIN.
 */
@Entity
@DiscriminatorValue("Organizador")
public class OrganizerUser extends User {
    public OrganizerUser(String name, String email, String password) {
        super(name, email, password);
    }

    public OrganizerUser() {
        super();
    }

    @Override
    public String getRole() {
        return "Organizador";
    }
}
