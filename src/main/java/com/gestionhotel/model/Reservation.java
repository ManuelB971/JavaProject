package main.java.com.gestionhotel.model;

import java.util.ArrayList;
import main.java.com.gestionhotel.utils.DateUtils;
import java.time.LocalDate;

/**
 * Classe représentant une réservation d'hôtel.
 * Gère les informations de réservation incluant client, chambre, dates et services.
 * Implémente une gestion robuste des statuts et de l'annulation.
 * 
 * Statuts possibles :
 * - EN_COURS : réservation en cours de traitement
 * - CONFIRMEE : réservation confirmée par le client
 * - ANNULEE : réservation annulée
 * - TERMINEES : séjour terminé (check-out effectué)
 * 
 * @author Dev 1 & Dev 3
 */
public class Reservation {

    // Énumération des statuts possibles
    public enum Statut {
        EN_COURS("En cours"),
        CONFIRMEE("Confirmée"),
        ANNULEE("Annulée"),
        TERMINEES("Terminée");
        
        private final String label;
        
        Statut(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }

    // Attribut statique pour l'auto-incrémentation du numéro de réservation
    private static int compteurReservation = 1;

    // Attributs d'instance
    private int numeroReservation;
    private Client client;
    private Chambre chambre;
    private String dateDebut;      // Format "jj/mm/aaaa"
    private String dateFin;        // Format "jj/mm/aaaa"
    private ArrayList<Service> services;
    private Statut statut;         // Énumération des statuts
    private String dateAnnulation; // Date d'annulation si applicable
    private String raison;         // Raison de l'annulation

    /**
     * Constructeur complet pour créer une nouvelle réservation.
     * 
     * @param client    Le client effectuant la réservation
     * @param chambre   La chambre réservée
     * @param dateDebut Date de début de séjour (format jj/mm/aaaa)
     * @param dateFin   Date de fin de séjour (format jj/mm/aaaa)
     */
    public Reservation(Client client, Chambre chambre, String dateDebut, String dateFin) {
        this.numeroReservation = compteurReservation++;
        this.client = client;
        this.chambre = chambre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.services = new ArrayList<>();
        this.statut = Statut.EN_COURS;
        this.dateAnnulation = null;
        this.raison = null;
        
        // Marquer la chambre comme occupée
        this.chambre.setOccupee(true);
    }

    // ===========================
    // GETTERS & SETTERS
    // ===========================

    public int getNumeroReservation() {
        return numeroReservation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public Statut getStatut() {
        return statut;
    }

    public String getStatutLabel() {
        return statut.getLabel();
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public String getDateAnnulation() {
        return dateAnnulation;
    }

    public void setDateAnnulation(String dateAnnulation) {
        this.dateAnnulation = dateAnnulation;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    // ===========================
    // MÉTHODES MÉTIER
    // ===========================

    /**
     * Calcule le nombre de nuits de la réservation.
     * 
     * @return Le nombre de nuits entre dateDebut et dateFin
     */
    public int calculerNombreNuits() {
        try {
            LocalDate debut = DateUtils.parserDateFR(this.dateDebut);
            LocalDate fin = DateUtils.parserDateFR(this.dateFin);
            return (int) DateUtils.calculerNuits(debut, fin);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Calcule le prix total de la chambre pour le séjour.
     * 
     * @return Le prix de la chambre multiplié par le nombre de nuits
     */
    public double calculerPrixChambre() {
        int nbNuits = calculerNombreNuits();
        return chambre.calculerPrix(nbNuits);
    }

    /**
     * Calcule le prix total des services commandés.
     * 
     * @return La somme des prix de tous les services
     */
    public double calculerPrixServices() {
        double total = 0;
        for (Service service : services) {
            total += service.getPrix();
        }
        return total;
    }

    /**
     * Calcule le prix total de la réservation (chambre + services).
     * 
     * @return Le prix total de la réservation
     */
    public double calculerPrixTotal() {
        return calculerPrixChambre() + calculerPrixServices();
    }

    /**
     * Ajoute un service à la réservation.
     * 
     * @param service Le service à ajouter
     */
    public void ajouterService(Service service) {
        if (service != null && service.isDisponible()) {
            this.services.add(service);
        }
    }

    /**
     * Annule la réservation avec validation des conditions.
     * Permet l'annulation seulement si la réservation est EN_COURS ou CONFIRMEE.
     * 
     * @param raison Motif de l'annulation (optionnel)
     * @return true si l'annulation a réussi, false sinon
     */
    public boolean annuler(String raison) {
        // Validation : on ne peut annuler que si la réservation est EN_COURS ou CONFIRMEE
        if (this.statut == Statut.ANNULEE) {
            System.out.println("Erreur : Cette réservation est déjà annulée.");
            return false;
        }
        
        if (this.statut == Statut.TERMINEES) {
            System.out.println("Erreur : Impossible d'annuler une réservation déjà terminée.");
            return false;
        }
        
        // Enregistrer les informations d'annulation
        this.statut = Statut.ANNULEE;
        this.dateAnnulation = DateUtils.formaterDateFR(LocalDate.now());
        this.raison = raison != null ? raison : "Non spécifiée";
        
        // Libérer la chambre
        this.chambre.setOccupee(false);
        
        return true;
    }

    /**
     * Annule la réservation sans motif spécifique.
     * 
     * @return true si l'annulation a réussi, false sinon
     */
    public boolean annuler() {
        return annuler(null);
    }

    /**
     * Vérifie si la réservation peut être annulée.
     * 
     * @return true si l'annulation est possible, false sinon
     */
    public boolean peutEtreAnnulee() {
        return (this.statut == Statut.EN_COURS || this.statut == Statut.CONFIRMEE);
    }

    /**
     * Confirme la réservation.
     * Passage du statut EN_COURS à CONFIRMEE.
     * 
     * @return true si la confirmation a réussi, false sinon
     */
    public boolean confirmer() {
        if (this.statut != Statut.EN_COURS) {
            System.out.println("Erreur : Seule une réservation EN_COURS peut être confirmée.");
            return false;
        }
        this.statut = Statut.CONFIRMEE;
        return true;
    }

    /**
     * Termine la réservation (check-out) et libère la chambre.
     * Passage du statut CONFIRMEE à TERMINEES.
     * 
     * @return true si la fin a réussi, false sinon
     */
    public boolean terminer() {
        if (this.statut == Statut.ANNULEE) {
            System.out.println("Erreur : Impossible de terminer une réservation annulée.");
            return false;
        }
        
        if (this.statut == Statut.TERMINEES) {
            System.out.println("Erreur : Cette réservation est déjà terminée.");
            return false;
        }
        
        this.statut = Statut.TERMINEES;
        this.chambre.setOccupee(false);
        return true;
    }

    /**
     * Vérifie si la réservation est annulée.
     * 
     * @return true si le statut est ANNULEE
     */
    public boolean estAnnulee() {
        return this.statut == Statut.ANNULEE;
    }

    /**
     * Vérifie si la réservation est confirmée.
     * 
     * @return true si le statut est CONFIRMEE
     */
    public boolean estConfirmee() {
        return this.statut == Statut.CONFIRMEE;
    }

    /**
     * Vérifie si la réservation est terminée.
     * 
     * @return true si le statut est TERMINEES
     */
    public boolean estTerminee() {
        return this.statut == Statut.TERMINEES;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Réservation n°").append(numeroReservation).append(" ===\n");
        sb.append("Client: ").append(client.getNomComplet()).append("\n");
        sb.append("Chambre: ").append(chambre.getType()).append(" n°").append(chambre.getNumero()).append("\n");
        sb.append("Période: du ").append(dateDebut).append(" au ").append(dateFin);
        sb.append(" (").append(calculerNombreNuits()).append(" nuits)\n");
        sb.append("Statut: ").append(statut.getLabel()).append("\n");
        
        // Afficher les infos d'annulation si applicable
        if (statut == Statut.ANNULEE) {
            sb.append("Date d'annulation: ").append(dateAnnulation != null ? dateAnnulation : "N/A").append("\n");
            sb.append("Raison: ").append(raison != null ? raison : "Non spécifiée").append("\n");
        }
        
        if (!services.isEmpty()) {
            sb.append("Services: ");
            for (int i = 0; i < services.size(); i++) {
                sb.append(services.get(i).getNom());
                if (i < services.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        
        sb.append("Prix chambre: ").append(String.format("%.2f", calculerPrixChambre())).append("€\n");
        sb.append("Prix services: ").append(String.format("%.2f", calculerPrixServices())).append("€\n");
        sb.append("TOTAL: ").append(String.format("%.2f", calculerPrixTotal())).append("€");
        
        return sb.toString();
    }
}