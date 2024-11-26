/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.edu.unicauca.conferences.dao;

import co.edu.unicauca.conferences.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para la entidad {@link Conference}.
 * 
 * Esta interfaz extiende de {@link JpaRepository}, proporcionando métodos CRUD 
 * para gestionar entidades de tipo Conference en la base de datos.
 * 
 * 
 * @author Daniel Muñoz
 * @author Jesus Iles
 * @author Esteban Martinez 
 * @author Felipe Armero
 */
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    
}

