package com.gestionhotel.model;

import java.util.regex.Pattern;
import java.util.HashMap;

public class Client {
    
    // Attribut statique pour l'auto-incrémentation
    private static int compteurClient = 1;
    
    // Attributs d'instance
    private int numeroClient;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private HashMap<Integer, Chambre> chambresOccupees; // Numéro chambre -> Chambre occupée

    /**
     * Constructeur complet pour créer un nouveau client.
     * 
     * @param nom
     * @param prenom
     * @param email
     * @param telephone
     */
    public Client(String nom, String prenom, String email, String telephone) {
        this.numeroClient = compteurClient++;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.chambresOccupees = new HashMap<>();
    }

    // GETTERS & SETTERS

    public int getNumeroClient() {
        return numeroClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public HashMap<Integer, Chambre> getChambresOccupees() {
        return chambresOccupees;
    }

    public void ajouterChambre(Chambre chambre) {
        if (chambre != null) {
            chambresOccupees.put(chambre.getNumero(), chambre);
        }
    }

    public void retirerChambre(int numeroChambre) {
        chambresOccupees.remove(numeroChambre);
    }

    public Chambre obtenirChambre(int numeroChambre) {
        return chambresOccupees.get(numeroChambre);
    }

    public boolean occupeChambre(int numeroChambre) {
        return chambresOccupees.containsKey(numeroChambre);
    }

    // MÉTHODES MÉTIER

    public String getNomComplet() {
        return prenom + " " + nom.toUpperCase();
    }

    public boolean validerEmail() {
        // Regex pour valider un email basique
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && !email.isEmpty() && pattern.matcher(email).matches();
    }

    @Override
    public String toString() {
        return String.format("Client n°%d - %s - Email: %s - Tél: %s",
                numeroClient, getNomComplet(), email, telephone);
    }
}
