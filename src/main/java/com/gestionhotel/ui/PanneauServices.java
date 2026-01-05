package com.gestionhotel.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.gestionhotel.core.Hotel;
import com.gestionhotel.model.Service;
import com.gestionhotel.utils.SQLiteDatabaseManager;

/**
 * Panneau de gestion des services avec interface graphique.
 * Permet d'ajouter, modifier et supprimer des services.
 * 
 * @author Dev 4 (Phase 4)
 */
public class PanneauServices extends JPanel {

    private Hotel hotel;
    private FenetrePrincipale fenetrePrincipale;
    private JTable tableServices;
    private DefaultTableModel modelTable;
    private JTextField champNom;
    private JTextArea champDescription;
    private JTextField champPrix;
    private JCheckBox checkDisponible;

    public PanneauServices(Hotel hotel, FenetrePrincipale fenetrePrincipale) {
        this.hotel = hotel;
        this.fenetrePrincipale = fenetrePrincipale;
        initialiserInterface();
        actualiser();
    }

    private void initialiserInterface() {
        setLayout(new BorderLayout());

        // Panneau supérieur : Table
        String[] colonnes = {"ID", "Nom", "Description", "Prix", "Disponible"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableServices = SwingUtils.creerTable(modelTable);
        JScrollPane scrollPane = new JScrollPane(tableServices);

        // Panneau central : Formulaire
        JPanel panelFormulaire = creerFormulaire();

        // Panneau inférieur : Boutons
        JPanel panelBoutons = creerBoutons();

        // Assemblage
        JPanel panelGauche = new JPanel(new BorderLayout());
        panelGauche.add(scrollPane, BorderLayout.CENTER);
        panelGauche.add(panelBoutons, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelGauche, panelFormulaire);
        splitPane.setDividerLocation(700);
        splitPane.setResizeWeight(0.7);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel creerFormulaire() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulaire Service"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nom
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        champNom = new JTextField(20);
        panel.add(champNom, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Description :"), gbc);
        gbc.gridx = 1;
        champDescription = new JTextArea(5, 20);
        champDescription.setLineWrap(true);
        champDescription.setWrapStyleWord(true);
        JScrollPane scrollDescription = new JScrollPane(champDescription);
        panel.add(scrollDescription, gbc);

        // Prix
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Prix (€) :"), gbc);
        gbc.gridx = 1;
        champPrix = new JTextField(20);
        panel.add(champPrix, gbc);

        // Disponible
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        checkDisponible = new JCheckBox("Service disponible");
        checkDisponible.setSelected(true);
        panel.add(checkDisponible, gbc);

        // Boutons formulaire
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel panelBoutonsForm = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterService());
        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(e -> modifierService());
        JButton btnReinitialiser = new JButton("Réinitialiser");
        btnReinitialiser.addActionListener(e -> reinitialiserFormulaire());
        panelBoutonsForm.add(btnAjouter);
        panelBoutonsForm.add(btnModifier);
        panelBoutonsForm.add(btnReinitialiser);
        panel.add(panelBoutonsForm, gbc);

        return panel;
    }

    private JPanel creerBoutons() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerService());
        panel.add(btnSupprimer);
        return panel;
    }

    public void actualiser() {
        modelTable.setRowCount(0);
        for (Service service : hotel.getServicesDisponibles()) {
            Object[] row = {
                service.getIdService(),
                service.getNom(),
                service.getDescription(),
                SwingUtils.formaterPrix(service.getPrix()),
                service.isDisponible() ? "Oui" : "Non"
            };
            modelTable.addRow(row);
        }
        fenetrePrincipale.mettreAJourStatusBar(hotel.getServicesDisponibles().size() + " service(s)");
    }

    private void ajouterService() {
        if (!validerFormulaire()) {
            return;
        }

        String nom = champNom.getText().trim();
        String description = champDescription.getText().trim();
        Double prix = SwingUtils.validerNombre(champPrix, "Prix");
        if (prix == null) {
            return;
        }

        Service service = new Service(nom, description, prix);
        service.setDisponible(checkDisponible.isSelected());

        hotel.ajouterService(service);
        
        // Sauvegarder automatiquement dans SQLite
        SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
        dbManager.sauvegarderHotel(hotel);
        
        actualiser();
        reinitialiserFormulaire();
        SwingUtils.afficherSucces(this, "Service ajouté avec succès.", "Succès");
    }

    private void modifierService() {
        int row = tableServices.getSelectedRow();
        if (row == -1) {
            SwingUtils.afficherErreur(this, "Veuillez sélectionner un service à modifier.", "Erreur");
            return;
        }

        if (!validerFormulaire()) {
            return;
        }

        int idService = (Integer) modelTable.getValueAt(row, 0);
        Service service = null;
        for (Service s : hotel.getServicesDisponibles()) {
            if (s.getIdService() == idService) {
                service = s;
                break;
            }
        }

        if (service != null) {
            service.setNom(champNom.getText().trim());
            service.setDescription(champDescription.getText().trim());
            Double prix = SwingUtils.validerNombre(champPrix, "Prix");
            if (prix != null) {
                service.setPrix(prix);
            }
            service.setDisponible(checkDisponible.isSelected());

            // Sauvegarder automatiquement dans SQLite
            SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
            dbManager.sauvegarderHotel(hotel);

            actualiser();
            reinitialiserFormulaire();
            SwingUtils.afficherSucces(this, "Service modifié avec succès.", "Succès");
        }
    }

    private void supprimerService() {
        int row = tableServices.getSelectedRow();
        if (row == -1) {
            SwingUtils.afficherErreur(this, "Veuillez sélectionner un service à supprimer.", "Erreur");
            return;
        }

        int idService = (Integer) modelTable.getValueAt(row, 0);
        Service service = null;
        for (Service s : hotel.getServicesDisponibles()) {
            if (s.getIdService() == idService) {
                service = s;
                break;
            }
        }

        if (service != null) {
            boolean confirmer = SwingUtils.demanderConfirmation(
                this,
                "Êtes-vous sûr de vouloir supprimer le service '" + service.getNom() + "' ?",
                "Confirmation de suppression"
            );

            if (confirmer) {
                hotel.getServicesDisponibles().remove(service);
                
                // Sauvegarder automatiquement dans SQLite
                SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
                dbManager.sauvegarderHotel(hotel);
                
                actualiser();
                SwingUtils.afficherSucces(this, "Service supprimé avec succès.", "Succès");
            }
        }
    }

    private boolean validerFormulaire() {
        if (!SwingUtils.validerChampNonVide(champNom, "Nom")) {
            return false;
        }
        if (champDescription.getText().trim().isEmpty()) {
            SwingUtils.afficherErreur(this, "Le champ 'Description' ne peut pas être vide.", "Erreur de validation");
            champDescription.requestFocus();
            return false;
        }
        return SwingUtils.validerChampNonVide(champPrix, "Prix");
    }

    private void reinitialiserFormulaire() {
        champNom.setText("");
        champDescription.setText("");
        champPrix.setText("");
        checkDisponible.setSelected(true);
        tableServices.clearSelection();
    }
}

