package vistasOrganizador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser; // Importar la clase JDateChooser
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import vistasSesion.VtnPrincipalLogin;

public class ConferenceFrame extends JFrame {

    private JTextField nameField;
    private JTextField locationField;
    private JDateChooser startDateChooser; 
    private JDateChooser endDateChooser;   
    private JTextField topicsField;
    private JTable conferenceTable;
    private DefaultTableModel tableModel;
    private ConferenceService conferenceService;
    private String organizerId;
    private Long selectedConferenceId;

    public ConferenceFrame(String organizerId) {
        this.organizerId = organizerId;
        conferenceService = new ConferenceService(); // Instancia de ConferenceService
        setTitle("Gestión de Conferencias");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear el panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // Panel para los campos de entrada
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        inputPanel.add(new JLabel("Nombre:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Ubicación:"));
        locationField = new JTextField();
        inputPanel.add(locationField);

        inputPanel.add(new JLabel("Fecha de Inicio:"));
        startDateChooser = new JDateChooser();
        inputPanel.add(startDateChooser);

        inputPanel.add(new JLabel("Fecha de Fin:"));
        endDateChooser = new JDateChooser();
        inputPanel.add(endDateChooser);

        inputPanel.add(new JLabel("Temas:"));
        topicsField = new JTextField();
        inputPanel.add(topicsField);

        // Botones para las acciones principales
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        JButton createButton = new JButton("Crear Conferencia");
        createButton.addActionListener(e -> createConference());
        buttonPanel.add(createButton);

        JButton listButton = new JButton("Listar Conferencias");
        listButton.addActionListener(e -> listConferences());
        buttonPanel.add(listButton);

        JButton updateButton = new JButton("Actualizar Conferencia");
        updateButton.addActionListener(e -> updateConference());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Eliminar Conferencia");
        deleteButton.addActionListener(e -> deleteConference());
        buttonPanel.add(deleteButton);

        JButton viewArticlesButton = new JButton("Ver Artículos");
        viewArticlesButton.addActionListener(e -> showArticlesForSelectedConference());
        buttonPanel.add(viewArticlesButton);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(new LogoutButtonListener());
        buttonPanel.add(logoutButton);

        // Agregar los paneles al mainPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Tabla para listar conferencias
        String[] columnNames = {"ID", "Nombre", "Ubicación", "Fecha de Inicio", "Fecha de Fin", "Temas"};
        tableModel = new DefaultTableModel(columnNames, 0);
        conferenceTable = new JTable(tableModel);
        conferenceTable.setFillsViewportHeight(true);

        // Añadir JScrollPane para la tabla
        JScrollPane scrollPane = new JScrollPane(conferenceTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Conferencias"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Evento de selección de fila
        conferenceTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = conferenceTable.getSelectedRow();
            if (selectedRow != -1) {
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                locationField.setText(tableModel.getValueAt(selectedRow, 2).toString());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    startDateChooser.setDate(sdf.parse(tableModel.getValueAt(selectedRow, 3).toString()));
                    endDateChooser.setDate(sdf.parse(tableModel.getValueAt(selectedRow, 4).toString()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                topicsField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                selectedConferenceId = Long.parseLong(tableModel.getValueAt(selectedRow, 0).toString());
            }
        });
    }

    private void createConference() {
        try {
            String name = nameField.getText();
            String location = locationField.getText();
            String startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateChooser.getDate());
            String endDate = new SimpleDateFormat("yyyy-MM-dd").format(endDateChooser.getDate());
            String topics = topicsField.getText();

            String result = conferenceService.createConference(name, location, startDate, endDate, topics, organizerId);
            JOptionPane.showMessageDialog(this, result);
            clearFields();
            listConferences();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void listConferences() {
        try {
            String[][] conferences = conferenceService.getConferences();
            tableModel.setRowCount(0);
            for (String[] conference : conferences) {
                tableModel.addRow(conference);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateConference() {
        try {
            if (selectedConferenceId == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una conferencia para actualizar.");
                return;
            }

            String name = nameField.getText();
            String location = locationField.getText();
            String startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateChooser.getDate());
            String endDate = new SimpleDateFormat("yyyy-MM-dd").format(endDateChooser.getDate());
            String topics = topicsField.getText();

            String result = conferenceService.updateConference(selectedConferenceId, name, location, startDate, endDate, topics, organizerId);
            JOptionPane.showMessageDialog(this, result);
            clearFields();
            listConferences();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteConference() {
        try {
            if (selectedConferenceId == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una conferencia para eliminar.");
                return;
            }

            String result = conferenceService.deleteConference(selectedConferenceId, organizerId);
            JOptionPane.showMessageDialog(this, result);
            clearFields();
            listConferences();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

 
    private void showArticlesForSelectedConference() {
    if (selectedConferenceId == null) {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona una conferencia.");
        return;
    }

    try {
        String[][] articles = conferenceService.getArticlesByConference(selectedConferenceId);
        String[][] evaluators = conferenceService.getEvaluators(); // Obtener evaluadores
        ArticlesFrame articlesFrame = new ArticlesFrame(articles, evaluators);
        articlesFrame.setVisible(true);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
    }
}

    private void clearFields() {
        nameField.setText("");
        locationField.setText("");
        startDateChooser.setDate(null);
        endDateChooser.setDate(null);
        topicsField.setText("");
        selectedConferenceId = null;
    }

    private class LogoutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(ConferenceFrame.this, "Sesión Cerrada");
            dispose();
            VtnPrincipalLogin login = new VtnPrincipalLogin();
            login.setVisible(true);
        }
    }
}
