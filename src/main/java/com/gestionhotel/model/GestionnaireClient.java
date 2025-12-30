package com.gestionhotel.model;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour gérer les clients de l'hôtel.
 * Gère l'ajout, la suppression, la modification et la recherche de clients.
 * 
 * @author Dev 2 (Phase 3)
 */
public class GestionnaireClient {

    // HashMap pour stocker les clients : clé = numéro client, valeur = Client
    private HashMap<Integer, Client> clients;

    /**
     * Constructeur du gestionnaire de clients.
     * Initialise la HashMap vide.
     */
    public GestionnaireClient() {
        this.clients = new HashMap<>();
    }

    /**
     * Ajoute un nouveau client au gestionnaire.
     * 
     * @param client Le client à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean ajouterClient(Client client) {
        if (client != null && !clients.containsKey(client.getNumeroClient())) {
            clients.put(client.getNumeroClient(), client);
            return true;
        }
        return false;
    }

    /**
     * Supprime un client du gestionnaire.
     * 
     * @param numeroClient Le numéro du client à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimerClient(int numeroClient) {
        return clients.remove(numeroClient) != null;
    }

    /**
     * Récupère un client par son numéro.
     * 
     * @param numeroClient Le numéro du client à récupérer
     * @return Le client trouvé, ou null s'il n'existe pas
     */
    public Client obtenirClient(int numeroClient) {
        return clients.get(numeroClient);
    }

    /**
     * Modifie les informations d'un client existant.
     * 
     * @param numeroClient Le numéro du client à modifier
     * @param nom Le nouveau nom
     * @param prenom Le nouveau prénom
     * @param email Le nouvel email
     * @param telephone Le nouveau téléphone
     * @return true si la modification a réussi, false sinon
     */
    public boolean modifierClient(int numeroClient, String nom, String prenom, String email, String telephone) {
        Client client = clients.get(numeroClient);
        if (client != null) {
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setEmail(email);
            client.setTelephone(telephone);
            return true;
        }
        return false;
    }

    /**
     * Recherche un client par son email.
     * 
     * @param email L'email à rechercher
     * @return Le client trouvé, ou null s'il n'existe pas
     */
    public Client rechercherParEmail(String email) {
        for (Client client : clients.values()) {
            if (client.getEmail().equalsIgnoreCase(email)) {
                return client;
            }
        }
        return null;
    }

    /**
     * Recherche un client par son téléphone.
     * 
     * @param telephone Le téléphone à rechercher
     * @return Le client trouvé, ou null s'il n'existe pas
     */
    public Client rechercherParTelephone(String telephone) {
        for (Client client : clients.values()) {
            if (client.getTelephone().equals(telephone)) {
                return client;
            }
        }
        return null;
    }

    /**
     * Retourne la liste de tous les clients.
     * 
     * @return Une ArrayList contenant tous les clients
     */
    public List<Client> obtenirTousLesClients() {
        return new ArrayList<>(clients.values());
    }

    /**
     * Affiche tous les clients de manière formatée.
     */
    public void afficherTousLesClients() {
        if (clients.isEmpty()) {
            System.out.println("Aucun client enregistré.");
            return;
        }
        System.out.println("\n=== LISTE DES CLIENTS ===");
        for (Client client : clients.values()) {
            System.out.println(client);
        }
        System.out.println();
    }

    /**
     * Vérifie si un email existe déjà dans la base.
     * 
     * @param email L'email à vérifier
     * @return true si l'email existe, false sinon
     */
    public boolean emailExiste(String email) {
        return rechercherParEmail(email) != null;
    }

    /**
     * Vérifie si un téléphone existe déjà dans la base.
     * 
     * @param telephone Le téléphone à vérifier
     * @return true si le téléphone existe, false sinon
     */
    public boolean telephoneExiste(String telephone) {
        return rechercherParTelephone(telephone) != null;
    }

    /**
     * Vérifie si un client existe par son numéro.
     * 
     * @param numeroClient Le numéro à vérifier
     * @return true si le client existe, false sinon
     */
    public boolean clientExiste(int numeroClient) {
        return clients.containsKey(numeroClient);
    }

    /**
     * Retourne le nombre total de clients.
     * 
     * @return Le nombre de clients
     */
    public int obtenirNombreClients() {
        return clients.size();
    }

    /**
     * Vide complètement la liste des clients.
     */
    public void effacerTousLesClients() {
        clients.clear();
    }
}
