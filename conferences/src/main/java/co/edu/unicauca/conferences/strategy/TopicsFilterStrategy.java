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
import java.util.stream.Collectors;

public class TopicsFilterStrategy implements ConferenceFilterStrategy {
    private String topic;

    public TopicsFilterStrategy(String topic) {
        this.topic = topic;
    }

    @Override
    public List<Conference> filter(List<Conference> conferences) {
        return conferences.stream()
                .filter(conference -> conference.getTopics() != null && conference.getTopics().toLowerCase().contains(topic.toLowerCase()))
                .collect(Collectors.toList());
    }
}