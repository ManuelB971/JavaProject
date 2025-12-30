package main.java.com.gestionhotel.ui;

import java.util.ArrayList;
import java.util.Scanner;
import main.java.com.gestionhotel.core.Hotel;
import main.java.com.gestionhotel.core.Statistiques;
import main.java.com.gestionhotel.model.Chambre;
import main.java.com.gestionhotel.model.ChambreSimple;
import main.java.com.gestionhotel.model.ChambreDouble;
import main.java.com.gestionhotel.model.Suite;
import main.java.com.gestionhotel.model.Client;
import main.java.com.gestionhotel.model.Reservation;
import main.java.com.gestionhotel.model.Service;

/**
 * Classe principale pour l'interface utilisateur en console.
 * Gère le menu principal et les sous-menus de l'application.
 * 
 * @author Dev 1 (Phase 4)
 */
public class MenuPrincipal {

    private Hotel hotel;
    private Scanner scanner;
    private boolean running;
    private Statistiques statistiques;

    /**
     * Constructeur du menu principal.
     * 
     * @param hotel L'hôtel à gérer
     */
    public MenuPrincipal(Hotel hotel) {
        this.hotel = hotel;
        this.scanner = new Scanner(System.in);
        this.running = true;
        this.statistiques = new Statistiques(hotel);
    }

    /**
     * Démarre l'application et affiche le menu principal.
     */
    public void demarrer() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SYSTÈME DE GESTION D'HÔTEL           ║");
        System.out.println("║   " + hotel.getNom() + "                          ║");
        System.out.println("╚════════════════════════════════════════╝");

        while (running) {
            afficherMenuPrincipal();
            int choix = lireChoix();
            traiterChoixPrincipal(choix);
        }

        System.out.println("Merci d'avoir utilisé le système. Au revoir !");
        scanner.close();
    }

    /**
     * Affiche le menu principal.
     */
    private void afficherMenuPrincipal() {
        System.out.println("\n========== MENU PRINCIPAL ==========");
        System.out.println("1. Gestion des Chambres");
        System.out.println("2. Gestion des Clients");
        System.out.println("3. Gestion des Réservations");
        System.out.println("4. Gestion des Services");
        System.out.println("5. Statistiques et Rapports");
        System.out.println("6. Quitter");
        System.out.println("=====================================");
        System.out.print("Votre choix : ");
    }

    /**
     * Lit le choix de l'utilisateur.
     * 
     * @return Le choix saisi
     */
    private int lireChoix() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Traite le choix du menu principal.
     * 
     * @param choix Le choix de l'utilisateur
     */
    private void traiterChoixPrincipal(int choix) {
        switch (choix) {
            case 1:
                menuGestionChambres();
                break;
            case 2:
                menuGestionClients();
                break;
            case 3:
                menuGestionReservations();
                break;
            case 4:
                menuGestionServices();
                break;
            case 5:
                menuStatistiques();
                break;
            case 6:
                running = false;
                break;
            default:
                System.out.println("Choix invalide. Veuillez réessayer.");
        }
    }

    // ===========================
    // SOUS-MENU 1 : GESTION DES CHAMBRES (Dev 1)
    // ===========================

    /**
     * Affiche et gère le sous-menu de gestion des chambres.
     */
    private void menuGestionChambres() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n----- Gestion des Chambres -----");
            System.out.println("1. Ajouter une chambre");
            System.out.println("2. Afficher toutes les chambres");
            System.out.println("3. Afficher les chambres disponibles");
            System.out.println("4. Rechercher une chambre par numéro");
            System.out.println("5. Rechercher par type");
            System.out.println("6. Rechercher par prix maximum");
            System.out.println("7. Retour au menu principal");
            System.out.print("Votre choix : ");

            int choix = lireChoix();
            switch (choix) {
                case 1:
                    ajouterChambre();
                    break;
                case 2:
                    hotel.afficherToutesLesChambres();
                    break;
                case 3:
                    hotel.afficherChambresDisponibles();
                    break;
                case 4:
                    rechercherChambreParNumero();
                    break;
                case 5:
                    rechercherChambreParType();
                    break;
                case 6:
                    rechercherChambreParPrix();
                    break;
                case 7:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    /**
     * Ajoute une nouvelle chambre.
     */
    private void ajouterChambre() {
        System.out.println("\n--- Ajouter une chambre ---");
        System.out.println("Type de chambre :");
        System.out.println("1. Simple (50€/nuit, 1 pers)");
        System.out.println("2. Double (80€/nuit, 2 pers)");
        System.out.println("3. Suite (150€/nuit, 4 pers)");
        System.out.print("Votre choix : ");

        int type = lireChoix();
        System.out.print("Numéro de la chambre : ");
        int numero = lireChoix();

        if (hotel.rechercherChambre(numero) != null) {
            System.out.println("Erreur: Une chambre avec ce numéro existe déjà.");
            return;
        }

        Chambre chambre = null;
        switch (type) {
            case 1:
                chambre = new ChambreSimple(numero);
                break;
            case 2:
                System.out.print("Lits jumeaux ? (oui/non) : ");
                String lits = scanner.nextLine().trim().toLowerCase();
                boolean litsJumeaux = lits.equals("oui") || lits.equals("o");
                chambre = new ChambreDouble(numero, litsJumeaux);
                break;
            case 3:
                System.out.print("Avec jacuzzi ? (oui/non) : ");
                String jacuzziStr = scanner.nextLine().trim().toLowerCase();
                boolean jacuzzi = jacuzziStr.equals("oui") || jacuzziStr.equals("o");
                System.out.print("Avec balcon ? (oui/non) : ");
                String balconStr = scanner.nextLine().trim().toLowerCase();
                boolean balcon = balconStr.equals("oui") || balconStr.equals("o");
                chambre = new Suite(numero, jacuzzi, balcon);
                break;
            default:
                System.out.println("Type invalide.");
                return;
        }

        hotel.ajouterChambre(chambre);
        System.out.println("Chambre ajoutée avec succès : " + chambre);
    }

    /**
     * Recherche une chambre par son numéro.
     */
    private void rechercherChambreParNumero() {
        System.out.print("Numéro de la chambre : ");
        int numero = lireChoix();
        Chambre chambre = hotel.rechercherChambre(numero);
        if (chambre != null) {
            System.out.println("Chambre trouvée : " + chambre);
        } else {
            System.out.println("Aucune chambre trouvée avec ce numéro.");
        }
    }

    /**
     * Recherche des chambres par type.
     */
    private void rechercherChambreParType() {
        System.out.print("Type de chambre (Simple/Double/Suite) : ");
        String type = scanner.nextLine().trim();
        ArrayList<Chambre> resultats = hotel.rechercherChambresParType(type);
        if (resultats.isEmpty()) {
            System.out.println("Aucune chambre de type '" + type + "' trouvée.");
        } else {
            System.out.println("=== Chambres de type " + type + " ===");
            for (Chambre c : resultats) {
                System.out.println(c);
            }
        }
    }

    /**
     * Recherche des chambres par prix maximum.
     */
    private void rechercherChambreParPrix() {
        System.out.print("Prix maximum par nuit (€) : ");
        try {
            double prixMax = Double.parseDouble(scanner.nextLine().trim());
            ArrayList<Chambre> resultats = hotel.rechercherChambresParPrix(prixMax);
            if (resultats.isEmpty()) {
                System.out.println("Aucune chambre trouvée à ce prix.");
            } else {
                System.out.println("=== Chambres à " + prixMax + "€ max/nuit ===");
                for (Chambre c : resultats) {
                    System.out.println(c);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Prix invalide.");
        }
    }

    // ===========================
    // SOUS-MENU 2 : GESTION DES CLIENTS (Dev 1)
    // ===========================

    /**
     * Affiche et gère le sous-menu de gestion des clients.
     */
    private void menuGestionClients() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n----- Gestion des Clients -----");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Afficher tous les clients");
            System.out.println("3. Rechercher un client");
            System.out.println("4. Modifier les informations d'un client");
            System.out.println("5. Retour au menu principal");
            System.out.print("Votre choix : ");

            int choix = lireChoix();
            switch (choix) {
                case 1:
                    ajouterClient();
                    break;
                case 2:
                    hotel.afficherTousLesClients();
                    break;
                case 3:
                    rechercherClient();
                    break;
                case 4:
                    modifierClient();
                    break;
                case 5:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    /**
     * Ajoute un nouveau client.
     */
    private void ajouterClient() {
        System.out.println("\n--- Ajouter un client ---");
        System.out.print("Nom : ");
        String nom = scanner.nextLine().trim();
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine().trim();
        System.out.print("Email : ");
        String email = scanner.nextLine().trim();
        System.out.print("Téléphone : ");
        String telephone = scanner.nextLine().trim();

        Client client = new Client(nom, prenom, email, telephone);
        
        if (!client.validerEmail()) {
            System.out.println("Attention: L'email semble invalide.");
        }
        
        hotel.ajouterClient(client);
        System.out.println("Client ajouté avec succès : " + client);
    }

    /**
     * Recherche un client.
     */
    private void rechercherClient() {
        System.out.println("Rechercher par :");
        System.out.println("1. Numéro client");
        System.out.println("2. Email");
        System.out.print("Votre choix : ");

        int choix = lireChoix();
        Client client = null;

        switch (choix) {
            case 1:
                System.out.print("Numéro du client : ");
                int numero = lireChoix();
                client = hotel.rechercherClient(numero);
                break;
            case 2:
                System.out.print("Email du client : ");
                String email = scanner.nextLine().trim();
                client = hotel.rechercherClientParEmail(email);
                break;
            default:
                System.out.println("Choix invalide.");
                return;
        }

        if (client != null) {
            System.out.println("Client trouvé : " + client);
        } else {
            System.out.println("Aucun client trouvé.");
        }
    }

    /**
     * Modifie les informations d'un client.
     */
    private void modifierClient() {
        System.out.print("Numéro du client à modifier : ");
        int numero = lireChoix();
        Client client = hotel.rechercherClient(numero);

        if (client == null) {
            System.out.println("Client non trouvé.");
            return;
        }

        System.out.println("Client actuel : " + client);
        System.out.println("Que souhaitez-vous modifier ?");
        System.out.println("1. Nom");
        System.out.println("2. Prénom");
        System.out.println("3. Email");
        System.out.println("4. Téléphone");
        System.out.print("Votre choix : ");

        int choix = lireChoix();
        switch (choix) {
            case 1:
                System.out.print("Nouveau nom : ");
                client.setNom(scanner.nextLine().trim());
                break;
            case 2:
                System.out.print("Nouveau prénom : ");
                client.setPrenom(scanner.nextLine().trim());
                break;
            case 3:
                System.out.print("Nouvel email : ");
                client.setEmail(scanner.nextLine().trim());
                break;
            case 4:
                System.out.print("Nouveau téléphone : ");
                client.setTelephone(scanner.nextLine().trim());
                break;
            default:
                System.out.println("Choix invalide.");
                return;
        }
        System.out.println("Client modifié : " + client);
    }

    // ===========================
    // SOUS-MENU 3 : GESTION DES RÉSERVATIONS (Dev 2/3)
    // ===========================

    /**
     * Affiche et gère le sous-menu de gestion des réservations.
     */
    private void menuGestionReservations() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n----- Gestion des Réservations -----");
            System.out.println("1. Créer une réservation");
            System.out.println("2. Afficher toutes les réservations");
            System.out.println("3. Afficher les réservations d'un client");
            System.out.println("4. Rechercher une réservation");
            System.out.println("5. Ajouter des services à une réservation");
            System.out.println("6. Annuler une réservation");
            System.out.println("7. Terminer une réservation (check-out)");
            System.out.println("8. Retour au menu principal");
            System.out.print("Votre choix : ");

            int choix = lireChoix();
            switch (choix) {
                case 1:
                    creerReservation();
                    break;
                case 2:
                    hotel.afficherToutesLesReservations();
                    break;
                case 3:
                    afficherReservationsClient();
                    break;
                case 4:
                    rechercherReservation();
                    break;
                case 5:
                    ajouterServicesReservation();
                    break;
                case 6:
                    annulerReservation();
                    break;
                case 7:
                    terminerReservation();
                    break;
                case 8:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    /**
     * Crée une nouvelle réservation.
     */
    private void creerReservation() {
        System.out.println("\n--- Créer une réservation ---");
        
        // Sélection du client
        System.out.print("Numéro du client : ");
        int numClient = lireChoix();
        Client client = hotel.rechercherClient(numClient);
        if (client == null) {
            System.out.println("Client non trouvé. Veuillez d'abord créer le client.");
            return;
        }

        // Afficher les chambres disponibles
        hotel.afficherChambresDisponibles();
        System.out.print("Numéro de la chambre : ");
        int numChambre = lireChoix();
        Chambre chambre = hotel.rechercherChambre(numChambre);
        if (chambre == null) {
            System.out.println("Chambre non trouvée.");
            return;
        }
        if (chambre.isOccupee()) {
            System.out.println("Cette chambre est déjà occupée.");
            return;
        }

        // Dates
        System.out.print("Date d'arrivée (jj/mm/aaaa) : ");
        String dateDebut = scanner.nextLine().trim();
        System.out.print("Date de départ (jj/mm/aaaa) : ");
        String dateFin = scanner.nextLine().trim();

        Reservation reservation = hotel.creerReservation(client, chambre, dateDebut, dateFin);
        if (reservation != null) {
            System.out.println("Réservation créée avec succès !");
            System.out.println(reservation);
        }
    }

    /**
     * Affiche les réservations d'un client.
     */
    private void afficherReservationsClient() {
        System.out.print("Numéro du client : ");
        int numClient = lireChoix();
        Client client = hotel.rechercherClient(numClient);
        if (client == null) {
            System.out.println("Client non trouvé.");
            return;
        }
        hotel.afficherReservationsClient(client);
    }

    /**
     * Recherche une réservation par son numéro.
     */
    private void rechercherReservation() {
        System.out.print("Numéro de la réservation : ");
        int numero = lireChoix();
        Reservation reservation = hotel.rechercherReservation(numero);
        if (reservation != null) {
            System.out.println(reservation);
        } else {
            System.out.println("Réservation non trouvée.");
        }
    }

    /**
     * Ajoute des services à une réservation.
     */
    private void ajouterServicesReservation() {
        System.out.print("Numéro de la réservation : ");
        int numRes = lireChoix();
        Reservation reservation = hotel.rechercherReservation(numRes);
        if (reservation == null) {
            System.out.println("Réservation non trouvée.");
            return;
        }

        hotel.afficherServices();
        System.out.print("ID du service à ajouter : ");
        int idService = lireChoix();

        for (Service s : hotel.getServicesDisponibles()) {
            if (s.getIdService() == idService) {
                reservation.ajouterService(s);
                System.out.println("Service ajouté : " + s.getNom());
                System.out.println("Nouveau total : " + String.format("%.2f", reservation.calculerPrixTotal()) + "€");
                return;
            }
        }
        System.out.println("Service non trouvé.");
    }

    /**
     * Annule une réservation.
     */
    private void annulerReservation() {
        System.out.print("Numéro de la réservation à annuler : ");
        int numero = lireChoix();
        hotel.annulerReservation(numero);
    }

    /**
     * Termine une réservation (check-out).
     */
    private void terminerReservation() {
        System.out.print("Numéro de la réservation à terminer : ");
        int numero = lireChoix();
        hotel.terminerReservation(numero);
    }

    // ===========================
    // SOUS-MENU 4 : GESTION DES SERVICES (Dev 2/3)
    // ===========================

    /**
     * Affiche et gère le sous-menu de gestion des services.
     */
    private void menuGestionServices() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n----- Gestion des Services -----");
            System.out.println("1. Afficher les services disponibles");
            System.out.println("2. Ajouter un nouveau service");
            System.out.println("3. Modifier un service");
            System.out.println("4. Retour au menu principal");
            System.out.print("Votre choix : ");

            int choix = lireChoix();
            switch (choix) {
                case 1:
                    hotel.afficherServices();
                    break;
                case 2:
                    ajouterService();
                    break;
                case 3:
                    modifierService();
                    break;
                case 4:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    /**
     * Ajoute un nouveau service.
     */
    private void ajouterService() {
        System.out.println("\n--- Ajouter un service ---");
        System.out.print("Nom du service : ");
        String nom = scanner.nextLine().trim();
        System.out.print("Description : ");
        String description = scanner.nextLine().trim();
        System.out.print("Prix (€) : ");
        try {
            double prix = Double.parseDouble(scanner.nextLine().trim());
            Service service = new Service(nom, description, prix);
            hotel.ajouterService(service);
            System.out.println("Service ajouté : " + service);
        } catch (NumberFormatException e) {
            System.out.println("Prix invalide.");
        }
    }

    /**
     * Modifie un service existant.
     */
    private void modifierService() {
        hotel.afficherServices();
        System.out.print("ID du service à modifier : ");
        int id = lireChoix();

        for (Service s : hotel.getServicesDisponibles()) {
            if (s.getIdService() == id) {
                System.out.println("Service actuel : " + s);
                System.out.println("1. Modifier le nom");
                System.out.println("2. Modifier la description");
                System.out.println("3. Modifier le prix");
                System.out.print("Votre choix : ");

                int choix = lireChoix();
                switch (choix) {
                    case 1:
                        System.out.print("Nouveau nom : ");
                        s.setNom(scanner.nextLine().trim());
                        break;
                    case 2:
                        System.out.print("Nouvelle description : ");
                        s.setDescription(scanner.nextLine().trim());
                        break;
                    case 3:
                        System.out.print("Nouveau prix : ");
                        try {
                            s.setPrix(Double.parseDouble(scanner.nextLine().trim()));
                        } catch (NumberFormatException e) {
                            System.out.println("Prix invalide.");
                            return;
                        }
                        break;
                    default:
                        System.out.println("Choix invalide.");
                        return;
                }
                System.out.println("Service modifié : " + s);
                return;
            }
        }
        System.out.println("Service non trouvé.");
    }

    // ===========================
    // SOUS-MENU 5 : STATISTIQUES (Dev 4)
    // ===========================

    /**
     * Affiche et gère le sous-menu des statistiques.
     */
    private void menuStatistiques() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n----- Statistiques et Rapports -----");
            System.out.println("1. Afficher le chiffre d'affaires");
            System.out.println("2. Afficher le taux d'occupation");
            System.out.println("3. Afficher la chambre la plus réservée");
            System.out.println("4. Afficher les statistiques complètes");
            System.out.println("5. Retour au menu principal");
            System.out.print("Votre choix : ");

            int choix = lireChoix();
            switch (choix) {
                case 1:
                    afficherChiffreAffaires();
                    break;
                case 2:
                    afficherTauxOccupation();
                    break;
                case 3:
                    afficherChambreLaPlusReservee();
                    break;
                case 4:
                    afficherStatistiquesCompletes();
                    break;
                case 5:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    /**
     * Affiche le chiffre d'affaires total.
     */
    private void afficherChiffreAffaires() {
        double ca = statistiques.calculerChiffreAffaires();
        System.out.println("Chiffre d'affaires total : " + String.format("%.2f", ca) + "€");
    }

    /**
     * Affiche le taux d'occupation.
     */
    private void afficherTauxOccupation() {
        double taux = statistiques.calculerTauxOccupation();
        int total = hotel.getChambres().size();
        int occupees = 0;
        for (Chambre c : hotel.getChambres()) {
            if (c.isOccupee()) {
                occupees++;
            }
        }
        System.out.println("Taux d'occupation : " + String.format("%.1f", taux) + "% (" + occupees + "/" + total + " chambres)");
    }

    /**
     * Affiche la chambre la plus réservée.
     */
    private void afficherChambreLaPlusReservee() {
        Chambre chambre = statistiques.trouverChambreLaPlusReservee();
        if (chambre != null) {
            System.out.println("Chambre la plus réservée :");
            System.out.println(chambre);
        } else {
            System.out.println("Aucune réservation enregistrée.");
        }
    }

    /**
     * Affiche les statistiques complètes.
     */
    private void afficherStatistiquesCompletes() {
        statistiques.afficherRapportComplet();
    }
}

