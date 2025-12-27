package com.gestionhotel.model;

/**
 * Classe représentant un service hôtelier disponible pour les clients.
 * Cette classe permet de gérer différents types de services avec leurs tarifs.
 * 
 * @author Dev 4
 */
public class Service {
    
    // Attributs
    private int idService;
    private String nom;
    private String description;
    private double prix;
    private boolean disponible;
    
    // Attribut statique pour l'auto-incrémentation
    private static int compteurService = 1;

    /**
     * Constructeur complet pour initialiser un service.
     * 
     * @param nom         Nom du service
     * @param description Description détaillée du service
     * @param prix        Prix du service
     */
    public Service(String nom, String description, double prix) {
        this.idService = compteurService++;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.disponible = true; // Par défaut, un service est disponible
    }

    // GETTERS & SETTERS

    public int getIdService() {
        return idService;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // MÉTHODES MÉTIER

    /**
     * Active le service (le rend disponible).
     */
    public void activer() {
        this.disponible = true;
    }

    /**
     * Désactive le service (le rend indisponible).
     */
    public void desactiver() {
        this.disponible = false;
    }

    /**
     * Applique une promotion au service.
     * 
     * @param pourcentageReduction Pourcentage de réduction (ex: 10 pour 10%)
     */
    public void appliquerPromotion(int pourcentageReduction) {
        if (pourcentageReduction > 0 && pourcentageReduction < 100) {
            double reduction = this.prix * (pourcentageReduction / 100.0);
            this.prix -= reduction;
        }
    }

    @Override
    public String toString() {
        String statut = disponible ? "Disponible" : "Indisponible";
        return String.format("Service n°%d - %s - %.2f€ - %s (%s)",
                idService, nom, prix, description, statut);
    }
}
