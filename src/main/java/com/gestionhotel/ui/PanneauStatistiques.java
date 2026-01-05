package com.gestionhotel.ui;

import javax.swing.*;
import java.awt.*;
import com.gestionhotel.core.Hotel;
import com.gestionhotel.core.Statistiques;
import com.gestionhotel.model.Chambre;
import com.gestionhotel.model.Client;
import com.gestionhotel.model.Service;

/**
 * Panneau d'affichage des statistiques de l'hôtel.
 * Affiche le chiffre d'affaires, le taux d'occupation, et d'autres statistiques.
 * 
 * @author Dev 4 (Phase 4)
 */
public class PanneauStatistiques extends JPanel {

    private Hotel hotel;
    private FenetrePrincipale fenetrePrincipale;
    private Statistiques statistiques;
    private JLabel labelCA;
    private JLabel labelTauxOccupation;
    private JLabel labelChambrePlusReservee;
    private JLabel labelClientPlusFidele;
    private JLabel labelServicePlusUtilise;
    private JLabel labelRevenuMoyen;
    private JLabel labelNuitsMoyennes;
    private JButton btnActualiser;

    public PanneauStatistiques(Hotel hotel, FenetrePrincipale fenetrePrincipale) {
        this.hotel = hotel;
        this.fenetrePrincipale = fenetrePrincipale;
        this.statistiques = new Statistiques(hotel);
        initialiserInterface();
        actualiser();
    }

    private void initialiserInterface() {
        setLayout(new BorderLayout());

        // Panneau principal avec scroll
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Titre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titre = new JLabel("Statistiques de l'Hôtel");
        titre.setFont(new Font(titre.getFont().getName(), Font.BOLD, 18));
        panelPrincipal.add(titre, gbc);
        gbc.gridwidth = 1;

        // Informations générales
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel sousTitre1 = new JLabel("=== Informations Générales ===");
        sousTitre1.setFont(new Font(sousTitre1.getFont().getName(), Font.BOLD, 14));
        panelPrincipal.add(sousTitre1, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Nombre de chambres :"), gbc);
        gbc.gridx = 1;
        panelPrincipal.add(new JLabel(String.valueOf(hotel.getChambres().size())), gbc);
        gbc.gridx = 0;

        gbc.gridy = 3;
        panelPrincipal.add(new JLabel("Nombre de clients :"), gbc);
        gbc.gridx = 1;
        panelPrincipal.add(new JLabel(String.valueOf(hotel.getClients().size())), gbc);
        gbc.gridx = 0;

        gbc.gridy = 4;
        panelPrincipal.add(new JLabel("Nombre de réservations :"), gbc);
        gbc.gridx = 1;
        panelPrincipal.add(new JLabel(String.valueOf(hotel.getReservations().size())), gbc);
        gbc.gridx = 0;

        gbc.gridy = 5;
        panelPrincipal.add(new JLabel("Nombre de services :"), gbc);
        gbc.gridx = 1;
        panelPrincipal.add(new JLabel(String.valueOf(hotel.getServicesDisponibles().size())), gbc);
        gbc.gridx = 0;

        // Chiffre d'affaires
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JLabel sousTitre2 = new JLabel("=== Chiffre d'Affaires ===");
        sousTitre2.setFont(new Font(sousTitre2.getFont().getName(), Font.BOLD, 14));
        panelPrincipal.add(sousTitre2, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 7;
        panelPrincipal.add(new JLabel("Chiffre d'affaires total :"), gbc);
        gbc.gridx = 1;
        labelCA = new JLabel();
        labelCA.setFont(new Font(labelCA.getFont().getName(), Font.BOLD, 14));
        labelCA.setForeground(new Color(0, 128, 0));
        panelPrincipal.add(labelCA, gbc);
        gbc.gridx = 0;

        gbc.gridy = 8;
        panelPrincipal.add(new JLabel("Revenu moyen par réservation :"), gbc);
        gbc.gridx = 1;
        labelRevenuMoyen = new JLabel();
        panelPrincipal.add(labelRevenuMoyen, gbc);
        gbc.gridx = 0;

        // Occupation
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        JLabel sousTitre3 = new JLabel("=== Occupation ===");
        sousTitre3.setFont(new Font(sousTitre3.getFont().getName(), Font.BOLD, 14));
        panelPrincipal.add(sousTitre3, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 10;
        panelPrincipal.add(new JLabel("Taux d'occupation :"), gbc);
        gbc.gridx = 1;
        labelTauxOccupation = new JLabel();
        panelPrincipal.add(labelTauxOccupation, gbc);
        gbc.gridx = 0;

        gbc.gridy = 11;
        panelPrincipal.add(new JLabel("Nombre moyen de nuits :"), gbc);
        gbc.gridx = 1;
        labelNuitsMoyennes = new JLabel();
        panelPrincipal.add(labelNuitsMoyennes, gbc);
        gbc.gridx = 0;

        // Analyses
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        JLabel sousTitre4 = new JLabel("=== Analyses ===");
        sousTitre4.setFont(new Font(sousTitre4.getFont().getName(), Font.BOLD, 14));
        panelPrincipal.add(sousTitre4, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 13;
        panelPrincipal.add(new JLabel("Chambre la plus réservée :"), gbc);
        gbc.gridx = 1;
        labelChambrePlusReservee = new JLabel();
        panelPrincipal.add(labelChambrePlusReservee, gbc);
        gbc.gridx = 0;

        gbc.gridy = 14;
        panelPrincipal.add(new JLabel("Client le plus fidèle :"), gbc);
        gbc.gridx = 1;
        labelClientPlusFidele = new JLabel();
        panelPrincipal.add(labelClientPlusFidele, gbc);
        gbc.gridx = 0;

        gbc.gridy = 15;
        panelPrincipal.add(new JLabel("Service le plus utilisé :"), gbc);
        gbc.gridx = 1;
        labelServicePlusUtilise = new JLabel();
        panelPrincipal.add(labelServicePlusUtilise, gbc);
        gbc.gridx = 0;

        // Bouton actualiser
        gbc.gridy = 16;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        btnActualiser = new JButton("Actualiser les statistiques");
        btnActualiser.addActionListener(e -> actualiser());
        btnActualiser.setFont(new Font(btnActualiser.getFont().getName(), Font.BOLD, 12));
        panelPrincipal.add(btnActualiser, gbc);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void actualiser() {
        // Chiffre d'affaires
        double ca = statistiques.calculerChiffreAffaires();
        labelCA.setText(SwingUtils.formaterPrix(ca));

        double revenuMoyen = statistiques.calculerRevenuMoyenParReservation();
        labelRevenuMoyen.setText(SwingUtils.formaterPrix(revenuMoyen));

        // Occupation
        double tauxOccupation = statistiques.calculerTauxOccupation();
        int chambresOccupees = 0;
        for (Chambre c : hotel.getChambres()) {
            if (c.isOccupee()) {
                chambresOccupees++;
            }
        }
        labelTauxOccupation.setText(String.format("%.1f%% (%d/%d chambres)", 
            tauxOccupation, chambresOccupees, hotel.getChambres().size()));

        double nuitsMoyennes = statistiques.calculerNombreMoyenNuits();
        labelNuitsMoyennes.setText(String.format("%.1f nuits", nuitsMoyennes));

        // Analyses
        Chambre chambrePlusReservee = statistiques.trouverChambreLaPlusReservee();
        if (chambrePlusReservee != null) {
            labelChambrePlusReservee.setText("Ch. " + chambrePlusReservee.getNumero() + 
                " (" + chambrePlusReservee.getType() + ")");
        } else {
            labelChambrePlusReservee.setText("Aucune");
        }

        Client clientPlusFidele = statistiques.trouverClientLePlusFidele();
        if (clientPlusFidele != null) {
            labelClientPlusFidele.setText(clientPlusFidele.getNomComplet());
        } else {
            labelClientPlusFidele.setText("Aucun");
        }

        Service servicePlusUtilise = statistiques.trouverServiceLePlusUtilise();
        if (servicePlusUtilise != null) {
            labelServicePlusUtilise.setText(servicePlusUtilise.getNom());
        } else {
            labelServicePlusUtilise.setText("Aucun");
        }

        fenetrePrincipale.mettreAJourStatusBar("Statistiques actualisées");
    }
}

