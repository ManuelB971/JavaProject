package com.gestionhotel.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import com.gestionhotel.core.Hotel;
import com.gestionhotel.model.Client;
import com.gestionhotel.model.Chambre;
import com.gestionhotel.model.Reservation;
import com.gestionhotel.model.Service;
import com.gestionhotel.utils.FideliteManager;
import com.gestionhotel.utils.SQLiteDatabaseManager;

/**
 * Panneau de gestion des réservations avec interface graphique.
 * Permet de créer, annuler, terminer des réservations et d'ajouter des services.
 * Affiche le prix avec réduction de fidélité.
 * 
 * @author Dev 4 (Phase 4)
 */
public class PanneauReservations extends JPanel {

    private Hotel hotel;
    private FenetrePrincipale fenetrePrincipale;
    private JTable tableReservations;
    private DefaultTableModel modelTable;
    private JComboBox<Client> comboClient;
    private JComboBox<Chambre> comboChambre;
    private JTextField champDateDebut;
    private JTextField champDateFin;
    private JList<Service> listeServices;
    private DefaultListModel<Service> modelListeServices;
    private JLabel labelPrixTotal;
    private JLabel labelPrixAvecFidelite;
    private JLabel labelReduction;

    public PanneauReservations(Hotel hotel, FenetrePrincipale fenetrePrincipale) {
        this.hotel = hotel;
        this.fenetrePrincipale = fenetrePrincipale;
        initialiserInterface();
        actualiser();
    }

    private void initialiserInterface() {
        setLayout(new BorderLayout());

        // Panneau supérieur : Table
        String[] colonnes = {"N°", "Client", "Chambre", "Date début", "Date fin", "Statut", "Prix total"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableReservations = SwingUtils.creerTable(modelTable);
        JScrollPane scrollPane = new JScrollPane(tableReservations);

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
        panel.setBorder(BorderFactory.createTitledBorder("Formulaire Réservation"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Client
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Client :"), gbc);
        gbc.gridx = 1;
        comboClient = new JComboBox<>();
        comboClient.setPreferredSize(new Dimension(300, 25));
        comboClient.addActionListener(e -> mettreAJourPrix());
        panel.add(comboClient, gbc);

        // Chambre
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Chambre :"), gbc);
        gbc.gridx = 1;
        comboChambre = new JComboBox<>();
        comboChambre.setPreferredSize(new Dimension(300, 25));
        comboChambre.addActionListener(e -> mettreAJourPrix());
        panel.add(comboChambre, gbc);

        // Date début
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Date début (jj/mm/aaaa) :"), gbc);
        gbc.gridx = 1;
        champDateDebut = new JTextField(20);
        champDateDebut.addActionListener(e -> mettreAJourPrix());
        panel.add(champDateDebut, gbc);

        // Date fin
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Date fin (jj/mm/aaaa) :"), gbc);
        gbc.gridx = 1;
        champDateFin = new JTextField(20);
        champDateFin.addActionListener(e -> mettreAJourPrix());
        panel.add(champDateFin, gbc);

        // Services
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Services :"), gbc);
        gbc.gridx = 1;
        modelListeServices = new DefaultListModel<>();
        listeServices = new JList<>(modelListeServices);
        listeServices.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollServices = new JScrollPane(listeServices);
        scrollServices.setPreferredSize(new Dimension(200, 100));
        panel.add(scrollServices, gbc);

        // Prix
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel panelPrix = new JPanel(new GridLayout(3, 1, 5, 5));
        labelPrixTotal = new JLabel("Prix total : --");
        labelPrixAvecFidelite = new JLabel("Prix avec fidélité : --");
        labelReduction = new JLabel("");
        labelReduction.setForeground(Color.GREEN);
        panelPrix.add(labelPrixTotal);
        panelPrix.add(labelPrixAvecFidelite);
        panelPrix.add(labelReduction);
        panel.add(panelPrix, gbc);

        // Boutons formulaire
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel panelBoutonsForm = new JPanel(new FlowLayout());
        JButton btnCreer = new JButton("Créer réservation");
        btnCreer.addActionListener(e -> creerReservation());
        JButton btnReinitialiser = new JButton("Réinitialiser");
        btnReinitialiser.addActionListener(e -> reinitialiserFormulaire());
        panelBoutonsForm.add(btnCreer);
        panelBoutonsForm.add(btnReinitialiser);
        panel.add(panelBoutonsForm, gbc);

        actualiserComboBoxes();

        return panel;
    }

    private JPanel creerBoutons() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton btnAnnuler = new JButton("Annuler réservation");
        btnAnnuler.addActionListener(e -> annulerReservation());
        JButton btnTerminer = new JButton("Terminer réservation");
        btnTerminer.addActionListener(e -> terminerReservation());
        panel.add(btnAnnuler);
        panel.add(btnTerminer);
        return panel;
    }

    private void actualiserComboBoxes() {
        comboClient.removeAllItems();
        for (Client client : hotel.getClients()) {
            comboClient.addItem(client);
        }
        
        // Personnaliser l'affichage des clients dans le ComboBox
        comboClient.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Client) {
                    Client cl = (Client) value;
                    setText("N°" + cl.getNumeroClient() + " - " + cl.getNomComplet() + 
                        " (" + cl.getEmail() + ")");
                } else if (value == null) {
                    setText("Sélectionner un client...");
                }
                return c;
            }
        });

        comboChambre.removeAllItems();
        ArrayList<Chambre> chambresDisponibles = hotel.getChambresDisponibles();
        for (Chambre chambre : chambresDisponibles) {
            comboChambre.addItem(chambre);
        }
        
        // Personnaliser l'affichage des chambres dans le ComboBox
        comboChambre.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Chambre) {
                    Chambre ch = (Chambre) value;
                    setText(hotel.formaterNumeroChambre(ch.getNumero()) + " - " + ch.getType() + 
                        " (" + SwingUtils.formaterPrix(ch.getPrixParNuit()) + "/nuit)");
                } else if (value == null) {
                    setText("Sélectionner une chambre...");
                }
                return c;
            }
        });
        
        // Afficher un message si aucune chambre disponible
        if (chambresDisponibles.isEmpty()) {
            // Ne pas ajouter null, mais afficher un message dans le status bar
            fenetrePrincipale.mettreAJourStatusBar("Aucune chambre disponible. Créez des chambres dans l'onglet Chambres.");
        }
        
        // Afficher un message si aucun client
        if (hotel.getClients().isEmpty()) {
            fenetrePrincipale.mettreAJourStatusBar("Aucun client enregistré. Créez des clients dans l'onglet Clients.");
        }

        modelListeServices.clear();
        for (Service service : hotel.getServicesDisponibles()) {
            if (service.isDisponible()) {
                modelListeServices.addElement(service);
            }
        }
    }

    public void actualiser() {
        modelTable.setRowCount(0);
        for (Reservation reservation : hotel.getReservations()) {
            Object[] row = {
                reservation.getNumeroReservation(),
                reservation.getClient().getNomComplet(),
                hotel.formaterNumeroChambre(reservation.getChambre().getNumero()) + " (" + reservation.getChambre().getType() + ")",
                reservation.getDateDebut(),
                reservation.getDateFin(),
                reservation.getStatut(),
                SwingUtils.formaterPrix(reservation.calculerPrixTotal())
            };
            modelTable.addRow(row);
        }
        fenetrePrincipale.mettreAJourStatusBar(hotel.getReservations().size() + " réservation(s)");
        
        // Actualiser les ComboBox seulement s'ils sont déjà initialisés
        if (comboClient != null && comboChambre != null) {
            actualiserComboBoxes();
        }
    }

    private void mettreAJourPrix() {
        Client client = (Client) comboClient.getSelectedItem();
        Chambre chambre = (Chambre) comboChambre.getSelectedItem();
        String dateDebut = champDateDebut.getText().trim();
        String dateFin = champDateFin.getText().trim();

        if (client == null || chambre == null || dateDebut.isEmpty() || dateFin.isEmpty()) {
            labelPrixTotal.setText("Prix total : --");
            labelPrixAvecFidelite.setText("Prix avec fidélité : --");
            labelReduction.setText("");
            return;
        }

        try {
            // Créer une réservation temporaire pour calculer le prix
            Reservation tempRes = new Reservation(client, chambre, dateDebut, dateFin);
            
            // Ajouter les services sélectionnés
            for (Service service : listeServices.getSelectedValuesList()) {
                tempRes.ajouterService(service);
            }

            double prixTotal = tempRes.calculerPrixTotal();
            double prixAvecFidelite = tempRes.calculerPrixTotalAvecFidelite(hotel);
            double reduction = FideliteManager.calculerReduction(client, hotel);
            String statut = FideliteManager.calculerStatutFidelite(client, hotel);

            labelPrixTotal.setText("Prix total : " + SwingUtils.formaterPrix(prixTotal));
            labelPrixAvecFidelite.setText("Prix avec fidélité : " + SwingUtils.formaterPrix(prixAvecFidelite));

            if (reduction > 0) {
                double economie = prixTotal - prixAvecFidelite;
                labelReduction.setText("Réduction " + statut + " (-" + String.format("%.1f", reduction) + 
                    "%) : -" + SwingUtils.formaterPrix(economie));
            } else {
                labelReduction.setText("Aucune réduction (statut : " + statut + ")");
            }
        } catch (Exception e) {
            labelPrixTotal.setText("Prix total : Erreur de calcul");
            labelPrixAvecFidelite.setText("Prix avec fidélité : --");
            labelReduction.setText("");
        }
    }

    private void creerReservation() {
        Object clientObj = comboClient.getSelectedItem();
        Object chambreObj = comboChambre.getSelectedItem();
        String dateDebut = champDateDebut.getText().trim();
        String dateFin = champDateFin.getText().trim();

        if (clientObj == null || !(clientObj instanceof Client)) {
            SwingUtils.afficherErreur(this, "Veuillez sélectionner un client.\n" +
                "Si aucun client n'apparaît, créez d'abord un client dans l'onglet Clients.", "Erreur");
            return;
        }
        
        Client client = (Client) clientObj;

        if (chambreObj == null || !(chambreObj instanceof Chambre)) {
            SwingUtils.afficherErreur(this, "Veuillez sélectionner une chambre.\n" +
                "Si aucune chambre n'apparaît, créez d'abord une chambre dans l'onglet Chambres.", "Erreur");
            return;
        }
        
        Chambre chambre = (Chambre) chambreObj;

        if (dateDebut.isEmpty() || dateFin.isEmpty()) {
            SwingUtils.afficherErreur(this, "Veuillez remplir les dates.", "Erreur");
            return;
        }

        Reservation reservation = hotel.creerReservationAvecFidelite(client, chambre, dateDebut, dateFin);

        if (reservation != null) {
            // Ajouter les services sélectionnés
            for (Service service : listeServices.getSelectedValuesList()) {
                reservation.ajouterService(service);
            }

            // Sauvegarder automatiquement dans SQLite
            SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
            dbManager.sauvegarderHotel(hotel);

            actualiser();
            reinitialiserFormulaire();
            SwingUtils.afficherSucces(this, "Réservation créée avec succès.", "Succès");
        }
    }

    private void annulerReservation() {
        int row = tableReservations.getSelectedRow();
        if (row == -1) {
            SwingUtils.afficherErreur(this, "Veuillez sélectionner une réservation à annuler.", "Erreur");
            return;
        }

        int numero = (Integer) modelTable.getValueAt(row, 0);
        Reservation reservation = hotel.rechercherReservation(numero);

        if (reservation != null) {
            String raison = JOptionPane.showInputDialog(this, "Raison de l'annulation (optionnel) :", "Annulation");
            hotel.annulerReservation(numero, raison);
            
            // Sauvegarder automatiquement dans SQLite
            SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
            dbManager.sauvegarderHotel(hotel);
            
            actualiser();
            SwingUtils.afficherSucces(this, "Réservation annulée avec succès.", "Succès");
        }
    }

    private void terminerReservation() {
        int row = tableReservations.getSelectedRow();
        if (row == -1) {
            SwingUtils.afficherErreur(this, "Veuillez sélectionner une réservation à terminer.", "Erreur");
            return;
        }

        int numero = (Integer) modelTable.getValueAt(row, 0);
        hotel.terminerReservation(numero);
        
        // Sauvegarder automatiquement dans SQLite
        SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
        dbManager.sauvegarderHotel(hotel);
        
        actualiser();
        SwingUtils.afficherSucces(this, "Réservation terminée avec succès.", "Succès");
    }

    private void reinitialiserFormulaire() {
        comboClient.setSelectedIndex(-1);
        comboChambre.setSelectedIndex(-1);
        champDateDebut.setText("");
        champDateFin.setText("");
        listeServices.clearSelection();
        labelPrixTotal.setText("Prix total : --");
        labelPrixAvecFidelite.setText("Prix avec fidélité : --");
        labelReduction.setText("");
        tableReservations.clearSelection();
    }
}

