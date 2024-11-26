package co.edu.unicauca.conferences.dto;

/**
 * DTO que representa la información básica de un artículo.
 * Incluye datos como el título, autor, conferencia asociada y la ubicación
 * del archivo subido.
 */
public class ArticleDTO {
    private Long id; // ID del artículo
    private String title; // Título del artículo
    private Long authorId; // ID del autor
    private Long conferenceId; // ID de la conferencia asociada
    private String filePath; // Ruta del archivo PDF o documento subido

    // Constructor vacío
    public ArticleDTO() {}

    // Constructor completo
    public ArticleDTO(Long id, String title, Long authorId, Long conferenceId, String filePath) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.conferenceId = conferenceId;
        this.filePath = filePath;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Long conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
