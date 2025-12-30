package com.gestionhotel.core;

import java.util.ArrayList;
import com.gestionhotel.model.Chambre;
import com.gestionhotel.model.Client;
import com.gestionhotel.model.Reservation;
import com.gestionhotel.model.Service;
import com.gestionhotel.model.GestionnaireClient;

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
    private GestionnaireClient gestionnaireClient;

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
        this.gestionnaireClient = new GestionnaireClient();
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
            gestionnaireClient.ajouterClient(client);
            this.clients.add(client);
        }
    }

    /**
     * Affiche tous les clients de l'hôtel.
     */
    public void afficherTousLesClients() {
        gestionnaireClient.afficherTousLesClients();
    }

    /**
     * Recherche un client par son numéro.
     * 
     * @param numero Le numéro du client
     * @return Le client trouvé ou null
     */
    public Client rechercherClient(int numero) {
        return gestionnaireClient.obtenirClient(numero);
    }

    /**
     * Recherche un client par son email.
     * 
     * @param email L'email du client
     * @return Le client trouvé ou null
     */
    public Client rechercherClientParEmail(String email) {
        return gestionnaireClient.rechercherParEmail(email);
    }

    // ===========================
    // MÉTHODES DE GESTION DES RÉSERVATIONS (Phase 3 - Dev 3)
    // ===========================

    /**
     * Crée une nouvelle réservation avec validation complète (Phase 3 - Dev 3).
     * Vérifie que la chambre est disponible et les dates sont valides.
     * 
     * @param client    Le client
     * @param chambre   La chambre
     * @param dateDebut Date de début (format jj/mm/aaaa)
     * @param dateFin   Date de fin (format jj/mm/aaaa)
     * @return La réservation créée ou null en cas d'erreur
     */
    public Reservation creerReservation(Client client, Chambre chambre, String dateDebut, String dateFin) {
        // Validation 1 : Chambre occupée
        if (chambre.isOccupee()) {
            System.out.println("Erreur: La chambre n°" + chambre.getNumero() + " est déjà occupée.");
            return null;
        }
        
        // Validation 2 : Client valide
        if (client == null) {
            System.out.println("Erreur: Le client est invalide.");
            return null;
        }
        
        // Validation 3 : Dates valides (format et logique)
        try {
            java.time.LocalDate debut = com.gestionhotel.utils.DateUtils.parserDateFR(dateDebut);
            java.time.LocalDate fin = com.gestionhotel.utils.DateUtils.parserDateFR(dateFin);
            
            if (!debut.isBefore(fin)) {
                System.out.println("Erreur: La date de départ doit être après la date d'arrivée.");
                return null;
            }
            
            if (!com.gestionhotel.utils.DateUtils.estPeriodeValide(debut, fin)) {
                System.out.println("Erreur: La période de réservation est invalide.");
                return null;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
            return null;
        }
        
        // Créer la réservation
        Reservation reservation = new Reservation(client, chambre, dateDebut, dateFin);
        this.reservations.add(reservation);
        
        System.out.println("✓ Réservation n°" + reservation.getNumeroReservation() + " créée avec succès.");
        return reservation;
    }

    /**
     * Affiche toutes les réservations avec filtrage optionnel (Phase 3 - Dev 3).
     */
    public void afficherToutesLesReservations() {
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation enregistrée.");
            return;
        }
        System.out.println("=== Liste complète des réservations (" + reservations.size() + ") ===");
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
            System.out.println("---");
        }
    }

    /**
     * Affiche les réservations d'un client spécifique (Phase 3 - Dev 3).
     * 
     * @param client Le client
     */
    public void afficherReservationsClient(Client client) {
        boolean found = false;
        System.out.println("=== Réservations de " + client.getNomComplet() + " (n°" + client.getNumeroClient() + ") ===");
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
     * Affiche les réservations actives (EN_COURS ou CONFIRMEES) (Phase 3 - Dev 3).
     */
    public void afficherReservationsActives() {
        ArrayList<Reservation> actives = new ArrayList<>();
        for (Reservation r : reservations) {
            if (!r.getStatut().equals("Annulée") && !r.getStatut().equals("Terminée")) {
                actives.add(r);
            }
        }
        
        if (actives.isEmpty()) {
            System.out.println("Aucune réservation active.");
            return;
        }
        System.out.println("=== Réservations actives (" + actives.size() + ") ===");
        for (Reservation r : actives) {
            System.out.println("Réservation n°" + r.getNumeroReservation() + " - " + 
                             r.getClient().getNomComplet() + " - Chambre " + r.getChambre().getNumero() + 
                             " (" + r.getChambre().getType() + ") - Statut: " + r.getStatut());
        }
    }

    /**
     * Affiche les réservations annulées (Phase 3 - Dev 3).
     */
    public void afficherReservationsAnnulees() {
        ArrayList<Reservation> annulees = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getStatut().equals("Annulée")) {
                annulees.add(r);
            }
        }
        
        if (annulees.isEmpty()) {
            System.out.println("Aucune réservation annulée.");
            return;
        }
        System.out.println("=== Réservations annulées (" + annulees.size() + ") ===");
        for (Reservation r : annulees) {
            System.out.println("Réservation n°" + r.getNumeroReservation() + " - " + 
                             r.getClient().getNomComplet() + " - Annulée le " + r.getDateAnnulation() + 
                             " - Raison: " + r.getRaison());
        }
    }

    /**
     * Recherche une réservation par son numéro (Phase 3 - Dev 3).
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
     * Annule une réservation par son numéro avec gestion robuste (Phase 3 - Dev 3).
     * 
     * @param numero Le numéro de réservation
     * @param raison Motif de l'annulation (optionnel)
     */
    public void annulerReservation(int numero, String raison) {
        Reservation reservation = rechercherReservation(numero);
        if (reservation != null) {
            boolean succes = reservation.annuler(raison);
            if (succes) {
                System.out.println("✓ Réservation n°" + numero + " annulée avec succès.");
            } else {
                System.out.println("✗ Impossible d'annuler la réservation n°" + numero);
            }
        } else {
            System.out.println("Erreur: Réservation n°" + numero + " non trouvée.");
        }
    }

    /**
     * Annule une réservation sans motif spécifique (Phase 3 - Dev 3).
     * 
     * @param numero Le numéro de réservation
     */
    public void annulerReservation(int numero) {
        annulerReservation(numero, null);
    }

    /**
     * Termine une réservation (check-out) avec validation (Phase 3 - Dev 3).
     * 
     * @param numero Le numéro de réservation
     */
    public void terminerReservation(int numero) {
        Reservation reservation = rechercherReservation(numero);
        if (reservation != null) {
            boolean succes = reservation.terminer();
            if (succes) {
                System.out.println("✓ Réservation n°" + numero + " terminée. Chambre libérée.");
            } else {
                System.out.println("✗ Impossible de terminer la réservation n°" + numero);
            }
        } else {
            System.out.println("Erreur: Réservation n°" + numero + " non trouvée.");
        }
    }

    /**
     * Confirme une réservation (Phase 3 - Dev 3).
     * 
     * @param numero Le numéro de réservation
     */
    public void confirmerReservation(int numero) {
        Reservation reservation = rechercherReservation(numero);
        if (reservation != null) {
            boolean succes = reservation.confirmer();
            if (succes) {
                System.out.println("✓ Réservation n°" + numero + " confirmée.");
            } else {
                System.out.println("✗ Impossible de confirmer la réservation n°" + numero);
            }
        } else {
            System.out.println("Erreur: Réservation n°" + numero + " non trouvée.");
        }
    }

    /**
     * Retourne le nombre de réservations en cours (Phase 3 - Dev 3).
     * 
     * @return Nombre de réservations actives
     */
    public int getNombreReservationsActives() {
        int count = 0;
        for (Reservation r : reservations) {
            if (!r.getStatut().equals("Annulée") && !r.getStatut().equals("Terminée")) {
                count++;
            }
        }
        return count;
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
