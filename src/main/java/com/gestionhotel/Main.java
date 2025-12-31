package com.gestionhotel;

import com.gestionhotel.core.Hotel;
import com.gestionhotel.ui.MenuPrincipal;
import com.gestionhotel.utils.FilePersistence;

/**
 * Main de l'application
 */
public class Main {
    public static void main(String[] args) {

        Hotel hotel = new Hotel("Hôtel Le Magnifique", "123 Avenue des Champs-Élysées, Paris");

        System.out.println("Chargement des données en cours...");
        if (FilePersistence.fichiersExistent()) {
            if (FilePersistence.chargerHotel(hotel)) {
                System.out.println("Données chargées avec succès.");
            } else {
                System.err.println("Erreur lors du chargement des données.");
            }
        } else {
            System.out.println("Aucune donnée existante trouvée. Démarrage d'un nouvel hôtel.");
        }

        MenuPrincipal menu = new MenuPrincipal(hotel);
        menu.demarrer();

        // Sauvegarde automatique à la fermeture
        System.out.println("Sauvegarde des données...");
        if (FilePersistence.sauvegarderHotel(hotel)) {
            System.out.println("Données sauvegardées.");
        }
    }
}
