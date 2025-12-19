package main.java.com.gestionhotel.model;

public abstract class Chambre {

    protected int numero; 
    protected double prixParNuit; 
    protected boolean occupee; 
    protected int capacite; 

    /**
     * Constructeur complet pour initialiser une chambre.
     * 
     * @param numero      
     * @param prixParNuit 
     * @param capacite    
     */
    public Chambre(int numero, double prixParNuit, int capacite) {
        this.numero = numero;
        this.prixParNuit = prixParNuit;
        this.capacite = capacite;
        this.occupee = false; 
    }


    // GETTERS & SETTERS

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

    
    // MÉTHODES MÉTIER

    public abstract String getType();

    /**
     * Calcule le prix total pour un séjour donné.
     * 
     * @param nbNuits 
     * @return 
     */
    public double calculerPrix(int nbNuits) {
        return this.prixParNuit * nbNuits;
    }

    @Override
    public String toString() {
        // Utilisation de ternaire pour afficher "Oui" ou "Non" pour le statut occupé
        String statut = occupee ? "Occupée" : "Libre";
        return String.format("Chambre n°%d [%s] - Capacité: %d pers - Prix: %.2f€/nuit - Statut: %s",
                numero, getType(), capacite, prixParNuit, statut);
    }
}
