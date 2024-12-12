package vistasEvaluador;

import entidades.ReviewDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class EvaluatorFrame extends JFrame {
    private JTable reviewTable;
    private DefaultTableModel tableModel;
    private EvaluatorService evaluatorService;
    private long incrementalId = 1; // Inicialización del ID incremental
    private String evaluatorId; // Guardar el ID del evaluador
    private boolean hasReviews = false; // Verificar si hay revisiones asignadas

    public EvaluatorFrame(String evaluatorId) {
        this.evaluatorId = evaluatorId; // Asignar el evaluador ID
        setTitle("Artículos Asignados");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        evaluatorService = new EvaluatorService();

        // Crear tabla para mostrar revisiones
        String[] columnNames = {"ID", "Nombre del Artículo", "PDF", "Estado", "Comentarios"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reviewTable = new JTable(tableModel);

        // Intentar cargar las revisiones
        cargarRevisiones();

        // Si no hay revisiones, mostrar un mensaje y evitar actualizar automáticamente
        if (!hasReviews) {
            JPanel noReviewsPanel = new JPanel();
            noReviewsPanel.setLayout(new BorderLayout());
            JLabel noReviewsLabel = new JLabel("No tienes revisiones asignadas actualmente.");
            noReviewsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noReviewsPanel.add(noReviewsLabel, BorderLayout.CENTER);

            JButton logoutButton = new JButton("Cerrar sesión");
            logoutButton.addActionListener(e -> cerrarSesion());
            noReviewsPanel.add(logoutButton, BorderLayout.SOUTH);

            add(noReviewsPanel, BorderLayout.CENTER);
        } else {
            // Mostrar tabla de revisiones
            add(new JScrollPane(reviewTable), BorderLayout.CENTER);

            // Botones para acciones
            JPanel buttonPanel = new JPanel();

            // Botón para abrir PDF
            JButton openPdfButton = new JButton("Abrir PDF");
            openPdfButton.addActionListener(e -> abrirPDF());
            buttonPanel.add(openPdfButton);

            // Botón para actualizar revisión
            JButton updateReviewButton = new JButton("Actualizar Revisión");
            updateReviewButton.addActionListener(e -> actualizarRevision());
            buttonPanel.add(updateReviewButton);

            // Botón para cerrar sesión
            JButton logoutButton = new JButton("Cerrar sesión");
            logoutButton.addActionListener(e -> cerrarSesion());
            buttonPanel.add(logoutButton);

            add(buttonPanel, BorderLayout.SOUTH);

            // Iniciar el hilo de actualización en tiempo real
            iniciarActualizacionAutomatica();
        }
    }

    private void cargarRevisiones() {
        try {
            List<ReviewDTO> reviews = evaluatorService.getReviewsByEvaluator(evaluatorId);
            tableModel.setRowCount(0); // Limpiar la tabla antes de agregar nuevos datos
            if (reviews.isEmpty()) {
                hasReviews = false; // No hay revisiones asignadas
            } else {
                hasReviews = true; // Hay revisiones asignadas
                for (ReviewDTO review : reviews) {
                    Long reviewId = review.getId() != null ? review.getId() : incrementalId++;
                    tableModel.addRow(new Object[]{
                        reviewId,
                        review.getArticleName(),
                        review.getPdfPath(),
                        review.getStatus(),
                        review.getComments()
                    });
                }
            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, "Error al cargar revisiones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarActualizacionAutomatica() {
        Thread updateThread = new Thread(() -> {
            while (hasReviews) { // Solo actualizar si hay revisiones asignadas
                try {
                    Thread.sleep(5000); // Actualizar cada 5 segundos
                    SwingUtilities.invokeLater(this::cargarRevisiones); // Cargar revisiones en el hilo de la interfaz
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(this, "Error en la actualización automática: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        updateThread.setDaemon(true); // Asegura que el hilo se detenga al cerrar la aplicación
        updateThread.start();
    }

    private void abrirPDF() {
        int selectedRow = reviewTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un artículo para abrir el PDF.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String pdfPath = (String) tableModel.getValueAt(selectedRow, 2);
        if (pdfPath == null || pdfPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró la ruta del archivo PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Desktop.getDesktop().open(new File(pdfPath));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir el archivo PDF. Verifique que exista en la ruta especificada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarRevision() {
        int selectedRow = reviewTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una revisión para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long reviewId = (Long) tableModel.getValueAt(selectedRow, 0);
        String newStatus = JOptionPane.showInputDialog(this, "Nuevo Estado (Aprobado/Rechazado/Pendiente):");

        if (newStatus == null || newStatus.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El estado no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] validStatuses = {"Aprobado", "Rechazado", "Pendiente"};
        if (!java.util.Arrays.asList(validStatuses).contains(newStatus)) {
            JOptionPane.showMessageDialog(this, "Estado inválido. Use: Aprobado, Rechazado o Pendiente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newComments = JOptionPane.showInputDialog(this, "Comentarios:");
        if (newComments == null || newComments.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los comentarios no pueden estar vacíos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ReviewDTO review = new ReviewDTO();
            review.setId(reviewId);
            review.setStatus(newStatus);
            review.setComments(newComments);

            String message = evaluatorService.updateReview(review);
            JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);

            tableModel.setValueAt(newStatus, selectedRow, 3);
            tableModel.setValueAt(newComments, selectedRow, 4);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar revisión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        dispose(); // Cierra la ventana actual
        // Puedes redirigir al usuario a la pantalla de inicio de sesión si es necesario
    }
}
