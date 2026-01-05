package com.gestionhotel.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Classe utilitaire pour les composants Swing réutilisables.
 * Fournit des méthodes pour créer des composants communs et des dialogues.
 * 
 * @author Dev 4 (Phase 4)
 */
public class SwingUtils {

    /**
     * Formate un prix en euros avec 2 décimales.
     * 
     * @param prix Le prix à formater
     * @return Le prix formaté (ex: "125.50€")
     */
    public static String formaterPrix(double prix) {
        return String.format("%.2f", prix) + "€";
    }

    /**
     * Affiche un message d'information.
     * 
     * @param parent Le composant parent
     * @param message Le message à afficher
     * @param titre Le titre de la boîte de dialogue
     */
    public static void afficherMessage(Component parent, String message, String titre) {
        JOptionPane.showMessageDialog(parent, message, titre, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Affiche un message d'erreur.
     * 
     * @param parent Le composant parent
     * @param message Le message d'erreur
     * @param titre Le titre de la boîte de dialogue
     */
    public static void afficherErreur(Component parent, String message, String titre) {
        JOptionPane.showMessageDialog(parent, message, titre, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Affiche un message de succès.
     * 
     * @param parent Le composant parent
     * @param message Le message de succès
     * @param titre Le titre de la boîte de dialogue
     */
    public static void afficherSucces(Component parent, String message, String titre) {
        JOptionPane.showMessageDialog(parent, message, titre, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Demande une confirmation à l'utilisateur.
     * 
     * @param parent Le composant parent
     * @param message Le message de confirmation
     * @param titre Le titre de la boîte de dialogue
     * @return true si l'utilisateur confirme, false sinon
     */
    public static boolean demanderConfirmation(Component parent, String message, String titre) {
        int reponse = JOptionPane.showConfirmDialog(
            parent,
            message,
            titre,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return reponse == JOptionPane.YES_OPTION;
    }

    /**
     * Crée un JTable avec un modèle non éditable par défaut.
     * 
     * @param colonnes Les noms des colonnes
     * @return Un JTable configuré
     */
    public static JTable creerTable(String[] colonnes) {
        DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return table;
    }

    /**
     * Crée un JTable avec un modèle personnalisé.
     * 
     * @param model Le modèle de table
     * @return Un JTable configuré
     */
    public static JTable creerTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return table;
    }

    /**
     * Valide qu'un champ texte n'est pas vide.
     * 
     * @param champ Le champ à valider
     * @param nomChamp Le nom du champ (pour le message d'erreur)
     * @return true si valide, false sinon
     */
    public static boolean validerChampNonVide(JTextField champ, String nomChamp) {
        if (champ.getText().trim().isEmpty()) {
            afficherErreur(champ, "Le champ '" + nomChamp + "' ne peut pas être vide.", "Erreur de validation");
            champ.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Valide qu'un champ contient un nombre valide.
     * 
     * @param champ Le champ à valider
     * @param nomChamp Le nom du champ
     * @return Le nombre si valide, null sinon
     */
    public static Double validerNombre(JTextField champ, String nomChamp) {
        try {
            return Double.parseDouble(champ.getText().trim());
        } catch (NumberFormatException e) {
            afficherErreur(champ, "Le champ '" + nomChamp + "' doit contenir un nombre valide.", "Erreur de validation");
            champ.requestFocus();
            return null;
        }
    }

    /**
     * Valide qu'un champ contient un entier valide.
     * 
     * @param champ Le champ à valider
     * @param nomChamp Le nom du champ
     * @return L'entier si valide, null sinon
     */
    public static Integer validerEntier(JTextField champ, String nomChamp) {
        try {
            return Integer.parseInt(champ.getText().trim());
        } catch (NumberFormatException e) {
            afficherErreur(champ, "Le champ '" + nomChamp + "' doit contenir un nombre entier valide.", "Erreur de validation");
            champ.requestFocus();
            return null;
        }
    }

    /**
     * Crée un panneau avec un layout BorderLayout et ajoute un composant au centre.
     * 
     * @param composant Le composant à ajouter
     * @return Le panneau créé
     */
    public static JPanel creerPanneauCentre(Component composant) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(composant, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crée un panneau avec un layout FlowLayout.
     * 
     * @param composants Les composants à ajouter
     * @return Le panneau créé
     */
    public static JPanel creerPanneauBoutons(Component... composants) {
        JPanel panel = new JPanel(new FlowLayout());
        for (Component comp : composants) {
            panel.add(comp);
        }
        return panel;
    }

    /**
     * Configure le Look and Feel du système.
     */
    public static void configurerLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Erreur lors de la configuration du Look and Feel : " + e.getMessage());
        }
    }

    /**
     * Obtient la couleur associée à un statut de fidélité.
     * 
     * @param statut Le statut de fidélité
     * @return La couleur correspondante
     */
    public static Color obtenirCouleurFidelite(String statut) {
        switch (statut) {
            case "Platine":
                return new Color(229, 228, 226); // #E5E4E2
            case "Or":
                return new Color(255, 215, 0);   // #FFD700
            case "Argent":
                return new Color(192, 192, 192); // #C0C0C0
            case "Bronze":
                return new Color(205, 127, 50);  // #CD7F32
            default:
                return Color.GRAY;
        }
    }
}

