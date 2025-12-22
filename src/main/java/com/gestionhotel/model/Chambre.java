package main.java.com.gestionhotel.model;

/**
 * Classe abstraite représentant une chambre d'hôtel générique.
 * Cette classe sert de base pour les différents types de chambres (Simple,
 * Double, Suite).
 * 
 * @author Dev 1
 */
public abstract class Chambre {

    // Attributs protégés pour être accessibles par les classes filles
    protected int numero; // Numéro de la chambre
    protected double prixParNuit; // Tarif par nuit
    protected boolean occupee; // Statut d'occupation (true = occupée, false = libre)
    protected int capacite; // Nombre maximum de personnes
    protected Client client; // Client actuellement occupant la chambre

    /**
     * Constructeur complet pour initialiser une chambre.
     * 
     * @param numero      Le numéro unique de la chambre
     * @param prixParNuit Le prix de base par nuit
     * @param capacite    La capacité maximale d'accueil
     */
    public Chambre(int numero, double prixParNuit, int capacite) {
        this.numero = numero;
        this.prixParNuit = prixParNuit;
        this.capacite = capacite;
        this.occupee = false; // Par défaut, une chambre est libre à la création
        this.client = null; // Aucun client au départ
    }

    // ===========================
    // GETTERS & SETTERS
    // ===========================

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getPrixParNuit() {
        return prixParNuit;
    }

    public void setPrixParNuit(double prixParNuit) {
        this.prixParNuit = prixParNuit;
    }

    public boolean isOccupee() {
        return occupee;
    }

    public void setOccupee(boolean occupee) {
        this.occupee = occupee;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // ===========================
    // MÉTHODES MÉTIER
    // ===========================

    /**
     * Méthode abstraite pour obtenir le type de la chambre.
     * Doit être implémentée par les classes filles (ChambreSimple, ChambreDouble,
     * Suite).
     * 
     * @return Le nom du type de chambre (ex: "Simple", "Double")
     */
    public abstract String getType();

    /**
     * Calcule le prix total pour un séjour donné.
     * 
     * @param nbNuits Le nombre de nuits réservées
     * @return Le prix total (prixParNuit * nbNuits)
     */
    public double calculerPrix(int nbNuits) {
        return this.prixParNuit * nbNuits;
    }

    /**
     * Retourne une description textuelle de la chambre.
     * 
     * @return Chaîne contenant les informations de la chambre
     */
    @Override
    public String toString() {
        // Utilisation de ternaire pour afficher "Oui" ou "Non" pour le statut occupé
        String statut = occupee ? "Occupée" : "Libre";
        return String.format("Chambre n°%d [%s] - Capacité: %d pers - Prix: %.2f€/nuit - Statut: %s",
                numero, getType(), capacite, prixParNuit, statut);
    }
}
