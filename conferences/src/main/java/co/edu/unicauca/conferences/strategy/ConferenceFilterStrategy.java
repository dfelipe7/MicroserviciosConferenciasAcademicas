/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.edu.unicauca.conferences.strategy;

/**
 *
 * @author Unicauca
 */

import co.edu.unicauca.conferences.model.Conference;
import java.util.List;

public interface ConferenceFilterStrategy {
    List<Conference> filter(List<Conference> conferences);
}
