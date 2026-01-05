package com.gestionhotel.utils;

import java.sql.*;
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
 * Gestionnaire de base de données SQLite pour la persistance des données de l'hôtel.
 * Remplace ou complémente FilePersistence avec une base de données relationnelle.
 * 
 * @author Dev 4 (Phase 4)
 */
public class SQLiteDatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:data/hotel.db";
    private static SQLiteDatabaseManager instance;

    private SQLiteDatabaseManager() {
        initialiserBaseDeDonnees();
    }

    /**
     * Obtient l'instance singleton du gestionnaire de base de données.
     * 
     * @return L'instance unique
     */
    public static synchronized SQLiteDatabaseManager getInstance() {
        if (instance == null) {
            instance = new SQLiteDatabaseManager();
        }
        return instance;
    }

    /**
     * Initialise la base de données et crée les tables si elles n'existent pas.
     */
    private void initialiserBaseDeDonnees() {
        try {
            // Charger le driver SQLite JDBC
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                System.err.println("⚠️  Driver SQLite JDBC non trouvé dans le classpath.");
                System.err.println("   Téléchargez-le depuis : https://github.com/xerial/sqlite-jdbc/releases");
                System.err.println("   Ou exécutez : ./INSTALL_SQLITE.sh");
                // On continue quand même, l'erreur sera levée lors de la connexion
            }
            
            // Créer le répertoire data s'il n'existe pas
            java.io.File dataDir = new java.io.File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            try (Connection conn = getConnection()) {
                // Créer la table des chambres
                String createChambres = "CREATE TABLE IF NOT EXISTS chambres (" +
                    "numero INTEGER PRIMARY KEY, " +
                    "type TEXT NOT NULL, " +
                    "prix_par_nuit REAL NOT NULL, " +
                    "capacite INTEGER NOT NULL, " +
                    "occupee INTEGER NOT NULL DEFAULT 0, " +
                    "lits_jumeaux INTEGER DEFAULT 0, " +
                    "jacuzzi INTEGER DEFAULT 0, " +
                    "balcon INTEGER DEFAULT 0" +
                    ")";
                conn.createStatement().execute(createChambres);

                // Créer la table des clients
                String createClients = "CREATE TABLE IF NOT EXISTS clients (" +
                    "numero_client INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nom TEXT NOT NULL, " +
                    "prenom TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "telephone TEXT" +
                    ")";
                conn.createStatement().execute(createClients);

                // Créer la table des services
                String createServices = "CREATE TABLE IF NOT EXISTS services (" +
                    "id_service INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nom TEXT NOT NULL, " +
                    "description TEXT, " +
                    "prix REAL NOT NULL, " +
                    "disponible INTEGER NOT NULL DEFAULT 1" +
                    ")";
                conn.createStatement().execute(createServices);

                // Créer la table des réservations
                String createReservations = "CREATE TABLE IF NOT EXISTS reservations (" +
                    "numero_reservation INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "numero_client INTEGER NOT NULL, " +
                    "numero_chambre INTEGER NOT NULL, " +
                    "date_debut TEXT NOT NULL, " +
                    "date_fin TEXT NOT NULL, " +
                    "statut TEXT NOT NULL DEFAULT 'En cours', " +
                    "date_annulation TEXT, " +
                    "raison TEXT, " +
                    "FOREIGN KEY(numero_client) REFERENCES clients(numero_client), " +
                    "FOREIGN KEY(numero_chambre) REFERENCES chambres(numero)" +
                    ")";
                conn.createStatement().execute(createReservations);

                // Créer la table de liaison réservations-services
                String createReservationsServices = "CREATE TABLE IF NOT EXISTS reservations_services (" +
                    "numero_reservation INTEGER NOT NULL, " +
                    "id_service INTEGER NOT NULL, " +
                    "PRIMARY KEY(numero_reservation, id_service), " +
                    "FOREIGN KEY(numero_reservation) REFERENCES reservations(numero_reservation), " +
                    "FOREIGN KEY(id_service) REFERENCES services(id_service)" +
                    ")";
                conn.createStatement().execute(createReservationsServices);

                // Créer la table des informations de l'hôtel
                String createHotel = "CREATE TABLE IF NOT EXISTS hotel_info (" +
                    "id INTEGER PRIMARY KEY DEFAULT 1, " +
                    "nom TEXT NOT NULL, " +
                    "adresse TEXT NOT NULL" +
                    ")";
                conn.createStatement().execute(createHotel);

            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtient une connexion à la base de données.
     * 
     * @return La connexion
     * @throws SQLException En cas d'erreur de connexion
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Sauvegarde toutes les données de l'hôtel dans la base de données.
     * 
     * @param hotel L'hôtel à sauvegarder
     * @return true si la sauvegarde a réussi, false sinon
     */
    public boolean sauvegarderHotel(Hotel hotel) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Sauvegarder les informations de l'hôtel
                sauvegarderHotelInfo(conn, hotel);
                
                // Vider les tables avant de réinsérer
                conn.createStatement().execute("DELETE FROM reservations_services");
                conn.createStatement().execute("DELETE FROM reservations");
                conn.createStatement().execute("DELETE FROM chambres");
                conn.createStatement().execute("DELETE FROM clients");
                conn.createStatement().execute("DELETE FROM services");
                
                // Sauvegarder les données
                sauvegarderChambres(conn, hotel.getChambres());
                sauvegarderClients(conn, hotel.getClients());
                sauvegarderServices(conn, hotel.getServicesDisponibles());
                sauvegarderReservations(conn, hotel.getReservations());
                
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Charge toutes les données de l'hôtel depuis la base de données.
     * 
     * @param hotel L'hôtel à remplir avec les données chargées
     * @return true si le chargement a réussi, false sinon
     */
    public boolean chargerHotel(Hotel hotel) {
        try (Connection conn = getConnection()) {
            chargerHotelInfo(conn, hotel);
            chargerChambres(conn, hotel);
            chargerClients(conn, hotel);
            chargerServices(conn, hotel);
            chargerReservations(conn, hotel);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ===========================
    // MÉTHODES DE SAUVEGARDE
    // ===========================

    private void sauvegarderHotelInfo(Connection conn, Hotel hotel) throws SQLException {
        String sql = "INSERT OR REPLACE INTO hotel_info (id, nom, adresse) VALUES (1, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hotel.getNom());
            stmt.setString(2, hotel.getAdresse());
            stmt.executeUpdate();
        }
    }

    private void sauvegarderChambres(Connection conn, ArrayList<Chambre> chambres) throws SQLException {
        String sql = "INSERT INTO chambres (numero, type, prix_par_nuit, capacite, occupee, lits_jumeaux, jacuzzi, balcon) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Chambre chambre : chambres) {
                stmt.setInt(1, chambre.getNumero());
                stmt.setString(2, chambre.getType());
                stmt.setDouble(3, chambre.getPrixParNuit());
                stmt.setInt(4, chambre.getCapacite());
                stmt.setInt(5, chambre.isOccupee() ? 1 : 0);
                
                if (chambre instanceof ChambreDouble) {
                    stmt.setInt(6, ((ChambreDouble) chambre).isLitsJumeaux() ? 1 : 0);
                    stmt.setInt(7, 0);
                    stmt.setInt(8, 0);
                } else if (chambre instanceof Suite) {
                    stmt.setInt(6, 0);
                    stmt.setInt(7, ((Suite) chambre).hasJacuzzi() ? 1 : 0);
                    stmt.setInt(8, ((Suite) chambre).hasBalcon() ? 1 : 0);
                } else {
                    stmt.setInt(6, 0);
                    stmt.setInt(7, 0);
                    stmt.setInt(8, 0);
                }
                stmt.executeUpdate();
            }
        }
    }

    private void sauvegarderClients(Connection conn, ArrayList<Client> clients) throws SQLException {
        String sql = "INSERT INTO clients (numero_client, nom, prenom, email, telephone) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Client client : clients) {
                stmt.setInt(1, client.getNumeroClient());
                stmt.setString(2, client.getNom());
                stmt.setString(3, client.getPrenom());
                stmt.setString(4, client.getEmail());
                stmt.setString(5, client.getTelephone());
                stmt.executeUpdate();
            }
        }
    }

    private void sauvegarderServices(Connection conn, ArrayList<Service> services) throws SQLException {
        String sql = "INSERT INTO services (id_service, nom, description, prix, disponible) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Service service : services) {
                stmt.setInt(1, service.getIdService());
                stmt.setString(2, service.getNom());
                stmt.setString(3, service.getDescription());
                stmt.setDouble(4, service.getPrix());
                stmt.setInt(5, service.isDisponible() ? 1 : 0);
                stmt.executeUpdate();
            }
        }
    }

    private void sauvegarderReservations(Connection conn, ArrayList<Reservation> reservations) throws SQLException {
        String sql = "INSERT INTO reservations (numero_reservation, numero_client, numero_chambre, " +
                    "date_debut, date_fin, statut, date_annulation, raison) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlServices = "INSERT INTO reservations_services (numero_reservation, id_service) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement stmtServices = conn.prepareStatement(sqlServices)) {
            
            for (Reservation reservation : reservations) {
                stmt.setInt(1, reservation.getNumeroReservation());
                stmt.setInt(2, reservation.getClient().getNumeroClient());
                stmt.setInt(3, reservation.getChambre().getNumero());
                stmt.setString(4, reservation.getDateDebut());
                stmt.setString(5, reservation.getDateFin());
                stmt.setString(6, reservation.getStatut());
                stmt.setString(7, reservation.getDateAnnulation());
                stmt.setString(8, reservation.getRaison());
                stmt.executeUpdate();
                
                // Sauvegarder les services associés
                for (Service service : reservation.getServices()) {
                    stmtServices.setInt(1, reservation.getNumeroReservation());
                    stmtServices.setInt(2, service.getIdService());
                    stmtServices.executeUpdate();
                }
            }
        }
    }

    // ===========================
    // MÉTHODES DE CHARGEMENT
    // ===========================

    private void chargerHotelInfo(Connection conn, Hotel hotel) throws SQLException {
        String sql = "SELECT nom, adresse FROM hotel_info WHERE id = 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                hotel.setNom(rs.getString("nom"));
                hotel.setAdresse(rs.getString("adresse"));
            }
        }
    }

    private void chargerChambres(Connection conn, Hotel hotel) throws SQLException {
        String sql = "SELECT * FROM chambres";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int numero = rs.getInt("numero");
                String type = rs.getString("type");
                double prix = rs.getDouble("prix_par_nuit");
                boolean occupee = rs.getInt("occupee") == 1;
                
                Chambre chambre = null;
                if (type.equals("Simple")) {
                    chambre = new ChambreSimple(numero);
                } else if (type.equals("Double")) {
                    boolean litsJumeaux = rs.getInt("lits_jumeaux") == 1;
                    chambre = new ChambreDouble(numero, litsJumeaux);
                } else if (type.equals("Suite")) {
                    boolean jacuzzi = rs.getInt("jacuzzi") == 1;
                    boolean balcon = rs.getInt("balcon") == 1;
                    chambre = new Suite(numero, jacuzzi, balcon);
                }
                
                if (chambre != null) {
                    chambre.setPrixParNuit(prix);
                    chambre.setOccupee(occupee);
                    hotel.ajouterChambre(chambre);
                }
            }
        }
    }

    private void chargerClients(Connection conn, Hotel hotel) throws SQLException {
        String sql = "SELECT * FROM clients ORDER BY numero_client";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int maxNumero = 0;
            while (rs.next()) {
                int numeroClient = rs.getInt("numero_client");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String telephone = rs.getString("telephone");
                
                // Mettre à jour le compteur statique pour préserver les numéros
                if (numeroClient > maxNumero) {
                    maxNumero = numeroClient;
                }
                
                Client client = new Client(nom, prenom, email, telephone);
                // Note: Le numéro sera auto-incrémenté, mais on essaie de préserver l'ordre
                hotel.ajouterClient(client);
            }
            // Mettre à jour le compteur statique de Client pour éviter les conflits
            // Cette partie nécessiterait une méthode statique dans Client, on la laisse pour l'instant
        }
    }

    private void chargerServices(Connection conn, Hotel hotel) throws SQLException {
        String sql = "SELECT * FROM services";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String nom = rs.getString("nom");
                String description = rs.getString("description");
                double prix = rs.getDouble("prix");
                boolean disponible = rs.getInt("disponible") == 1;
                
                Service service = new Service(nom, description, prix);
                service.setDisponible(disponible);
                hotel.ajouterService(service);
            }
        }
    }

    private void chargerReservations(Connection conn, Hotel hotel) throws SQLException {
        String sql = "SELECT * FROM reservations";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int numeroReservation = rs.getInt("numero_reservation");
                int numeroClient = rs.getInt("numero_client");
                int numeroChambre = rs.getInt("numero_chambre");
                String dateDebut = rs.getString("date_debut");
                String dateFin = rs.getString("date_fin");
                String statut = rs.getString("statut");
                
                Client client = hotel.rechercherClient(numeroClient);
                Chambre chambre = hotel.rechercherChambre(numeroChambre);
                
                if (client != null && chambre != null) {
                    // Créer la réservation directement sans vérifier l'occupation
                    // (car lors du chargement, les chambres sont déjà marquées occupées)
                    Reservation reservation = new Reservation(client, chambre, dateDebut, dateFin);
                    
                    // Restaurer le numéro de réservation depuis la base
                    reservation.setNumeroReservation(numeroReservation);
                    
                    // Restaurer le statut et les informations d'annulation
                    reservation.setStatut(statut);
                    reservation.setDateAnnulation(rs.getString("date_annulation"));
                    reservation.setRaison(rs.getString("raison"));
                    
                    // Ajouter la réservation à la liste (sans passer par creerReservation)
                    hotel.getReservations().add(reservation);
                    
                    // Charger les services associés
                    chargerServicesReservation(conn, reservation, hotel);
                }
            }
        }
    }

    /**
     * Charge les services associés à une réservation depuis la base de données.
     * 
     * @param conn La connexion à la base de données
     * @param reservation La réservation
     * @param hotel L'hôtel contenant les services
     * @throws SQLException En cas d'erreur SQL
     */
    private void chargerServicesReservation(Connection conn, Reservation reservation, Hotel hotel) throws SQLException {
        String sql = "SELECT id_service FROM reservations_services WHERE numero_reservation = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getNumeroReservation());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idService = rs.getInt("id_service");
                    // Trouver le service dans l'hôtel
                    for (Service service : hotel.getServicesDisponibles()) {
                        if (service.getIdService() == idService) {
                            reservation.ajouterService(service);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Vérifie si la base de données existe.
     * 
     * @return true si la base existe, false sinon
     */
    public boolean baseExiste() {
        java.io.File dbFile = new java.io.File("data/hotel.db");
        return dbFile.exists();
    }

    /**
     * Vérifie si la base de données est vide (aucune donnée dans les tables principales).
     * 
     * @return true si la base est vide, false sinon
     */
    public boolean baseEstVide() {
        try (Connection conn = getConnection()) {
            // Vérifier si au moins une table a des données
            String sql = "SELECT (SELECT COUNT(*) FROM chambres) + " +
                        "(SELECT COUNT(*) FROM clients) + " +
                        "(SELECT COUNT(*) FROM services) + " +
                        "(SELECT COUNT(*) FROM reservations) as total";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    return total == 0;
                }
            }
            return true;
        } catch (SQLException e) {
            // Si erreur, considérer comme vide
            return true;
        }
    }
}

