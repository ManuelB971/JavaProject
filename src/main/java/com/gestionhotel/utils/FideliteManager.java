package com.gestionhotel.utils;

import com.gestionhotel.core.Hotel;
import com.gestionhotel.model.Client;
import com.gestionhotel.model.Reservation;

/**
 * Classe utilitaire pour gérer le programme de fidélité des clients.
 * Calcule les statuts de fidélité, les réductions applicables et applique
 * les réductions aux prix des réservations.
 * 
 * @author Dev 4 (Phase 4)
 */
public class FideliteManager {

    // Constantes pour les seuils de fidélité
    private static final int SEUIL_PLATINE = 5;
    private static final int SEUIL_OR = 3;
    private static final int SEUIL_ARGENT = 1;

    // Pourcentages de réduction par niveau
    private static final double REDUCTION_PLATINE = 15.0;
    private static final double REDUCTION_OR = 10.0;
    private static final double REDUCTION_ARGENT = 5.0;
    private static final double REDUCTION_BRONZE = 0.0;

    /**
     * Calcule le statut de fidélité d'un client basé sur son nombre de réservations.
     * 
     * <p><b>Système de fidélité :</b>
     * <ul>
     *   <li><b>Bronze</b> : 0 réservation (0% de réduction)</li>
     *   <li><b>Argent</b> : ≥ 1 réservation (5% de réduction)</li>
     *   <li><b>Or</b> : ≥ 3 réservations (10% de réduction)</li>
     *   <li><b>Platine</b> : ≥ 5 réservations (15% de réduction)</li>
     * </ul>
     * 
     * <p><b>Note :</b> Seules les réservations non annulées sont comptabilisées.
     * 
     * @param client Le client
     * @param hotel  L'hôtel contenant les réservations
     * @return Le statut de fidélité : "Bronze", "Argent", "Or" ou "Platine"
     */
    public static String calculerStatutFidelite(Client client, Hotel hotel) {
        if (client == null || hotel == null) {
            return "Bronze";
        }

        int nbReservations = obtenirNombreReservations(client, hotel);
        
        if (nbReservations >= SEUIL_PLATINE) {
            return "Platine";
        } else if (nbReservations >= SEUIL_OR) {
            return "Or";
        } else if (nbReservations >= SEUIL_ARGENT) {
            return "Argent";
        } else {
            return "Bronze";
        }
    }

    /**
     * Calcule le pourcentage de réduction applicable à un client.
     * 
     * @param client Le client
     * @param hotel  L'hôtel contenant les réservations
     * @return Le pourcentage de réduction (0.0 à 15.0)
     */
    public static double calculerReduction(Client client, Hotel hotel) {
        if (client == null || hotel == null) {
            return REDUCTION_BRONZE;
        }

        int nbReservations = obtenirNombreReservations(client, hotel);
        
        if (nbReservations >= SEUIL_PLATINE) {
            return REDUCTION_PLATINE;
        } else if (nbReservations >= SEUIL_OR) {
            return REDUCTION_OR;
        } else if (nbReservations >= SEUIL_ARGENT) {
            return REDUCTION_ARGENT;
        } else {
            return REDUCTION_BRONZE;
        }
    }

    /**
     * Applique la réduction de fidélité au prix total d'une réservation.
     * 
     * @param prixTotal Le prix total avant réduction
     * @param client    Le client effectuant la réservation
     * @param hotel     L'hôtel
     * @return Le prix total après application de la réduction de fidélité
     */
    public static double appliquerReductionFidelite(double prixTotal, Client client, Hotel hotel) {
        if (client == null || hotel == null || prixTotal < 0) {
            return prixTotal;
        }

        double reduction = calculerReduction(client, hotel);
        if (reduction > 0) {
            return prixTotal * (1 - reduction / 100.0);
        }
        return prixTotal;
    }

    /**
     * Obtient le nombre de réservations non annulées d'un client.
     * 
     * @param client Le client
     * @param hotel  L'hôtel contenant les réservations
     * @return Le nombre de réservations non annulées
     */
    public static int obtenirNombreReservations(Client client, Hotel hotel) {
        if (client == null || hotel == null) {
            return 0;
        }

        int count = 0;
        for (Reservation reservation : hotel.getReservations()) {
            if (reservation.getClient().getNumeroClient() == client.getNumeroClient() 
                && !reservation.estAnnulee()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Obtient la dépense totale d'un client (somme des prix de toutes ses réservations non annulées).
     * 
     * @param client Le client
     * @param hotel  L'hôtel contenant les réservations
     * @return La dépense totale en euros
     */
    public static double obtenirDepenseTotale(Client client, Hotel hotel) {
        if (client == null || hotel == null) {
            return 0.0;
        }

        double total = 0.0;
        for (Reservation reservation : hotel.getReservations()) {
            if (reservation.getClient().getNumeroClient() == client.getNumeroClient() 
                && !reservation.estAnnulee()) {
                total += reservation.calculerPrixTotal();
            }
        }
        return total;
    }

    /**
     * Calcule les économies réalisées par un client grâce à son statut de fidélité.
     * 
     * @param client Le client
     * @param hotel  L'hôtel
     * @return Le montant total des économies réalisées en euros
     */
    public static double calculerEconomiesTotales(Client client, Hotel hotel) {
        if (client == null || hotel == null) {
            return 0.0;
        }

        double depenseTotale = obtenirDepenseTotale(client, hotel);
        double reduction = calculerReduction(client, hotel);
        
        if (reduction > 0) {
            return depenseTotale * (reduction / 100.0);
        }
        return 0.0;
    }

    /**
     * Obtient le nombre de réservations nécessaires pour atteindre le niveau supérieur.
     * 
     * @param client Le client
     * @param hotel  L'hôtel
     * @return Le nombre de réservations nécessaires, ou 0 si déjà au niveau maximum
     */
    public static int obtenirReservationsPourNiveauSuperieur(Client client, Hotel hotel) {
        if (client == null || hotel == null) {
            return 0;
        }

        int nbReservations = obtenirNombreReservations(client, hotel);
        String statut = calculerStatutFidelite(client, hotel);

        if (statut.equals("Platine")) {
            return 0; // Déjà au niveau maximum
        } else if (statut.equals("Or")) {
            return SEUIL_PLATINE - nbReservations;
        } else if (statut.equals("Argent")) {
            return SEUIL_OR - nbReservations;
        } else {
            return SEUIL_ARGENT - nbReservations;
        }
    }
}

