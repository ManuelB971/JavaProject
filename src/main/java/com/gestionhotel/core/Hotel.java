package main.java.com.gestionhotel.core;

import java.util.ArrayList;
import main.java.com.gestionhotel.model.Chambre;
import main.java.com.gestionhotel.model.Client;
import main.java.com.gestionhotel.model.Reservation;
import main.java.com.gestionhotel.model.Service;

/**
 * Classe principale représentant l'hôtel.
 * Gère les chambres, clients, réservations et services.
 * 
 * @author Dev 1 (Phase 3)
 */
public class Hotel {

    // Attributs
    private String nom;
    private String adresse;
    private ArrayList<Chambre> chambres;
    private ArrayList<Client> clients;
    private ArrayList<Reservation> reservations;
    private ArrayList<Service> servicesDisponibles;

    /**
     * Constructeur de l'hôtel.
     * 
     * @param nom     Nom de l'hôtel
     * @param adresse Adresse de l'hôtel
     */
    public Hotel(String nom, String adresse) {
        this.nom = nom;
        this.adresse = adresse;
        this.chambres = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.servicesDisponibles = new ArrayList<>();
    }

    // ===========================
    // GETTERS & SETTERS
    // ===========================

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public ArrayList<Chambre> getChambres() {
        return chambres;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public ArrayList<Service> getServicesDisponibles() {
        return servicesDisponibles;
    }

    // ===========================
    // MÉTHODES DE GESTION DES CHAMBRES (Phase 3 - Dev 1)
    // ===========================

    /**
     * Ajoute une chambre à l'hôtel.
     * 
     * @param chambre La chambre à ajouter
     */
    public void ajouterChambre(Chambre chambre) {
        if (chambre != null && rechercherChambre(chambre.getNumero()) == null) {
            this.chambres.add(chambre);
        }
    }

    /**
     * Affiche toutes les chambres de l'hôtel.
     */
    public void afficherToutesLesChambres() {
        if (chambres.isEmpty()) {
            System.out.println("Aucune chambre enregistrée dans l'hôtel.");
            return;
        }
        System.out.println("=== Liste des chambres ===");
        for (Chambre chambre : chambres) {
            System.out.println(chambre);
        }
    }

    /**
     * Affiche les chambres disponibles (non occupées).
     */
    public void afficherChambresDisponibles() {
        ArrayList<Chambre> disponibles = getChambresDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("Aucune chambre disponible actuellement.");
            return;
        }
        System.out.println("=== Chambres disponibles ===");
        for (Chambre chambre : disponibles) {
            System.out.println(chambre);
        }
    }

    /**
     * Retourne la liste des chambres disponibles.
     * 
     * @return ArrayList des chambres non occupées
     */
    public ArrayList<Chambre> getChambresDisponibles() {
        ArrayList<Chambre> disponibles = new ArrayList<>();
        for (Chambre chambre : chambres) {
            if (!chambre.isOccupee()) {
                disponibles.add(chambre);
            }
        }
        return disponibles;
    }

    /**
     * Recherche une chambre par son numéro.
     * 
     * @param numero Le numéro de la chambre
     * @return La chambre trouvée ou null si non trouvée
     */
    public Chambre rechercherChambre(int numero) {
        for (Chambre chambre : chambres) {
            if (chambre.getNumero() == numero) {
                return chambre;
            }
        }
        return null;
    }

    /**
     * Recherche les chambres par type (Simple, Double, Suite).
     * 
     * @param type Le type de chambre recherché
     * @return ArrayList des chambres correspondant au type
     */
    public ArrayList<Chambre> rechercherChambresParType(String type) {
        ArrayList<Chambre> resultat = new ArrayList<>();
        for (Chambre chambre : chambres) {
            if (chambre.getType().equalsIgnoreCase(type)) {
                resultat.add(chambre);
            }
        }
        return resultat;
    }

    /**
     * Recherche les chambres dont le prix est inférieur ou égal au prix maximum.
     * 
     * @param prixMax Le prix maximum par nuit
     * @return ArrayList des chambres dans la fourchette de prix
     */
    public ArrayList<Chambre> rechercherChambresParPrix(double prixMax) {
        ArrayList<Chambre> resultat = new ArrayList<>();
        for (Chambre chambre : chambres) {
            if (chambre.getPrixParNuit() <= prixMax) {
                resultat.add(chambre);
            }
        }
        return resultat;
    }

    // ===========================
    // MÉTHODES DE GESTION DES CLIENTS (Phase 3 - Dev 2)
    // ===========================

    /**
     * Ajoute un client à l'hôtel.
     * 
     * @param client Le client à ajouter
     */
    public void ajouterClient(Client client) {
        if (client != null) {
            this.clients.add(client);
        }
    }

    /**
     * Affiche tous les clients de l'hôtel.
     */
    public void afficherTousLesClients() {
        if (clients.isEmpty()) {
            System.out.println("Aucun client enregistré.");
            return;
        }
        System.out.println("=== Liste des clients ===");
        for (Client client : clients) {
            System.out.println(client);
        }
    }

    /**
     * Recherche un client par son numéro.
     * 
     * @param numero Le numéro du client
     * @return Le client trouvé ou null
     */
    public Client rechercherClient(int numero) {
        for (Client client : clients) {
            if (client.getNumeroClient() == numero) {
                return client;
            }
        }
        return null;
    }

    /**
     * Recherche un client par son email.
     * 
     * @param email L'email du client
     * @return Le client trouvé ou null
     */
    public Client rechercherClientParEmail(String email) {
        for (Client client : clients) {
            if (client.getEmail().equalsIgnoreCase(email)) {
                return client;
            }
        }
        return null;
    }

    // ===========================
    // MÉTHODES DE GESTION DES RÉSERVATIONS (Phase 3 - Dev 3)
    // ===========================

    /**
     * Crée une nouvelle réservation.
     * 
     * @param client    Le client
     * @param chambre   La chambre
     * @param dateDebut Date de début
     * @param dateFin   Date de fin
     * @return La réservation créée ou null si la chambre est occupée
     */
    public Reservation creerReservation(Client client, Chambre chambre, String dateDebut, String dateFin) {
        if (chambre.isOccupee()) {
            System.out.println("Erreur: La chambre n°" + chambre.getNumero() + " est déjà occupée.");
            return null;
        }
        Reservation reservation = new Reservation(client, chambre, dateDebut, dateFin);
        this.reservations.add(reservation);
        return reservation;
    }

    /**
     * Affiche toutes les réservations.
     */
    public void afficherToutesLesReservations() {
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation enregistrée.");
            return;
        }
        System.out.println("=== Liste des réservations ===");
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
            System.out.println("---");
        }
    }

    /**
     * Affiche les réservations d'un client.
     * 
     * @param client Le client
     */
    public void afficherReservationsClient(Client client) {
        boolean found = false;
        System.out.println("=== Réservations de " + client.getNomComplet() + " ===");
        for (Reservation reservation : reservations) {
            if (reservation.getClient().getNumeroClient() == client.getNumeroClient()) {
                System.out.println(reservation);
                System.out.println("---");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Aucune réservation trouvée pour ce client.");
        }
    }

    /**
     * Recherche une réservation par son numéro.
     * 
     * @param numero Le numéro de réservation
     * @return La réservation trouvée ou null
     */
    public Reservation rechercherReservation(int numero) {
        for (Reservation reservation : reservations) {
            if (reservation.getNumeroReservation() == numero) {
                return reservation;
            }
        }
        return null;
    }

    /**
     * Annule une réservation par son numéro.
     * 
     * @param numero Le numéro de réservation
     */
    public void annulerReservation(int numero) {
        Reservation reservation = rechercherReservation(numero);
        if (reservation != null) {
            reservation.annuler();
            System.out.println("Réservation n°" + numero + " annulée avec succès.");
        } else {
            System.out.println("Réservation n°" + numero + " non trouvée.");
        }
    }

    /**
     * Termine une réservation (check-out).
     * 
     * @param numero Le numéro de réservation
     */
    public void terminerReservation(int numero) {
        Reservation reservation = rechercherReservation(numero);
        if (reservation != null) {
            reservation.terminer();
            System.out.println("Réservation n°" + numero + " terminée. Chambre libérée.");
        } else {
            System.out.println("Réservation n°" + numero + " non trouvée.");
        }
    }

    // ===========================
    // MÉTHODES DE GESTION DES SERVICES
    // ===========================

    /**
     * Ajoute un service disponible à l'hôtel.
     * 
     * @param service Le service à ajouter
     */
    public void ajouterService(Service service) {
        if (service != null) {
            this.servicesDisponibles.add(service);
        }
    }

    /**
     * Affiche tous les services disponibles.
     */
    public void afficherServices() {
        if (servicesDisponibles.isEmpty()) {
            System.out.println("Aucun service disponible.");
            return;
        }
        System.out.println("=== Services disponibles ===");
        for (Service service : servicesDisponibles) {
            System.out.println(service);
        }
    }

    @Override
    public String toString() {
        return String.format("Hôtel %s - %s\nChambres: %d | Clients: %d | Réservations: %d | Services: %d",
                nom, adresse, chambres.size(), clients.size(), reservations.size(), servicesDisponibles.size());
    }
}