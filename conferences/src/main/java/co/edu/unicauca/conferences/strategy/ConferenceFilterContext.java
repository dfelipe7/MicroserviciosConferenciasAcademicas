/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.conferences.strategy;

/**
 *
 * @author Unicauca
 */

import co.edu.unicauca.conferences.model.Conference;
import java.util.List;

public class ConferenceFilterContext {
    private ConferenceFilterStrategy strategy;

    public ConferenceFilterContext(ConferenceFilterStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ConferenceFilterStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Conference> filter(List<Conference> conferences) {
        return strategy.filter(conferences);
    }
}
