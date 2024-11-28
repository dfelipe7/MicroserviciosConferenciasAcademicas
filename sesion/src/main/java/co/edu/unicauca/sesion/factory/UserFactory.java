/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.sesion.factory;

/**
 *
 * @author Unicauca
 */


import co.edu.unicauca.sesion.model.AuthorUser;
import co.edu.unicauca.sesion.model.EvaluatorUser;
import co.edu.unicauca.sesion.model.OrganizerUser;
import co.edu.unicauca.sesion.model.User;

/**
 * Fábrica para crear instancias de usuarios.
 * Implementa el patrón Factory Method para encapsular la lógica
 * de creación de objetos de usuario según su rol.
 * 
 * Este patrón facilita la creación de nuevos tipos de usuarios en el futuro.
 * 
 * @autor Jesus Iles, Daniel Muñoz, Esteban Martinez, Felipe Armero
 */

/**
 * Fábrica para crear instancias de usuarios según su rol.
 * Implementa el patrón Factory Method.
 */
public class UserFactory {

    /**
     * Crea un nuevo usuario basado en el rol proporcionado.
     *
     * @param name Nombre del usuario
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     * @param role Rol del usuario (ADMIN, AUTHOR, EVALUATOR).
     * @return Instancia específica de User según el rol.
     */
    public static User createUser(String name, String email, String password, String role) {
        switch (role) {
            case "Organizador":
                return new OrganizerUser(name, email, password);
            case "Autor":
                return new AuthorUser(name, email, password);
            case "Evaluador":
                return new EvaluatorUser(name, email, password);
            default:
                throw new IllegalArgumentException("Role no admitido: " + role);
        }
    }
}
