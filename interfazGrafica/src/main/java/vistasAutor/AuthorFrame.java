package vistasAutor;

import entidades.ReviewDTO;
import javax.swing.*;
import java.awt.*;

public class AuthorFrame extends JFrame {
    public AuthorFrame(ReviewDTO review) {
        setTitle("Detalles de Revisión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel para los detalles de la revisión
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(2, 2, 10, 10));

        // Estado
        detailsPanel.add(new JLabel("Estado:"));
        detailsPanel.add(new JLabel(review.getStatus()));

        // Comentarios
        detailsPanel.add(new JLabel("Comentarios:"));
        detailsPanel.add(new JLabel(review.getComments()));


        add(detailsPanel, BorderLayout.CENTER);

        // Botón de cerrar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
