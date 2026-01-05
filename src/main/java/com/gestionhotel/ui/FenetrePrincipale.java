package com.gestionhotel.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.gestionhotel.core.Hotel;
import com.gestionhotel.utils.SQLiteDatabaseManager;

/**
 * Fenêtre principale de l'application avec interface graphique Swing.
 * Contient tous les panneaux de gestion organisés en onglets.
 * 
 * @author Dev 4 (Phase 4)
 */
public class FenetrePrincipale extends JFrame {

    private Hotel hotel;
    private JTabbedPane onglets;
    private JLabel statusBar;
    
    // Panneaux
    private PanneauChambres panneauChambres;
    private PanneauClients panneauClients;
    private PanneauReservations panneauReservations;
    private PanneauServices panneauServices;
    private PanneauStatistiques panneauStatistiques;
    private PanneauFidelite panneauFidelite;

    /**
     * Constructeur de la fenêtre principale.
     * 
     * @param hotel L'hôtel à gérer
     */
    public FenetrePrincipale(Hotel hotel) {
        this.hotel = hotel;
        initialiserInterface();
    }

    /**
     * Initialise l'interface graphique.
     */
    private void initialiserInterface() {
        setTitle("Système de Gestion d'Hôtel - " + hotel.getNom());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Configuration du Look and Feel
        SwingUtils.configurerLookAndFeel();

        // Créer le menu bar
        creerMenuBar();

        // Créer les onglets
        creerOnglets();

        // Créer la barre de statut
        creerStatusBar();

        // Layout principal
        setLayout(new BorderLayout());
        add(onglets, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        // Gestion de la fermeture
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fermerApplication();
            }
        });

        mettreAJourStatusBar("Application prête");
    }

    /**
     * Crée la barre de menu.
     */
    private void creerMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Fichier
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem itemSauvegarder = new JMenuItem("Sauvegarder");
        itemSauvegarder.addActionListener(e -> sauvegarder());
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> fermerApplication());
        menuFichier.add(itemSauvegarder);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);

        // Menu Édition
        JMenu menuEdition = new JMenu("Édition");
        JMenuItem itemActualiser = new JMenuItem("Actualiser");
        itemActualiser.addActionListener(e -> actualiserTousLesPanneaux());
        menuEdition.add(itemActualiser);

        // Menu Aide
        JMenu menuAide = new JMenu("Aide");
        JMenuItem itemAPropos = new JMenuItem("À propos");
        itemAPropos.addActionListener(e -> afficherAPropos());
        menuAide.add(itemAPropos);

        menuBar.add(menuFichier);
        menuBar.add(menuEdition);
        menuBar.add(menuAide);

        setJMenuBar(menuBar);
    }

    /**
     * Crée les onglets avec tous les panneaux.
     */
    private void creerOnglets() {
        onglets = new JTabbedPane();

        // Créer les panneaux
        panneauChambres = new PanneauChambres(hotel, this);
        panneauClients = new PanneauClients(hotel, this);
        panneauReservations = new PanneauReservations(hotel, this);
        panneauServices = new PanneauServices(hotel, this);
        panneauStatistiques = new PanneauStatistiques(hotel, this);
        panneauFidelite = new PanneauFidelite(hotel, this);

        // Ajouter les onglets
        onglets.addTab("Chambres", panneauChambres);
        onglets.addTab("Clients", panneauClients);
        onglets.addTab("Réservations", panneauReservations);
        onglets.addTab("Services", panneauServices);
        onglets.addTab("Statistiques", panneauStatistiques);
        onglets.addTab("Fidélité", panneauFidelite);
    }

    /**
     * Crée la barre de statut.
     */
    private void creerStatusBar() {
        statusBar = new JLabel("Prêt");
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
    }

    /**
     * Met à jour le message de la barre de statut.
     * 
     * @param message Le message à afficher
     */
    public void mettreAJourStatusBar(String message) {
        if (statusBar != null) {
            statusBar.setText(message);
        }
    }

    /**
     * Actualise tous les panneaux.
     */
    public void actualiserTousLesPanneaux() {
        panneauChambres.actualiser();
        panneauClients.actualiser();
        panneauReservations.actualiser();
        panneauServices.actualiser();
        panneauStatistiques.actualiser();
        panneauFidelite.actualiser();
        mettreAJourStatusBar("Données actualisées");
    }

    /**
     * Sauvegarde les données de l'hôtel dans SQLite.
     */
    private void sauvegarder() {
        // Sauvegarder dans SQLite
        SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
        boolean succesSQLite = dbManager.sauvegarderHotel(hotel);
        
        if (succesSQLite) {
            SwingUtils.afficherSucces(this, "Données sauvegardées avec succès dans la base de données.", "Sauvegarde");
            mettreAJourStatusBar("Données sauvegardées");
        } else {
            SwingUtils.afficherErreur(this, "Erreur lors de la sauvegarde dans SQLite.", "Erreur");
            mettreAJourStatusBar("Erreur de sauvegarde");
        }
    }

    /**
     * Ferme l'application avec sauvegarde automatique dans SQLite.
     */
    private void fermerApplication() {
        // Sauvegarder automatiquement dans SQLite (sans demander)
        SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
        if (dbManager.sauvegarderHotel(hotel)) {
            System.out.println("✅ Données sauvegardées automatiquement dans SQLite.");
        } else {
            System.err.println("⚠️  Erreur lors de la sauvegarde automatique.");
            // Demander confirmation si la sauvegarde automatique échoue
            boolean reponse = SwingUtils.demanderConfirmation(
                this,
                "Erreur lors de la sauvegarde automatique.\nVoulez-vous réessayer ?",
                "Erreur de sauvegarde"
            );
            if (reponse) {
                sauvegarder();
            }
        }

        System.exit(0);
    }

    /**
     * Affiche la boîte de dialogue "À propos".
     */
    private void afficherAPropos() {
        String message = "Système de Gestion d'Hôtel\n\n" +
                        "Version 1.0\n" +
                        "Développé par l'équipe Dev 4\n\n" +
                        "© 2026 - Tous droits réservés";
        SwingUtils.afficherMessage(this, message, "À propos");
    }

    /**
     * Obtient l'hôtel géré par cette fenêtre.
     * 
     * @return L'hôtel
     */
    public Hotel getHotel() {
        return hotel;
    }
}

