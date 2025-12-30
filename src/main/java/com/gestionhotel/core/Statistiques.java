package com.gestionhotel.core;

import java.util.HashMap;
import java.util.Map;
import com.gestionhotel.model.Chambre;
import com.gestionhotel.model.Client;
import com.gestionhotel.model.Reservation;
import com.gestionhotel.model.Service;

/**
 * Classe utilitaire pour calculer et afficher les statistiques de l'hôtel.
 * Fournit des méthodes pour analyser les performances, l'occupation et les revenus.
 * 
 * @author Dev 4 (Phase 3)
 */
public class Statistiques {

    private Hotel hotel;

    /**
     * Constructeur de la classe Statistiques.
     * 
     * @param hotel L'hôtel pour lequel calculer les statistiques
     */
    public Statistiques(Hotel hotel) {
        this.hotel = hotel;
    }

    /**
     * Calcule le chiffre d'affaires total de l'hôtel.
     * Compte uniquement les réservations non annulées.
     * 
     * @return Le chiffre d'affaires total en euros
     */
    public double calculerChiffreAffaires() {
        double chiffreAffaires = 0.0;
        for (Reservation reservation : hotel.getReservations()) {
            if (!reservation.getStatut().equals("Annulée")) {
                chiffreAffaires += reservation.calculerPrixTotal();
            }
        }
        return chiffreAffaires;
    }

    /**
     * Calcule le chiffre d'affaires pour un statut spécifique.
     * 
     * @param statut Le statut des réservations à considérer ("Confirmée", "Terminée", etc.)
     * @return Le chiffre d'affaires pour ce statut
     */
    public double calculerChiffreAffairesParStatut(String statut) {
        double chiffreAffaires = 0.0;
        for (Reservation reservation : hotel.getReservations()) {
            if (reservation.getStatut().equals(statut)) {
                chiffreAffaires += reservation.calculerPrixTotal();
            }
        }
        return chiffreAffaires;
    }

    /**
     * Calcule le taux d'occupation actuel de l'hôtel.
     * 
     * @return Le taux d'occupation en pourcentage (0-100)
     */
    public double calculerTauxOccupation() {
        int totalChambres = hotel.getChambres().size();
        if (totalChambres == 0) {
            return 0.0;
        }
        
        int chambresOccupees = 0;
        for (Chambre chambre : hotel.getChambres()) {
            if (chambre.isOccupee()) {
                chambresOccupees++;
            }
        }
        
        return (double) chambresOccupees / totalChambres * 100.0;
    }

    /**
     * Trouve la chambre la plus réservée.
     * 
     * @return La chambre avec le plus de réservations, ou null si aucune réservation
     */
    public Chambre trouverChambreLaPlusReservee() {
        if (hotel.getReservations().isEmpty()) {
            return null;
        }

        // Compteur pour chaque chambre
        Map<Integer, Integer> compteurChambres = new HashMap<>();
        
        for (Reservation reservation : hotel.getReservations()) {
            int numeroChambre = reservation.getChambre().getNumero();
            compteurChambres.put(numeroChambre, 
                compteurChambres.getOrDefault(numeroChambre, 0) + 1);
        }

        // Trouver la chambre avec le maximum de réservations
        int maxReservations = 0;
        int numeroChambreMax = -1;
        
        for (Map.Entry<Integer, Integer> entry : compteurChambres.entrySet()) {
            if (entry.getValue() > maxReservations) {
                maxReservations = entry.getValue();
                numeroChambreMax = entry.getKey();
            }
        }

        if (numeroChambreMax == -1) {
            return null;
        }

        return hotel.rechercherChambre(numeroChambreMax);
    }

    /**
     * Calcule le nombre de réservations par statut.
     * 
     * @return Une map avec le statut comme clé et le nombre comme valeur
     */
    public Map<String, Integer> calculerReservationsParStatut() {
        Map<String, Integer> statistiques = new HashMap<>();
        
        for (Reservation reservation : hotel.getReservations()) {
            String statut = reservation.getStatut();
            statistiques.put(statut, statistiques.getOrDefault(statut, 0) + 1);
        }
        
        return statistiques;
    }

    /**
     * Calcule le nombre moyen de nuits par réservation.
     * 
     * @return Le nombre moyen de nuits, ou 0 si aucune réservation
     */
    public double calculerNombreMoyenNuits() {
        if (hotel.getReservations().isEmpty()) {
            return 0.0;
        }

        int totalNuits = 0;
        int nombreReservations = 0;
        
        for (Reservation reservation : hotel.getReservations()) {
            if (!reservation.getStatut().equals("Annulée")) {
                totalNuits += reservation.calculerNombreNuits();
                nombreReservations++;
            }
        }

        if (nombreReservations == 0) {
            return 0.0;
        }

        return (double) totalNuits / nombreReservations;
    }

    /**
     * Trouve le client le plus fidèle (avec le plus de réservations).
     * 
     * @return Le client avec le plus de réservations, ou null si aucune réservation
     */
    public Client trouverClientLePlusFidele() {
        if (hotel.getReservations().isEmpty()) {
            return null;
        }

        Map<Integer, Integer> compteurClients = new HashMap<>();
        
        for (Reservation reservation : hotel.getReservations()) {
            int numeroClient = reservation.getClient().getNumeroClient();
            compteurClients.put(numeroClient, 
                compteurClients.getOrDefault(numeroClient, 0) + 1);
        }

        int maxReservations = 0;
        int numeroClientMax = -1;
        
        for (Map.Entry<Integer, Integer> entry : compteurClients.entrySet()) {
            if (entry.getValue() > maxReservations) {
                maxReservations = entry.getValue();
                numeroClientMax = entry.getKey();
            }
        }

        if (numeroClientMax == -1) {
            return null;
        }

        return hotel.rechercherClient(numeroClientMax);
    }

    /**
     * Calcule le revenu moyen par réservation.
     * 
     * @return Le revenu moyen en euros, ou 0 si aucune réservation
     */
    public double calculerRevenuMoyenParReservation() {
        if (hotel.getReservations().isEmpty()) {
            return 0.0;
        }

        double totalRevenu = 0.0;
        int nombreReservations = 0;
        
        for (Reservation reservation : hotel.getReservations()) {
            if (!reservation.getStatut().equals("Annulée")) {
                totalRevenu += reservation.calculerPrixTotal();
                nombreReservations++;
            }
        }

        if (nombreReservations == 0) {
            return 0.0;
        }

        return totalRevenu / nombreReservations;
    }

    /**
     * Calcule le nombre de services utilisés au total.
     * 
     * @return Le nombre total de services commandés
     */
    public int calculerNombreTotalServices() {
        int total = 0;
        for (Reservation reservation : hotel.getReservations()) {
            total += reservation.getServices().size();
        }
        return total;
    }

    /**
     * Trouve le service le plus utilisé.
     * 
     * @return Le service le plus commandé, ou null si aucun service
     */
    public Service trouverServiceLePlusUtilise() {
        if (hotel.getReservations().isEmpty()) {
            return null;
        }

        Map<Integer, Integer> compteurServices = new HashMap<>();
        
        for (Reservation reservation : hotel.getReservations()) {
            for (Service service : reservation.getServices()) {
                int idService = service.getIdService();
                compteurServices.put(idService, 
                    compteurServices.getOrDefault(idService, 0) + 1);
            }
        }

        if (compteurServices.isEmpty()) {
            return null;
        }

        int maxUtilisations = 0;
        int idServiceMax = -1;
        
        for (Map.Entry<Integer, Integer> entry : compteurServices.entrySet()) {
            if (entry.getValue() > maxUtilisations) {
                maxUtilisations = entry.getValue();
                idServiceMax = entry.getKey();
            }
        }

        if (idServiceMax == -1) {
            return null;
        }

        // Trouver le service dans la liste des services disponibles
        for (Service service : hotel.getServicesDisponibles()) {
            if (service.getIdService() == idServiceMax) {
                return service;
            }
        }

        return null;
    }

    /**
     * Affiche un rapport complet des statistiques.
     */
    public void afficherRapportComplet() {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║         RAPPORT STATISTIQUES COMPLET               ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");

        // Informations générales
        System.out.println("=== INFORMATIONS GÉNÉRALES ===");
        System.out.println("Nombre de chambres : " + hotel.getChambres().size());
        System.out.println("Nombre de clients : " + hotel.getClients().size());
        System.out.println("Nombre de réservations : " + hotel.getReservations().size());
        System.out.println("Nombre de services disponibles : " + hotel.getServicesDisponibles().size());
        System.out.println();

        // Chiffre d'affaires
        System.out.println("=== CHIFFRE D'AFFAIRES ===");
        System.out.println("Chiffre d'affaires total : " + 
            String.format("%.2f", calculerChiffreAffaires()) + "€");
        System.out.println("Revenu moyen par réservation : " + 
            String.format("%.2f", calculerRevenuMoyenParReservation()) + "€");
        System.out.println();

        // Occupation
        System.out.println("=== OCCUPATION ===");
        System.out.println("Taux d'occupation : " + 
            String.format("%.1f", calculerTauxOccupation()) + "%");
        System.out.println();

        // Réservations
        System.out.println("=== RÉSERVATIONS ===");
        System.out.println("Nombre moyen de nuits : " + 
            String.format("%.1f", calculerNombreMoyenNuits()));
        
        Map<String, Integer> reservationsParStatut = calculerReservationsParStatut();
        System.out.println("Répartition par statut :");
        for (Map.Entry<String, Integer> entry : reservationsParStatut.entrySet()) {
            System.out.println("  - " + entry.getKey() + " : " + entry.getValue());
        }
        System.out.println();

        // Services
        System.out.println("=== SERVICES ===");
        System.out.println("Nombre total de services commandés : " + calculerNombreTotalServices());
        Service servicePlusUtilise = trouverServiceLePlusUtilise();
        if (servicePlusUtilise != null) {
            System.out.println("Service le plus utilisé : " + servicePlusUtilise.getNom());
        }
        System.out.println();

        // Chambre la plus réservée
        System.out.println("=== CHAMBRE LA PLUS RÉSERVÉE ===");
        Chambre chambrePlusReservee = trouverChambreLaPlusReservee();
        if (chambrePlusReservee != null) {
            System.out.println(chambrePlusReservee);
        } else {
            System.out.println("Aucune réservation enregistrée.");
        }
        System.out.println();

        // Client le plus fidèle
        System.out.println("=== CLIENT LE PLUS FIDÈLE ===");
        Client clientPlusFidele = trouverClientLePlusFidele();
        if (clientPlusFidele != null) {
            System.out.println(clientPlusFidele);
        } else {
            System.out.println("Aucune réservation enregistrée.");
        }
        System.out.println();

        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║              FIN DU RAPPORT                        ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");
    }
}
