package com.gestionhotel.utils;

import com.gestionhotel.core.Hotel;
import com.gestionhotel.model.ChambreSimple;
import com.gestionhotel.model.ChambreDouble;
import com.gestionhotel.model.Suite;
import com.gestionhotel.model.Client;
import com.gestionhotel.model.Service;
import com.gestionhotel.model.Reservation;

/**
 * Classe utilitaire pour initialiser la base de données avec des données de test.
 * Crée un hôtel complet avec chambres, clients, services et réservations.
 * 
 * @author Dev 4 (Phase 4)
 */
public class DataInitializer {

    /**
     * Initialise un hôtel avec des données de test complètes.
     * 
     * @param hotel L'hôtel à remplir avec les données de test
     */
    public static void initialiserDonneesTest(Hotel hotel) {
        System.out.println("📦 Initialisation des données de test...");
        
        // Réinitialiser l'hôtel
        hotel.setNom("L'hotel le magnifique");
        hotel.setAdresse("123 Avenue des Champs-Élysées, 75008 Paris");
        
        // ============================================================
        // CHAMBRES
        // ============================================================
        System.out.println("   → Création des chambres...");
        
        // Chambres simples
        hotel.ajouterChambre(new ChambreSimple(1));
        hotel.ajouterChambre(new ChambreSimple(2));
        hotel.ajouterChambre(new ChambreSimple(3));
        hotel.ajouterChambre(new ChambreSimple(4));
        hotel.ajouterChambre(new ChambreSimple(5));
        
        // Chambres doubles
        hotel.ajouterChambre(new ChambreDouble(6, false));  // Lit double
        hotel.ajouterChambre(new ChambreDouble(7, true));   // Lits jumeaux
        hotel.ajouterChambre(new ChambreDouble(8, false));
        hotel.ajouterChambre(new ChambreDouble(9, true));
        hotel.ajouterChambre(new ChambreDouble(10, false));
        
        // Suites
        hotel.ajouterChambre(new Suite(11, true, true));   // Jacuzzi + Balcon
        hotel.ajouterChambre(new Suite(12, true, false));  // Jacuzzi seulement
        hotel.ajouterChambre(new Suite(13, false, true));   // Balcon seulement
        hotel.ajouterChambre(new Suite(14, true, true));   // Jacuzzi + Balcon
        
        // ============================================================
        // CLIENTS
        // ============================================================
        System.out.println("   → Création des clients...");
        
        Client client1 = new Client("Dupont", "Jean", "jean.dupont@email.com", "01 23 45 67 89");
        Client client2 = new Client("Martin", "Marie", "marie.martin@email.com", "01 98 76 54 32");
        Client client3 = new Client("Bernard", "Pierre", "pierre.bernard@email.com", "02 11 22 33 44");
        Client client4 = new Client("Dubois", "Sophie", "sophie.dubois@email.com", "02 44 55 66 77");
        Client client5 = new Client("Moreau", "Luc", "luc.moreau@email.com", "03 12 34 56 78");
        Client client6 = new Client("Lefebvre", "Julie", "julie.lefebvre@email.com", "03 87 65 43 21");
        Client client7 = new Client("Garcia", "Antoine", "antoine.garcia@email.com", "04 11 22 33 44");
        Client client8 = new Client("Roux", "Camille", "camille.roux@email.com", "04 55 66 77 88");
        
        hotel.ajouterClient(client1);
        hotel.ajouterClient(client2);
        hotel.ajouterClient(client3);
        hotel.ajouterClient(client4);
        hotel.ajouterClient(client5);
        hotel.ajouterClient(client6);
        hotel.ajouterClient(client7);
        hotel.ajouterClient(client8);
        
        // ============================================================
        // SERVICES
        // ============================================================
        System.out.println("   → Création des services...");
        
        Service service1 = new Service("Petit-déjeuner", "Petit-déjeuner buffet continental", 15.0);
        Service service2 = new Service("Petit-déjeuner premium", "Petit-déjeuner avec produits locaux", 25.0);
        Service service3 = new Service("Spa", "Séance de spa et massage", 80.0);
        Service service4 = new Service("Room service", "Service de chambre 24/7", 10.0);
        Service service5 = new Service("Parking", "Stationnement sécurisé", 12.0);
        Service service6 = new Service("WiFi premium", "Connexion haut débit illimitée", 5.0);
        Service service7 = new Service("Blanchisserie", "Service de blanchisserie express", 20.0);
        Service service8 = new Service("Navette aéroport", "Transfert aéroport-hôtel", 50.0);
        
        hotel.ajouterService(service1);
        hotel.ajouterService(service2);
        hotel.ajouterService(service3);
        hotel.ajouterService(service4);
        hotel.ajouterService(service5);
        hotel.ajouterService(service6);
        hotel.ajouterService(service7);
        hotel.ajouterService(service8);
        
        // ============================================================
        // RÉSERVATIONS
        // ============================================================
        System.out.println("   → Création des réservations...");
        
        // Réservation 1 : Client 1 - Chambre Simple - 3 nuits
        Reservation res1 = hotel.creerReservation(client1, hotel.rechercherChambre(1), 
            "15/01/2024", "18/01/2024");
        if (res1 != null) {
            res1.confirmer();
            res1.ajouterService(service1); // Petit-déjeuner
            res1.ajouterService(service6); // WiFi premium
        }
        
        // Réservation 2 : Client 2 - Chambre Double - 5 nuits
        Reservation res2 = hotel.creerReservation(client2, hotel.rechercherChambre(6), 
            "20/01/2024", "25/01/2024");
        if (res2 != null) {
            res2.confirmer();
            res2.ajouterService(service2); // Petit-déjeuner premium
            res2.ajouterService(service5); // Parking
        }
        
        // Réservation 3 : Client 3 - Suite - 2 nuits
        Reservation res3 = hotel.creerReservation(client3, hotel.rechercherChambre(11), 
            "10/02/2024", "12/02/2024");
        if (res3 != null) {
            res3.confirmer();
            res3.ajouterService(service3); // Spa
            res3.ajouterService(service2); // Petit-déjeuner premium
        }
        
        // Réservation 4 : Client 1 - Chambre Simple - 4 nuits (client fidèle)
        Reservation res4 = hotel.creerReservation(client1, hotel.rechercherChambre(2), 
            "01/03/2024", "05/03/2024");
        if (res4 != null) {
            res4.confirmer();
            res4.ajouterService(service1);
        }
        
        // Réservation 5 : Client 4 - Chambre Double - 7 nuits
        Reservation res5 = hotel.creerReservation(client4, hotel.rechercherChambre(7), 
            "15/03/2024", "22/03/2024");
        if (res5 != null) {
            res5.confirmer();
            res5.ajouterService(service1);
            res5.ajouterService(service4); // Room service
            res5.ajouterService(service7); // Blanchisserie
        }
        
        // Réservation 6 : Client 5 - Suite - 1 nuit
        Reservation res6 = hotel.creerReservation(client5, hotel.rechercherChambre(12), 
            "25/03/2024", "26/03/2024");
        if (res6 != null) {
            res6.confirmer();
            res6.ajouterService(service3); // Spa
        }
        
        // Réservation 7 : Client 1 - Chambre Double - 3 nuits (client très fidèle)
        Reservation res7 = hotel.creerReservation(client1, hotel.rechercherChambre(8), 
            "10/04/2024", "13/04/2024");
        if (res7 != null) {
            res7.confirmer();
            res7.ajouterService(service2);
            res7.ajouterService(service5);
        }
        
        // Réservation 8 : Client 6 - Chambre Simple - 2 nuits
        Reservation res8 = hotel.creerReservation(client6, hotel.rechercherChambre(3), 
            "20/04/2024", "22/04/2024");
        if (res8 != null) {
            res8.confirmer();
            res8.ajouterService(service1);
        }
        
        // Réservation 9 : Client 2 - Suite - 4 nuits (client fidèle)
        Reservation res9 = hotel.creerReservation(client2, hotel.rechercherChambre(13), 
            "01/05/2024", "05/05/2024");
        if (res9 != null) {
            res9.confirmer();
            res9.ajouterService(service2);
            res9.ajouterService(service3);
            res9.ajouterService(service8); // Navette aéroport
        }
        
        // Réservation 10 : Client 1 - Suite - 5 nuits (client très fidèle - niveau Or/Platine)
        Reservation res10 = hotel.creerReservation(client1, hotel.rechercherChambre(14), 
            "15/05/2024", "20/05/2024");
        if (res10 != null) {
            res10.confirmer();
            res10.ajouterService(service2);
            res10.ajouterService(service3);
            res10.ajouterService(service4);
        }
        
        // Réservation 11 : Client 7 - Chambre Double - 6 nuits
        Reservation res11 = hotel.creerReservation(client7, hotel.rechercherChambre(9), 
            "25/05/2024", "31/05/2024");
        if (res11 != null) {
            res11.confirmer();
            res11.ajouterService(service1);
            res11.ajouterService(service5);
        }
        
        // Réservation 12 : Client 8 - Chambre Simple - 1 nuit
        Reservation res12 = hotel.creerReservation(client8, hotel.rechercherChambre(4), 
            "05/06/2024", "06/06/2024");
        if (res12 != null) {
            res12.confirmer();
        }
        
        // Réservation 13 : Client 1 - Chambre Simple - 2 nuits (client très fidèle)
        Reservation res13 = hotel.creerReservation(client1, hotel.rechercherChambre(5), 
            "10/06/2024", "12/06/2024");
        if (res13 != null) {
            res13.confirmer();
            res13.ajouterService(service1);
        }
        
        // Réservation annulée pour tester
        Reservation resAnnulee = hotel.creerReservation(client3, hotel.rechercherChambre(10), 
            "20/06/2024", "23/06/2024");
        if (resAnnulee != null) {
            resAnnulee.annuler("Changement de plans");
        }
        
        // Réservation terminée pour tester
        Reservation resTerminee = hotel.creerReservation(client4, hotel.rechercherChambre(1), 
            "01/01/2024", "05/01/2024");
        if (resTerminee != null) {
            resTerminee.confirmer();
            resTerminee.terminer(); // Check-out effectué
        }
        
        System.out.println("✅ Données de test initialisées :");
        System.out.println("   - " + hotel.getChambres().size() + " chambres");
        System.out.println("   - " + hotel.getClients().size() + " clients");
        System.out.println("   - " + hotel.getServicesDisponibles().size() + " services");
        System.out.println("   - " + hotel.getReservations().size() + " réservations");
        System.out.println("");
    }

    /**
     * Initialise les données de test et sauvegarde dans SQLite.
     * 
     * @param hotel L'hôtel à initialiser
     * @return true si l'initialisation et la sauvegarde ont réussi
     */
    public static boolean initialiserEtSauvegarder(Hotel hotel) {
        initialiserDonneesTest(hotel);
        
        // Sauvegarder dans SQLite
        SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
        boolean succes = dbManager.sauvegarderHotel(hotel);
        
        if (succes) {
            System.out.println("✅ Données sauvegardées dans SQLite (data/hotel.db)");
        } else {
            System.out.println("⚠️  Erreur lors de la sauvegarde dans SQLite");
        }
        
        return succes;
    }
}

