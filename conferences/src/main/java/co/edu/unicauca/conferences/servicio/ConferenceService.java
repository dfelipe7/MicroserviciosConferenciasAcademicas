/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.conferences.servicio;

import co.edu.unicauca.conferences.dao.ConferenceRepository;
import co.edu.unicauca.conferences.model.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las operaciones CRUD de conferencias.
 * Proporciona métodos para crear, leer, actualizar y eliminar conferencias.
 * 
 * @author Jesus Iles
 * @author Daniel Muñoz
 * @author Esteban Martinez
 * @autor Felipe Armero
 */
@Service
public class ConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;

    /**
     * Crea una nueva conferencia.
     * 
     * @param conference La conferencia a crear
     * @return La conferencia creada
     */
    public Conference createConference(Conference conference) {
        return conferenceRepository.save(conference);
    }

    /**
     * Obtiene todas las conferencias registradas.
     * 
     * @return Lista de conferencias
     */
    public List<Conference> getAllConferences() {
        return conferenceRepository.findAll();
    }

    /**
     * Busca una conferencia por su ID.
     * 
     * @param id El ID de la conferencia
     * @return Un Optional con la conferencia encontrada o vacío si no se encuentra
     */
    public Optional<Conference> getConferenceById(Long id) {
        return conferenceRepository.findById(id);
    }

    /**
     * Actualiza una conferencia existente.
     * 
     * @param id El ID de la conferencia a actualizar
     * @param conference La conferencia con la información actualizada
     * @return La conferencia actualizada
     * @throws ResourceNotFoundException Si la conferencia no se encuentra
     */
    public Conference updateConference(Long id, Conference conference) {
        // Verificar si existe la conferencia
        Conference existingConference = conferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conference not found"));
        
        // Actualizar propiedades
        existingConference.setName(conference.getName());
        existingConference.setStartDate(conference.getStartDate());
        existingConference.setEndDate(conference.getEndDate());
        existingConference.setLocation(conference.getLocation());
        existingConference.setTopics(conference.getTopics());
        
        return conferenceRepository.save(existingConference);
    }

    /**
     * Elimina una conferencia por su ID.
     * 
     * @param id El ID de la conferencia a eliminar
     * @throws ResourceNotFoundException Si la conferencia no se encuentra
     */
    public void deleteConference(Long id) {
        // Verificar si existe la conferencia
        if (!conferenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Conference not found");
        }
        conferenceRepository.deleteById(id);
    }
}
