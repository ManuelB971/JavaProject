package com.gestionhotel.model;

public class Suite extends Chambre {
    
    // Constantes
    private static final int CAPACITE_SUITE = 4;
    private static final double PRIX_BASE_SUITE = 150.0;
    private static final double SUPPLEMENT_JACUZZI = 30.0;
    private static final double SUPPLEMENT_BALCON = 20.0;
    
    // Attributs supplémentaires
    private boolean jacuzzi;    // Présence d'un jacuzzi
    private boolean balcon;     // Présence d'un balcon

    /**
     * Constructeur complet pour initialiser une Suite.
     * 
     * @param numero    
     * @param jacuzzi  
     * @param balcon    
     */
    public Suite(int numero, boolean jacuzzi, boolean balcon) {
        super(numero, PRIX_BASE_SUITE, CAPACITE_SUITE);
        this.jacuzzi = jacuzzi;
        this.balcon = balcon;
    }

    // GETTERS & SETTERS

    public boolean hasJacuzzi() {
        return jacuzzi;
    }

    public void setJacuzzi(boolean jacuzzi) {
        this.jacuzzi = jacuzzi;
    }

    public boolean hasBalcon() {
        return balcon;
    }

    public void setBalcon(boolean balcon) {
        this.balcon = balcon;
    }

    // MÉTHODES MÉTIER

    @Override
    public String getType() {
        return "Suite";
    }

    /**
     * Calcule le prix total pour un séjour donné en tenant compte des suppléments.
     * 
     * @param nbNuits Le nombre de nuits réservées
     * @return Le prix total avec les suppléments (jacuzzi + balcon)
     */
    @Override
    public double calculerPrix(int nbNuits) {
        double prixTotal = super.calculerPrix(nbNuits);
        
        if (jacuzzi) {
            prixTotal += SUPPLEMENT_JACUZZI * nbNuits;
        }
        if (balcon) {
            prixTotal += SUPPLEMENT_BALCON * nbNuits;
        }
        
        return prixTotal;
    }

    @Override
    public String toString() {
        String statut = occupee ? "Occupée" : "Libre";
        String options = "";
        
        if (jacuzzi && balcon) {
            options = " - Options: Jacuzzi + Balcon";
        } else if (jacuzzi) {
            options = " - Option: Jacuzzi";
        } else if (balcon) {
            options = " - Option: Balcon";
        }
        
        return String.format("Suite n°%d - Capacité: %d personnes - Prix de base: %.2f€/nuit - Statut: %s%s",
                numero, capacite, prixParNuit, statut, options);
    }
}
