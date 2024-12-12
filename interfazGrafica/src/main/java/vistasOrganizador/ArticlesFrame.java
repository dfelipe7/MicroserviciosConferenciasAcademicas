package vistasOrganizador;

import entidades.Evaluator;
import entidades.ReviewDTO;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import vistasAutor.ArticleService;
import vistasAutor.AuthorFrame;

public class ArticlesFrame extends JFrame {
    private JTable articlesTable;
    private DefaultTableModel tableModel;
    private JComboBox<Evaluator> evaluatorsComboBox; // Usar un JComboBox con objetos Evaluator
    private JButton assignButton; // Botón para asignar evaluador
    private JButton viewStatusButton; // Botón para ver estado y comentarios
    private ConferenceService conferenceService;
    private ArticleService articleService;

    public ArticlesFrame(String[][] articles, String[][] evaluators) {
        setTitle("Artículos Asociados");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        conferenceService = new ConferenceService(); // Inicializar servicio
        articleService = new ArticleService();

        // Crear tabla para mostrar los artículos
        String[] columnNames = {"ID", "Título", "Autor", "Archivo", "Acción"};
        tableModel = new DefaultTableModel(articles, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer la columna de acción editable para el botón
                return column == 4;
            }
        };
        articlesTable = new JTable(tableModel);

        // Configurar el botón en la columna de acción
        TableColumn actionColumn = articlesTable.getColumnModel().getColumn(4);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor((TableCellEditor) new ButtonEditor(new JCheckBox()));

        // Añadir la tabla a la ventana
        add(new JScrollPane(articlesTable), BorderLayout.CENTER);

        // Panel para asignar evaluadores y ver estado
        JPanel bottomPanel = new JPanel(new FlowLayout());

        evaluatorsComboBox = new JComboBox<>();
        for (String[] evaluator : evaluators) {
            // Crear un objeto Evaluator con ID y nombre
            Evaluator evaluatorObj = new Evaluator(Long.parseLong(evaluator[0]), evaluator[1]);
            evaluatorsComboBox.addItem(evaluatorObj); // Añadir el objeto al JComboBox
        }
        bottomPanel.add(new JLabel("Evaluador:"));
        bottomPanel.add(evaluatorsComboBox);

        assignButton = new JButton("Asignar Evaluador");
        bottomPanel.add(assignButton);

        viewStatusButton = new JButton("Ver Estado y Comentarios");
        bottomPanel.add(viewStatusButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Acción del botón "Asignar Evaluador"
        assignButton.addActionListener(e -> assignEvaluatorToArticle());

        // Acción del botón "Ver Estado y Comentarios"
        viewStatusButton.addActionListener(e -> viewArticleStatus());
    }

    private void assignEvaluatorToArticle() {
        int selectedRow = articlesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un artículo.");
            return;
        }

        String articleId = (String) tableModel.getValueAt(selectedRow, 0);
        Evaluator selectedEvaluator = (Evaluator) evaluatorsComboBox.getSelectedItem(); // Obtener el evaluador seleccionado

        try {
            String message = conferenceService.assignEvaluatorToArticle(Long.parseLong(articleId), selectedEvaluator.getId());
            JOptionPane.showMessageDialog(this, message);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al asignar evaluador: " + ex.getMessage());
        }
    }

    private void viewArticleStatus() {
        int selectedRow = articlesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un artículo.");
            return;
        }

        String articleId = (String) tableModel.getValueAt(selectedRow, 0);

        try {
            // Llamar al servicio para obtener el estado y los comentarios
            ReviewDTO review = articleService.getReviewByArticleId(Long.parseLong(articleId));

            if (review != null) {
                // Mostrar la ventana con los detalles de la revisión
                AuthorFrame authorFrame = new AuthorFrame(review);
                authorFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No hay revisión asociada a este artículo.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener el estado y los comentarios: " + ex.getMessage());
        }
    }

    // Renderer para mostrar el botón en la celda
    private class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setText("Abrir PDF");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor para manejar el clic del botón
    private class ButtonEditor extends DefaultCellEditor {

        private String filePath;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            JButton button = new JButton("Abrir PDF");
            button.addActionListener(e -> openPdf(filePath));
            this.editorComponent = button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            filePath = (String) table.getValueAt(row, 3); // Obtener la ruta del archivo PDF de la columna 3
            return (Component) editorComponent;
        }

        @Override
        public Object getCellEditorValue() {
            return filePath;
        }
    }

    private void openPdf(String filePath) {
        try {
            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                JOptionPane.showMessageDialog(this, "El archivo no existe: " + filePath);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al abrir el archivo PDF: " + e.getMessage());
        }
    }
}
