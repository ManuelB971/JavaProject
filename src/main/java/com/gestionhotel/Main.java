package com.gestionhotel;

import com.gestionhotel.core.Hotel;
import com.gestionhotel.ui.MenuPrincipal;
import com.gestionhotel.ui.FenetrePrincipale;
import com.gestionhotel.utils.FilePersistence;
import com.gestionhotel.utils.SQLiteDatabaseManager;
import com.gestionhotel.utils.DataInitializer;
import javax.swing.SwingUtilities;

/**
 * Classe principale de l'application.
 * Initialise l'hôtel, charge les données et démarre le menu interactif.
 * Supporte deux modes : console et interface graphique Swing.
 * 
 * Usage :
 *   - Sans argument ou avec "swing" : lance l'interface graphique
 *   - Avec "console" : lance l'interface console
 * 
 * @author Dev 3 (Phase 4 - Try-catch global) et Dev 4 (Phase 4 - Swing)
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
            
            // Essayer de charger depuis SQLite d'abord
            SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
            boolean chargeDepuisSQLite = false;
            
            if (dbManager.baseExiste()) {
                if (dbManager.chargerHotel(hotel)) {
                    // Vérifier si la base est vide
                    if (dbManager.baseEstVide()) {
                        System.out.println("ℹ️  Base de données vide. Initialisation avec des données de test...");
                        DataInitializer.initialiserEtSauvegarder(hotel);
                        System.out.println("✅ Données de test initialisées dans la base de données.");
                    } else {
                        System.out.println("✅ Données chargées depuis SQLite avec succès.");
                        chargeDepuisSQLite = true;
                    }
                } else {
                    System.err.println("⚠️  Erreur lors du chargement depuis SQLite.");
                }
            }
            
            // Si pas de données SQLite, essayer les fichiers texte
            if (!chargeDepuisSQLite && FilePersistence.fichiersExistent()) {
                if (FilePersistence.chargerHotel(hotel)) {
                    System.out.println("✅ Données chargées depuis fichiers texte avec succès.");
                    // Migrer vers SQLite
                    dbManager.sauvegarderHotel(hotel);
                    System.out.println("✅ Données migrées vers SQLite.");
                    chargeDepuisSQLite = true;
                } else {
                    System.err.println("⚠️  Erreur lors du chargement des données.");
                }
            }
            
            // Si aucune donnée, initialiser avec des données de test dans la BDD
            if (!chargeDepuisSQLite && !FilePersistence.fichiersExistent()) {
                System.out.println("ℹ️  Aucune donnée existante trouvée.");
                System.out.println("📦 Initialisation avec des données de test dans la base de données...");
                DataInitializer.initialiserEtSauvegarder(hotel);
                System.out.println("✅ Données de test initialisées dans la base de données.");
            }

            // Déterminer le mode d'interface
            String mode = "swing";
            if (args.length > 0) {
                String arg1 = args[0].toLowerCase();
                if (arg1.equals("console")) {
                    mode = "console";
                }
            }

            if (mode.equals("console")) {
                demarrerConsole(hotel);
            } else {
                demarrerSwing(hotel);
            }
            
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

    /**
     * Démarre l'application en mode console.
     * 
     * @param hotel L'hôtel à gérer
     */
    private static void demarrerConsole(Hotel hotel) {
        try {
            System.out.println("");
            MenuPrincipal menu = new MenuPrincipal(hotel);
            menu.demarrer();

            // Sauvegarde automatique à la fermeture
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   SAUVEGARDE ET FERMETURE             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("Sauvegarde des données...");
            
            // Sauvegarder dans SQLite
            SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
            if (dbManager.sauvegarderHotel(hotel)) {
                System.out.println("✅ Données sauvegardées dans SQLite avec succès.");
            } else {
                System.err.println("⚠️  Erreur lors de la sauvegarde dans SQLite.");
            }
            
            System.out.println("\nMerci d'avoir utilisé notre système. À bientôt !");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'exécution en mode console : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Démarre l'application en mode interface graphique Swing.
     * 
     * @param hotel L'hôtel à gérer
     */
    private static void demarrerSwing(Hotel hotel) {
        SwingUtilities.invokeLater(() -> {
            try {
                FenetrePrincipale fenetre = new FenetrePrincipale(hotel);
                fenetre.setVisible(true);
                System.out.println("✅ Interface graphique lancée.");
            } catch (Exception e) {
                System.err.println("❌ Erreur lors du lancement de l'interface graphique : " + e.getMessage());
                e.printStackTrace();
                // En cas d'erreur, basculer vers le mode console
                System.out.println("Basculage vers le mode console...");
                demarrerConsole(hotel);
            }
        });
    }
}
