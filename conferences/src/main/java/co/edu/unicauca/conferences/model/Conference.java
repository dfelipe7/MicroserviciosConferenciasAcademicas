/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.conferences.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * Representa una conferencia en el sistema.
 * 
 * Cada conferencia tiene un identificador único, nombre, fechas de inicio y 
 * fin, ubicación, temas y un identificador del organizador que creó la 
 * conferencia.
 * 
 * @author Daniel Muñoz
 * @author Jesus Iles
 * @author Esteban Martinez 
 * @author Felipe Armero
 */
@Entity
@Table(name = "conferences")
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la conferencia es obligatorio")
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    @NotBlank(message = "El lugar es obligatorio")
    private String location;

    private String topics;

    private Long organizerId; // ID del organizador que creó la conferencia

    /**
     * Obtiene el identificador del organizador.
     * 
     * @return el ID del organizador de la conferencia
     */
    public Long getOrganizerId() {
        return organizerId;
    }

    /**
     * Establece el identificador del organizador.
     * 
     * @param organizerId el ID del organizador de la conferencia
     */
    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Obtiene el identificador de la conferencia.
     * 
     * @return el ID de la conferencia
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la conferencia.
     * 
     * @param id el ID de la conferencia
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la conferencia.
     * 
     * @return el nombre de la conferencia
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la conferencia.
     * 
     * @param name el nombre de la conferencia
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la fecha de inicio de la conferencia.
     * 
     * @return la fecha de inicio de la conferencia
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Establece la fecha de inicio de la conferencia.
     * 
     * @param startDate la fecha de inicio de la conferencia
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Obtiene la fecha de fin de la conferencia.
     * 
     * @return la fecha de fin de la conferencia
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Establece la fecha de fin de la conferencia.
     * 
     * @param endDate la fecha de fin de la conferencia
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Obtiene la ubicación de la conferencia.
     * 
     * @return la ubicación de la conferencia
     */
    public String getLocation() {
        return location;
    }

    /**
     * Establece la ubicación de la conferencia.
     * 
     * @param location la ubicación de la conferencia
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Obtiene los temas de la conferencia.
     * 
     * @return los temas de la conferencia
     */
    public String getTopics() {
        return topics;
    }

    /**
     * Establece los temas de la conferencia.
     * 
     * @param topics los temas de la conferencia
     */
    public void setTopics(String topics) {
        this.topics = topics;
    }
}
