/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vistasOrganizador;

/**
 *
 * @author Unicauca
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ConferenceDetailsFrame extends JFrame {
    private JTable articlesTable;

public ConferenceDetailsFrame(String conferenceName, String location, List<String[]> articles) {
    setTitle("Detalles de Conferencia");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel conferenceInfoPanel = new JPanel();
    conferenceInfoPanel.setLayout(new GridLayout(2, 1));
    conferenceInfoPanel.add(new JLabel("Conferencia: " + conferenceName));
    conferenceInfoPanel.add(new JLabel("Ubicación: " + location));
    add(conferenceInfoPanel, BorderLayout.NORTH);

    // Tabla de artículos
    String[] columnNames = {"ID", "Título", "Autor", "Archivo"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

    for (String[] article : articles) {
        tableModel.addRow(article);
    }

    articlesTable = new JTable(tableModel);
    add(new JScrollPane(articlesTable), BorderLayout.CENTER);
}

}
