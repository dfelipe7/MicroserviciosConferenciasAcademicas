/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.review.dto;

/**
 * Representa un objeto de transferencia de datos (DTO) para un usuario en el sistema.
 * 
 * Este DTO incluye el identificador del usuario y su rol dentro del sistema.
 * 
 * @author Daniel Muñoz
 * @author Jesus Iles
 * @author Esteban Martinez 
 * @author Felipe Armero
 */
public class UserDTO {
    private Long id;
    private String role;

    /**
     * Constructor para crear un UserDTO con un ID y un rol específicos.
     * 
     * @param id el identificador del usuario
     * @param role el rol del usuario
     */
    public UserDTO(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    /**
     * Obtiene el identificador del usuario.
     * 
     * @return el ID del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador del usuario.
     * 
     * @param id el ID del usuario
     */
    public void setId(Long id) {
        this.id = id;
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
     * @param role el rol del usuario
     */
    public void setRole(String role) {
        this.role = role;
    }
}
