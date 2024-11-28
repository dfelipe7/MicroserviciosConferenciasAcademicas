/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.edu.unicauca.conferences.builder;

/**
 *
 * @author Unicauca
 */

import co.edu.unicauca.conferences.model.Conference;

/**
 * Interfaz para definir los métodos del Builder de Conferencias.
 */
public interface ConferenceBuilder {
    void setName(String name);

    void setStartDate(String startDate);

    void setEndDate(String endDate);

    void setLocation(String location);

    void setTopics(String topics);

    void setType();

    Conference build();
}
