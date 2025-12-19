package main.java.com.gestionhotel.model;

public class Suite extends Chambre {
    /**
     * Constructeur complet pour initialiser une chambre.
     *
     * @param numero      Le numéro unique de la chambre
     * @param prixParNuit Le prix de base par nuit
     * @param capacite    La capacité maximale d'accueil
     */
    public Suite(int numero, double prixParNuit, int capacite) {
        super(numero, prixParNuit, capacite);
    }

    /**
     * @return
     */
    @Override
    public String getType() {
        return "";
    }
    // Implementation de Suite
}
