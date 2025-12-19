package main.java.com.gestionhotel.model;

public class ChambreDouble extends Chambre {

    private static final int CAPACITE_DOUBLE = 2;
    private static final double PRIX_BASE_DOUBLE = 80.0;

    private boolean litsJumeaux;


    public ChambreDouble(int numero, boolean litsJumeaux) {
        super(numero, PRIX_BASE_DOUBLE, CAPACITE_DOUBLE);
        this.litsJumeaux = litsJumeaux;
    }

    @Override
    public String getType() {
        return "Double";
    }

    public boolean isLitsJumeaux() {
        return litsJumeaux;
    }
    public void setLitsJumeaux(boolean litsJumeaux) {
        this.litsJumeaux = litsJumeaux;
    }

    @Override
    public String toString() {
        String typeLit = litsJumeaux ? "2 lits simples" : "1 lit double";
        return String.format("ChambreDouble #%d - %.2f€/nuit - Capacité: %d personnes - %s - %s",
                numero, prixParNuit, capacite, typeLit,
                occupee ? "Occupée" : "Libre");
    }
}
