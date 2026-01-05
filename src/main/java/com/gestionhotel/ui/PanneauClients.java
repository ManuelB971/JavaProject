package com.gestionhotel.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.gestionhotel.core.Hotel;
import com.gestionhotel.model.Client;
import com.gestionhotel.utils.SQLiteDatabaseManager;

/**
 * Panneau de gestion des clients avec interface graphique.
 * Permet d'ajouter, modifier, supprimer et rechercher des clients.
 * 
 * @author Dev 4 (Phase 4)
 */
public class PanneauClients extends JPanel {

    private Hotel hotel;
    private FenetrePrincipale fenetrePrincipale;
    private JTable tableClients;
    private DefaultTableModel modelTable;
    private JTextField champNom;
    private JTextField champPrenom;
    private JTextField champEmail;
    private JTextField champTelephone;
    private JTextField champRecherche;

    public PanneauClients(Hotel hotel, FenetrePrincipale fenetrePrincipale) {
        this.hotel = hotel;
        this.fenetrePrincipale = fenetrePrincipale;
        initialiserInterface();
        actualiser();
    }

    private void initialiserInterface() {
        setLayout(new BorderLayout());

        // Panneau supérieur : Table
        String[] colonnes = {"N°", "Nom", "Prénom", "Email", "Téléphone"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableClients = SwingUtils.creerTable(modelTable);
        JScrollPane scrollPane = new JScrollPane(tableClients);

        // Panneau central : Formulaire
        JPanel panelFormulaire = creerFormulaire();

        // Panneau inférieur : Boutons
        JPanel panelBoutons = creerBoutons();

        // Panneau de recherche
        JPanel panelRecherche = creerPanneauRecherche();

        // Assemblage
        JPanel panelGauche = new JPanel(new BorderLayout());
        panelGauche.add(panelRecherche, BorderLayout.NORTH);
        panelGauche.add(scrollPane, BorderLayout.CENTER);
        panelGauche.add(panelBoutons, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelGauche, panelFormulaire);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.6);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel creerFormulaire() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulaire Client"));
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

        // Prénom
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 1;
        champPrenom = new JTextField(20);
        panel.add(champPrenom, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1;
        champEmail = new JTextField(20);
        panel.add(champEmail, gbc);

        // Téléphone
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx = 1;
        champTelephone = new JTextField(20);
        panel.add(champTelephone, gbc);

        // Boutons formulaire
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel panelBoutonsForm = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterClient());
        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(e -> modifierClient());
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
        btnSupprimer.addActionListener(e -> supprimerClient());
        panel.add(btnSupprimer);
        return panel;
    }

    private JPanel creerPanneauRecherche() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Rechercher :"));
        champRecherche = new JTextField(15);
        champRecherche.addActionListener(e -> rechercher());
        panel.add(champRecherche);
        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.addActionListener(e -> rechercher());
        panel.add(btnRechercher);
        JButton btnToutAfficher = new JButton("Tout afficher");
        btnToutAfficher.addActionListener(e -> actualiser());
        panel.add(btnToutAfficher);
        return panel;
    }

    public void actualiser() {
        modelTable.setRowCount(0);
        for (Client client : hotel.getClients()) {
            Object[] row = {
                client.getNumeroClient(),
                client.getNom(),
                client.getPrenom(),
                client.getEmail(),
                client.getTelephone()
            };
            modelTable.addRow(row);
        }
        fenetrePrincipale.mettreAJourStatusBar(hotel.getClients().size() + " client(s)");
    }

    private void ajouterClient() {
        if (!validerFormulaire()) {
            return;
        }

        String nom = champNom.getText().trim();
        String prenom = champPrenom.getText().trim();
        String email = champEmail.getText().trim();
        String telephone = champTelephone.getText().trim();

        Client client = new Client(nom, prenom, email, telephone);
        
        if (!client.validerEmail()) {
            SwingUtils.afficherErreur(this, "L'email semble invalide.", "Erreur de validation");
        }

        hotel.ajouterClient(client);
        
        // Sauvegarder automatiquement dans SQLite
        SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
        dbManager.sauvegarderHotel(hotel);
        
        actualiser();
        reinitialiserFormulaire();
        SwingUtils.afficherSucces(this, "Client ajouté avec succès.", "Succès");
    }

    private void modifierClient() {
        int row = tableClients.getSelectedRow();
        if (row == -1) {
            SwingUtils.afficherErreur(this, "Veuillez sélectionner un client à modifier.", "Erreur");
            return;
        }

        if (!validerFormulaire()) {
            return;
        }

        int numeroClient = (Integer) modelTable.getValueAt(row, 0);
        Client client = hotel.rechercherClient(numeroClient);

        if (client != null) {
            client.setNom(champNom.getText().trim());
            client.setPrenom(champPrenom.getText().trim());
            client.setEmail(champEmail.getText().trim());
            client.setTelephone(champTelephone.getText().trim());
            
            // Sauvegarder automatiquement dans SQLite
            SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
            dbManager.sauvegarderHotel(hotel);
            
            actualiser();
            reinitialiserFormulaire();
            SwingUtils.afficherSucces(this, "Client modifié avec succès.", "Succès");
        }
    }

    private void supprimerClient() {
        int row = tableClients.getSelectedRow();
        if (row == -1) {
            SwingUtils.afficherErreur(this, "Veuillez sélectionner un client à supprimer.", "Erreur");
            return;
        }

        int numeroClient = (Integer) modelTable.getValueAt(row, 0);
        Client client = hotel.rechercherClient(numeroClient);

        if (client != null) {
            boolean confirmer = SwingUtils.demanderConfirmation(
                this,
                "Êtes-vous sûr de vouloir supprimer le client " + client.getNomComplet() + " ?",
                "Confirmation de suppression"
            );

            if (confirmer) {
                hotel.getClients().remove(client);
                
                // Sauvegarder automatiquement dans SQLite
                SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
                dbManager.sauvegarderHotel(hotel);
                
                actualiser();
                SwingUtils.afficherSucces(this, "Client supprimé avec succès.", "Succès");
            }
        }
    }

    private void rechercher() {
        String recherche = champRecherche.getText().trim().toLowerCase();
        if (recherche.isEmpty()) {
            actualiser();
            return;
        }

        modelTable.setRowCount(0);
        int count = 0;
        for (Client client : hotel.getClients()) {
            if (client.getNom().toLowerCase().contains(recherche) ||
                client.getPrenom().toLowerCase().contains(recherche) ||
                client.getEmail().toLowerCase().contains(recherche)) {
                Object[] row = {
                    client.getNumeroClient(),
                    client.getNom(),
                    client.getPrenom(),
                    client.getEmail(),
                    client.getTelephone()
                };
                modelTable.addRow(row);
                count++;
            }
        }
        fenetrePrincipale.mettreAJourStatusBar(count + " client(s) trouvé(s)");
    }

    private boolean validerFormulaire() {
        return SwingUtils.validerChampNonVide(champNom, "Nom") &&
               SwingUtils.validerChampNonVide(champPrenom, "Prénom") &&
               SwingUtils.validerChampNonVide(champEmail, "Email");
    }

    private void reinitialiserFormulaire() {
        champNom.setText("");
        champPrenom.setText("");
        champEmail.setText("");
        champTelephone.setText("");
        tableClients.clearSelection();
    }
}

