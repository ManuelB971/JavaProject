package com.gestionhotel.ui;

import javax.swing.*;
import java.awt.*;
import com.gestionhotel.core.Hotel;
import com.gestionhotel.model.Client;
import com.gestionhotel.utils.FideliteManager;

/**
 * Panneau d'affichage et de gestion du programme de fidélité.
 * Affiche les informations de fidélité d'un client sélectionné.
 * 
 * @author Dev 4 (Phase 4)
 */
public class PanneauFidelite extends JPanel {

    private Hotel hotel;
    private FenetrePrincipale fenetrePrincipale;
    private JComboBox<Client> comboClient;
    private JLabel labelStatut;
    private JLabel labelNombreReservations;
    private JLabel labelDepenseTotale;
    private JLabel labelReduction;
    private JLabel labelEconomies;
    private JLabel labelProgression;
    private JTextArea textAreaOffres;
    private JButton btnActualiser;

    public PanneauFidelite(Hotel hotel, FenetrePrincipale fenetrePrincipale) {
        this.hotel = hotel;
        this.fenetrePrincipale = fenetrePrincipale;
        initialiserInterface();
        actualiser();
    }

    private void initialiserInterface() {
        setLayout(new BorderLayout());

        // Panneau principal
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Sélection du client
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(new JLabel("Sélectionner un client :"), gbc);
        gbc.gridx = 1;
        comboClient = new JComboBox<>();
        comboClient.addActionListener(e -> afficherInformationsFidelite());
        panelPrincipal.add(comboClient, gbc);
        gbc.gridx = 0;

        // Titre
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel titre = new JLabel("Informations de Fidélité");
        titre.setFont(new Font(titre.getFont().getName(), Font.BOLD, 18));
        panelPrincipal.add(titre, gbc);
        gbc.gridwidth = 1;

        // Statut
        gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Statut de fidélité :"), gbc);
        gbc.gridx = 1;
        labelStatut = new JLabel();
        labelStatut.setFont(new Font(labelStatut.getFont().getName(), Font.BOLD, 16));
        panelPrincipal.add(labelStatut, gbc);
        gbc.gridx = 0;

        // Nombre de réservations
        gbc.gridy = 3;
        panelPrincipal.add(new JLabel("Nombre de réservations :"), gbc);
        gbc.gridx = 1;
        labelNombreReservations = new JLabel();
        panelPrincipal.add(labelNombreReservations, gbc);
        gbc.gridx = 0;

        // Dépense totale
        gbc.gridy = 4;
        panelPrincipal.add(new JLabel("Dépense totale :"), gbc);
        gbc.gridx = 1;
        labelDepenseTotale = new JLabel();
        labelDepenseTotale.setFont(new Font(labelDepenseTotale.getFont().getName(), Font.BOLD, 14));
        labelDepenseTotale.setForeground(new Color(0, 128, 0));
        panelPrincipal.add(labelDepenseTotale, gbc);
        gbc.gridx = 0;

        // Réduction
        gbc.gridy = 5;
        panelPrincipal.add(new JLabel("Réduction applicable :"), gbc);
        gbc.gridx = 1;
        labelReduction = new JLabel();
        panelPrincipal.add(labelReduction, gbc);
        gbc.gridx = 0;

        // Économies
        gbc.gridy = 6;
        panelPrincipal.add(new JLabel("Économies réalisées :"), gbc);
        gbc.gridx = 1;
        labelEconomies = new JLabel();
        labelEconomies.setFont(new Font(labelEconomies.getFont().getName(), Font.BOLD, 14));
        labelEconomies.setForeground(new Color(0, 128, 0));
        panelPrincipal.add(labelEconomies, gbc);
        gbc.gridx = 0;

        // Progression
        gbc.gridy = 7;
        panelPrincipal.add(new JLabel("Progression :"), gbc);
        gbc.gridx = 1;
        labelProgression = new JLabel();
        panelPrincipal.add(labelProgression, gbc);
        gbc.gridx = 0;

        // Offres spéciales
        gbc.gridy = 8;
        panelPrincipal.add(new JLabel("Offres spéciales :"), gbc);
        gbc.gridx = 1;
        textAreaOffres = new JTextArea(6, 30);
        textAreaOffres.setEditable(false);
        textAreaOffres.setLineWrap(true);
        textAreaOffres.setWrapStyleWord(true);
        textAreaOffres.setBackground(getBackground());
        JScrollPane scrollOffres = new JScrollPane(textAreaOffres);
        panelPrincipal.add(scrollOffres, gbc);
        gbc.gridx = 0;

        // Bouton actualiser
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        btnActualiser = new JButton("Actualiser");
        btnActualiser.addActionListener(e -> actualiser());
        panelPrincipal.add(btnActualiser, gbc);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void actualiser() {
        comboClient.removeAllItems();
        for (Client client : hotel.getClients()) {
            comboClient.addItem(client);
        }

        if (comboClient.getItemCount() > 0) {
            comboClient.setSelectedIndex(0);
            afficherInformationsFidelite();
        } else {
            reinitialiserAffichage();
        }
    }

    private void afficherInformationsFidelite() {
        Client client = (Client) comboClient.getSelectedItem();
        if (client == null) {
            reinitialiserAffichage();
            return;
        }

        // Calculer les informations
        int nombreReservations = FideliteManager.obtenirNombreReservations(client, hotel);
        double depenseTotale = FideliteManager.obtenirDepenseTotale(client, hotel);
        String statut = FideliteManager.calculerStatutFidelite(client, hotel);
        double reduction = FideliteManager.calculerReduction(client, hotel);
        double economies = FideliteManager.calculerEconomiesTotales(client, hotel);
        int reservationsPourNiveauSuperieur = FideliteManager.obtenirReservationsPourNiveauSuperieur(client, hotel);

        // Afficher les informations
        labelStatut.setText(statut);
        labelStatut.setForeground(SwingUtils.obtenirCouleurFidelite(statut));

        labelNombreReservations.setText(String.valueOf(nombreReservations));
        labelDepenseTotale.setText(SwingUtils.formaterPrix(depenseTotale));
        labelReduction.setText(String.format("%.1f%%", reduction));

        if (reduction > 0) {
            labelEconomies.setText(SwingUtils.formaterPrix(economies));
        } else {
            labelEconomies.setText("Aucune");
        }

        if (reservationsPourNiveauSuperieur > 0) {
            labelProgression.setText(reservationsPourNiveauSuperieur + 
                " réservation(s) pour atteindre le niveau supérieur");
        } else if (statut.equals("Platine")) {
            labelProgression.setText("🏆 Niveau maximum atteint !");
        } else {
            labelProgression.setText("--");
        }

        // Afficher les offres spéciales
        StringBuilder offres = new StringBuilder();
        if (nombreReservations >= 1) {
            offres.append("• Upgrade gratuit vers une chambre supérieure\n");
        }
        if (nombreReservations >= 3) {
            offres.append("• Séjour gratuit pour toute réservation de 5+ nuits\n");
            offres.append("• Service de conciergerie offert\n");
        }
        if (nombreReservations >= 5) {
            offres.append("• Suite offerte pour 1 nuit par an\n");
            offres.append("• Petit-déjeuner gratuit illimité\n");
            offres.append("• Service VIP prioritaire\n");
        }
        if (offres.length() == 0) {
            offres.append("Aucune offre spéciale disponible.\n");
            offres.append("Effectuez votre première réservation pour débloquer des avantages !");
        }
        textAreaOffres.setText(offres.toString());

        fenetrePrincipale.mettreAJourStatusBar("Informations de fidélité affichées pour " + client.getNomComplet());
    }

    private void reinitialiserAffichage() {
        labelStatut.setText("--");
        labelNombreReservations.setText("--");
        labelDepenseTotale.setText("--");
        labelReduction.setText("--");
        labelEconomies.setText("--");
        labelProgression.setText("--");
        textAreaOffres.setText("");
    }
}

