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
 * Usuario Evaluador.
 * Representa a los usuarios con el rol EVALUATOR.
 */
@Entity
@DiscriminatorValue("Evaluador")
public class EvaluatorUser extends User {
    public EvaluatorUser(String name, String email, String password) {
        super(name, email, password);
    }

    public EvaluatorUser() {
        super();
    }

    @Override
    public String getRole() {
        return "Evaluador";
    }
}
