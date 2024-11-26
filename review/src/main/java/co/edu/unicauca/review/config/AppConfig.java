/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.review.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de la aplicación para gestionar componentes comunes en el microservicio.
 *
 * La clase {@code AppConfig} se utiliza para definir y configurar beans que estarán disponibles 
 * en el contexto de la aplicación de Spring. En este caso, se configura un bean de {@link RestTemplate} 
 * para facilitar las solicitudes HTTP a otros servicios, lo que permite la comunicación con otros 
 * microservicios o APIs externas.
 * 
 * @author Daniel Muñoz
 * @author Jesus Iles
 * @author Esteban Martinez 
 * @author Felipe Armero
 */
@Configuration
public class AppConfig {

    /**
     * Crea y configura una instancia de {@link RestTemplate} que se registrará como un bean en 
     * el contexto de Spring. Este bean puede inyectarse en otras partes de la aplicación donde 
     * se necesiten realizar solicitudes HTTP.
     * 
     * @return una nueva instancia de {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
