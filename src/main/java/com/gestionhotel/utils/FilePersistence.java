package com.gestionhotel.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.gestionhotel.core.Hotel;
import com.gestionhotel.model.Chambre;
import com.gestionhotel.model.ChambreSimple;
import com.gestionhotel.model.ChambreDouble;
import com.gestionhotel.model.Suite;
import com.gestionhotel.model.Client;
import com.gestionhotel.model.Reservation;
import com.gestionhotel.model.Service;

/**
 * Classe utilitaire pour la persistance des données de l'hôtel.
 * Permet de sauvegarder et charger les chambres, clients, réservations et services
 * depuis/vers des fichiers texte.
 * 
 * @author Dev 4 (Phase 3)
 */
public class FilePersistence {

    // Noms des fichiers de sauvegarde
    private static final String FICHIER_CHAMBRES = "data/chambres.txt";
    private static final String FICHIER_CLIENTS = "data/clients.txt";
    private static final String FICHIER_RESERVATIONS = "data/reservations.txt";
    private static final String FICHIER_SERVICES = "data/services.txt";
    private static final String FICHIER_HOTEL = "data/hotel.txt";

    /**
     * Sauvegarde toutes les données de l'hôtel dans des fichiers.
     * 
     * @param hotel L'hôtel à sauvegarder
     * @return true si la sauvegarde a réussi, false sinon
     */
    public static boolean sauvegarderHotel(Hotel hotel) {
        try {
            // Créer le répertoire data s'il n'existe pas
            java.io.File dataDir = new java.io.File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            sauvegarderHotelInfo(hotel);
            sauvegarderChambres(hotel.getChambres());
            sauvegarderClients(hotel.getClients());
            sauvegarderReservations(hotel.getReservations());
            sauvegarderServices(hotel.getServicesDisponibles());

            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            return false;
        }
    }

    /**
     * Charge toutes les données de l'hôtel depuis les fichiers.
     * 
     * @param hotel L'hôtel à remplir avec les données chargées
     * @return true si le chargement a réussi, false sinon
     */
    public static boolean chargerHotel(Hotel hotel) {
        try {
            chargerHotelInfo(hotel);
            chargerChambres(hotel);
            chargerClients(hotel);
            chargerServices(hotel);
            chargerReservations(hotel);

            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
            return false;
        }
    }

    // ===========================
    // MÉTHODES DE SAUVEGARDE
    // ===========================

    /**
     * Sauvegarde les informations de l'hôtel (nom, adresse).
     */
    private static void sauvegarderHotelInfo(Hotel hotel) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_HOTEL))) {
            writer.write(hotel.getNom());
            writer.newLine();
            writer.write(hotel.getAdresse());
            writer.newLine();
        }
    }

    /**
     * Sauvegarde les chambres dans un fichier.
     */
    private static void sauvegarderChambres(ArrayList<Chambre> chambres) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_CHAMBRES))) {
            for (Chambre chambre : chambres) {
                String type = chambre.getType();
                writer.write(type + "|" + chambre.getNumero() + "|" + 
                    chambre.getPrixParNuit() + "|" + chambre.getCapacite() + "|" + 
                    chambre.isOccupee());
                
                // Ajouter des informations spécifiques selon le type
                if (chambre instanceof ChambreDouble) {
                    ChambreDouble chambreDouble = (ChambreDouble) chambre;
                    writer.write("|" + chambreDouble.isLitsJumeaux());
                } else if (chambre instanceof Suite) {
                    Suite suite = (Suite) chambre;
                    writer.write("|" + suite.hasBalcon() + "|" + suite.hasJacuzzi());
                }
                
                writer.newLine();
            }
        }
    }

    /**
     * Sauvegarde les clients dans un fichier.
     */
    private static void sauvegarderClients(ArrayList<Client> clients) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_CLIENTS))) {
            for (Client client : clients) {
                writer.write(client.getNumeroClient() + "|" + 
                    client.getNom() + "|" + 
                    client.getPrenom() + "|" + 
                    client.getEmail() + "|" + 
                    client.getTelephone());
                writer.newLine();
            }
        }
    }

    /**
     * Sauvegarde les réservations dans un fichier.
     */
    private static void sauvegarderReservations(ArrayList<Reservation> reservations) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_RESERVATIONS))) {
            for (Reservation reservation : reservations) {
                writer.write(reservation.getNumeroReservation() + "|" + 
                    reservation.getClient().getNumeroClient() + "|" + 
                    reservation.getChambre().getNumero() + "|" + 
                    reservation.getDateDebut() + "|" + 
                    reservation.getDateFin() + "|" + 
                    reservation.getStatut());
                
                // Ajouter la date d'annulation et la raison si applicable
                if (reservation.getDateAnnulation() != null) {
                    writer.write("|" + reservation.getDateAnnulation() + "|" + 
                        (reservation.getRaison() != null ? reservation.getRaison() : ""));
                }
                
                // Ajouter les IDs des services
                if (!reservation.getServices().isEmpty()) {
                    writer.write("|SERVICES:");
                    for (int i = 0; i < reservation.getServices().size(); i++) {
                        writer.write(reservation.getServices().get(i).getIdService() + "");
                        if (i < reservation.getServices().size() - 1) {
                            writer.write(",");
                        }
                    }
                }
                
                writer.newLine();
            }
        }
    }

    /**
     * Sauvegarde les services dans un fichier.
     */
    private static void sauvegarderServices(ArrayList<Service> services) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_SERVICES))) {
            for (Service service : services) {
                writer.write(service.getIdService() + "|" + 
                    service.getNom() + "|" + 
                    service.getDescription() + "|" + 
                    service.getPrix() + "|" + 
                    service.isDisponible());
                writer.newLine();
            }
        }
    }

    // ===========================
    // MÉTHODES DE CHARGEMENT
    // ===========================

    /**
     * Charge les informations de l'hôtel (nom, adresse).
     */
    private static void chargerHotelInfo(Hotel hotel) throws IOException {
        java.io.File fichier = new java.io.File(FICHIER_HOTEL);
        if (!fichier.exists()) {
            return; // Pas de fichier, on garde les valeurs par défaut
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FICHIER_HOTEL))) {
            String nom = reader.readLine();
            String adresse = reader.readLine();
            
            if (nom != null) {
                hotel.setNom(nom);
            }
            if (adresse != null) {
                hotel.setAdresse(adresse);
            }
        }
    }

    /**
     * Charge les chambres depuis un fichier.
     */
    private static void chargerChambres(Hotel hotel) throws IOException {
        java.io.File fichier = new java.io.File(FICHIER_CHAMBRES);
        if (!fichier.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FICHIER_CHAMBRES))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split("\\|");
                if (parts.length < 5) continue;

                String type = parts[0];
                int numero = Integer.parseInt(parts[1]);
                double prixParNuit = Double.parseDouble(parts[2]);
                boolean occupee = Boolean.parseBoolean(parts[4]);

                Chambre chambre = null;
                
                if (type.equals("Simple")) {
                    chambre = new ChambreSimple(numero);
                } else if (type.equals("Double")) {
                    boolean litsJumeaux = parts.length > 5 ? Boolean.parseBoolean(parts[5]) : false;
                    chambre = new ChambreDouble(numero, litsJumeaux);
                } else if (type.equals("Suite")) {
                    boolean balcon = parts.length > 5 ? Boolean.parseBoolean(parts[5]) : false;
                    boolean jacuzzi = parts.length > 6 ? Boolean.parseBoolean(parts[6]) : false;
                    chambre = new Suite(numero, balcon, jacuzzi);
                }

                if (chambre != null) {
                    chambre.setPrixParNuit(prixParNuit);
                    chambre.setOccupee(occupee);
                    hotel.ajouterChambre(chambre);
                }
            }
        }
    }

    /**
     * Charge les clients depuis un fichier.
     */
    private static void chargerClients(Hotel hotel) throws IOException {
        java.io.File fichier = new java.io.File(FICHIER_CLIENTS);
        if (!fichier.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FICHIER_CLIENTS))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split("\\|");
                if (parts.length < 5) continue;

                String nom = parts[1];
                String prenom = parts[2];
                String email = parts[3];
                String telephone = parts[4];

                Client client = new Client(nom, prenom, email, telephone);
                hotel.ajouterClient(client);
            }
        }
    }

    /**
     * Charge les services depuis un fichier.
     */
    private static void chargerServices(Hotel hotel) throws IOException {
        java.io.File fichier = new java.io.File(FICHIER_SERVICES);
        if (!fichier.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FICHIER_SERVICES))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split("\\|");
                if (parts.length < 5) continue;

                String nom = parts[1];
                String description = parts[2];
                double prix = Double.parseDouble(parts[3]);
                boolean disponible = Boolean.parseBoolean(parts[4]);

                Service service = new Service(nom, description, prix);
                service.setDisponible(disponible);
                hotel.ajouterService(service);
            }
        }
    }

    /**
     * Charge les réservations depuis un fichier.
     * Note: Cette méthode doit être appelée après le chargement des chambres, clients et services.
     */
    private static void chargerReservations(Hotel hotel) throws IOException {
        java.io.File fichier = new java.io.File(FICHIER_RESERVATIONS);
        if (!fichier.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FICHIER_RESERVATIONS))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split("\\|");
                if (parts.length < 6) continue;

                int numeroClient = Integer.parseInt(parts[1]);
                int numeroChambre = Integer.parseInt(parts[2]);
                String dateDebut = parts[3];
                String dateFin = parts[4];
                String statut = parts[5];

                Client client = hotel.rechercherClient(numeroClient);
                Chambre chambre = hotel.rechercherChambre(numeroChambre);

                if (client == null || chambre == null) {
                    continue; // Ignorer si client ou chambre non trouvés
                }

                Reservation reservation = hotel.creerReservation(client, chambre, dateDebut, dateFin);
                if (reservation != null) {
                    reservation.setStatut(statut);

                    // Charger date d'annulation et raison si présentes
                    if (parts.length > 6 && !parts[6].isEmpty() && !parts[6].startsWith("SERVICES")) {
                        reservation.setDateAnnulation(parts[6]);
                        if (parts.length > 7 && !parts[7].startsWith("SERVICES")) {
                            reservation.setRaison(parts[7]);
                        }
                    }

                    // Charger les services associés
                    for (int i = 6; i < parts.length; i++) {
                        if (parts[i].startsWith("SERVICES:")) {
                            String servicesStr = parts[i].substring(9); // Enlever "SERVICES:"
                            String[] serviceIds = servicesStr.split(",");
                            for (String serviceIdStr : serviceIds) {
                                try {
                                    int serviceId = Integer.parseInt(serviceIdStr);
                                    for (Service service : hotel.getServicesDisponibles()) {
                                        if (service.getIdService() == serviceId) {
                                            reservation.ajouterService(service);
                                            break;
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    // Ignorer les IDs invalides
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Vérifie si des fichiers de sauvegarde existent.
     * 
     * @return true si au moins un fichier existe, false sinon
     */
    public static boolean fichiersExistent() {
        return new java.io.File(FICHIER_CHAMBRES).exists() ||
               new java.io.File(FICHIER_CLIENTS).exists() ||
               new java.io.File(FICHIER_RESERVATIONS).exists() ||
               new java.io.File(FICHIER_SERVICES).exists() ||
               new java.io.File(FICHIER_HOTEL).exists();
    }
}
