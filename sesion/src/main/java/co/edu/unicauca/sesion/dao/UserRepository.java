/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.edu.unicauca.sesion.dao;

import co.edu.unicauca.sesion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 *  Repositorio para realizar operaciones CRUD sobre usuarios {@link User}.
 * 
 * Extiende {@link JpaRepository} de Spring Data JPA para realizar operaciones
 * CRUD en la base de datos y búsquedas personalizadas por email e ID.
 * 
 * @author Jesus Iles
 * @author Daniel Muñoz
 * @author Esteban Martinez 
 * @author Felipe Armero
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * 
     * @param email el email del usuario a buscar
     * @return un {@link Optional} que contiene el usuario si se encuentra
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por su identificador único.
     * 
     * @param id el identificador del usuario a buscar
     * @return un {@link Optional} que contiene el usuario si se encuentra
     */
    Optional<User> findById(Long id);
}
