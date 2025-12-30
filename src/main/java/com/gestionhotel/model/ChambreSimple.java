package com.gestionhotel.model;

public class ChambreSimple extends Chambre {

    // Constantes
    private static final int CAPACITE_SIMPLE = 1;
    private static final double PRIX_BASE_SIMPLE = 50.0;

    public ChambreSimple(int numero) {
        super(numero, PRIX_BASE_SIMPLE, CAPACITE_SIMPLE);
    }
    @Override
    public String getType() {
        return "Simple";
    }
    @Override
    public String toString() {
        return String.format("ChambreSimple #%d - %.2f€/nuit - Capacité: %d personne - %s",
                numero, prixParNuit, capacite, 
                occupee ? "Occupée" : "Libre");
    }
}
