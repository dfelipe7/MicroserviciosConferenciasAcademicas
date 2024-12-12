/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.conferences.dto;

/**
 *
 * @author Unicauca
 */

import co.edu.unicauca.conferences.model.Conference;
import java.util.List;

/**
 * DTO que representa los detalles de una conferencia y sus artículos asociados.
 */
public class ConferenceDetailsDTO {
    private Conference conference;
    private List<ArticleDTO> articles;

    public ConferenceDetailsDTO(Conference conference, List<ArticleDTO> articles) {
        this.conference = conference;
        this.articles = articles;
    }

    // Getters y Setters
    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public List<ArticleDTO> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleDTO> articles) {
        this.articles = articles;
    }
}

