package main.java.com.gestionhotel.model;

import java.util.ArrayList;
import main.java.com.gestionhotel.utils.DateUtils;
import java.time.LocalDate;

/**
 * Classe représentant une réservation d'hôtel.
 * Gère les informations de réservation incluant client, chambre, dates et services.
 * 
 * @author Dev 1
 */
public class Reservation {

    // Attribut statique pour l'auto-incrémentation du numéro de réservation
    private static int compteurReservation = 1;

    // Attributs d'instance
    private int numeroReservation;
    private Client client;
    private Chambre chambre;
    private String dateDebut;      // Format "jj/mm/aaaa"
    private String dateFin;        // Format "jj/mm/aaaa"
    private ArrayList<Service> services;
    private String statut;         // "En cours", "Confirmée", "Annulée", "Terminée"

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
        this.statut = "En cours";
        
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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
     * Annule la réservation et libère la chambre.
     */
    public void annuler() {
        this.statut = "Annulée";
        this.chambre.setOccupee(false);
    }

    /**
     * Termine la réservation (check-out) et libère la chambre.
     */
    public void terminer() {
        this.statut = "Terminée";
        this.chambre.setOccupee(false);
    }

    /**
     * Confirme la réservation.
     */
    public void confirmer() {
        this.statut = "Confirmée";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Réservation n°").append(numeroReservation).append(" ===\n");
        sb.append("Client: ").append(client.getNomComplet()).append("\n");
        sb.append("Chambre: ").append(chambre.getType()).append(" n°").append(chambre.getNumero()).append("\n");
        sb.append("Période: du ").append(dateDebut).append(" au ").append(dateFin);
        sb.append(" (").append(calculerNombreNuits()).append(" nuits)\n");
        sb.append("Statut: ").append(statut).append("\n");
        
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
