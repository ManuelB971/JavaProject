package com.gestionhotel;

import com.gestionhotel.core.Hotel;
import com.gestionhotel.ui.MenuPrincipal;
import com.gestionhotel.utils.FilePersistence;

/**
 * Classe principale de l'application.
 * Initialise l'hôtel, charge les données et démarre le menu interactif.
 * Implémente une gestion robuste des erreurs globales.
 * 
 * @author Dev 3 (Phase 4 - Try-catch global)
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Initialisation de l'hôtel
            Hotel hotel = new Hotel("Hôtel Le Magnifique", "123 Avenue des Champs-Élysées, Paris");

            // Chargement des données persistantes
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║   DÉMARRAGE DE L'APPLICATION           ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("\nChargement des données en cours...");
            
            if (FilePersistence.fichiersExistent()) {
                if (FilePersistence.chargerHotel(hotel)) {
                    System.out.println("✅ Données chargées avec succès.");
                } else {
                    System.err.println("⚠️  Erreur lors du chargement des données. Démarrage avec des données vierges.");
                }
            } else {
                System.out.println("ℹ️  Aucune donnée existante trouvée. Démarrage d'un nouvel hôtel.");
            }

            // Démarrage du menu principal
            System.out.println("");
            MenuPrincipal menu = new MenuPrincipal(hotel);
            menu.demarrer();

            // Sauvegarde automatique à la fermeture
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   SAUVEGARDE ET FERMETURE             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("Sauvegarde des données...");
            if (FilePersistence.sauvegarderHotel(hotel)) {
                System.out.println("✅ Données sauvegardées avec succès.");
            } else {
                System.err.println("⚠️  Erreur lors de la sauvegarde des données.");
            }
            System.out.println("\nMerci d'avoir utilisé notre système. À bientôt !");
            
        } catch (NullPointerException e) {
            System.err.println("❌ Erreur critique : Une ressource requise n'a pas pu être initialisée.");
            System.err.println("   Détail : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Erreur critique lors du démarrage de l'application :");
            System.err.println("   " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
