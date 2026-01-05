package com.gestionhotel.utils;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import com.gestionhotel.core.Hotel;
import com.gestionhotel.model.Chambre;
import com.gestionhotel.model.ChambreSimple;
import com.gestionhotel.model.ChambreDouble;
import com.gestionhotel.model.Suite;
import com.gestionhotel.model.Client;
import com.gestionhotel.model.Reservation;
import com.gestionhotel.model.Service;
import java.io.File;

/**
 * Tests unitaires pour SQLiteDatabaseManager.
 * Teste la persistance des données dans une base SQLite.
 * 
 * @author Dev 4 (Phase 4)
 */
public class TestSQLiteDatabaseManager {

    private SQLiteDatabaseManager dbManager;
    private Hotel hotel;
    private static final String TEST_DB = "data/test_hotel.db";

    @Before
    public void setUp() {
        // Utiliser une base de test séparée
        dbManager = SQLiteDatabaseManager.getInstance();
        hotel = new Hotel("Hôtel Test", "123 Rue Test, Paris");
        
        // Supprimer la base de test si elle existe
        File testDb = new File(TEST_DB);
        if (testDb.exists()) {
            testDb.delete();
        }
    }

    @After
    public void tearDown() {
        // Nettoyer la base de test après les tests
        File testDb = new File(TEST_DB);
        if (testDb.exists()) {
            testDb.delete();
        }
    }

    @Test
    public void testInitialisationBaseDeDonnees() {
        // Vérifier que la base est initialisée
        assertNotNull("Le gestionnaire de base de données ne doit pas être null", dbManager);
    }

    @Test
    public void testSauvegarderEtChargerHotelInfo() {
        // Créer un hôtel avec des données
        Hotel hotelTest = new Hotel("Hôtel Le Magnifique", "123 Avenue des Champs-Élysées, Paris");
        
        // Sauvegarder
        boolean sauvegarde = dbManager.sauvegarderHotel(hotelTest);
        assertTrue("La sauvegarde doit réussir", sauvegarde);
        
        // Créer un nouvel hôtel et charger
        Hotel hotelCharge = new Hotel("", "");
        boolean chargement = dbManager.chargerHotel(hotelCharge);
        assertTrue("Le chargement doit réussir", chargement);
        
        // Vérifier que les données sont correctes
        assertEquals("Le nom de l'hôtel doit être correct", "Hôtel Le Magnifique", hotelCharge.getNom());
        assertEquals("L'adresse de l'hôtel doit être correcte", 
            "123 Avenue des Champs-Élysées, Paris", hotelCharge.getAdresse());
    }

    @Test
    public void testSauvegarderEtChargerChambres() {
        // Ajouter des chambres
        hotel.ajouterChambre(new ChambreSimple(1));
        hotel.ajouterChambre(new ChambreDouble(2, true));
        hotel.ajouterChambre(new Suite(3, true, false));
        
        // Sauvegarder
        boolean sauvegarde = dbManager.sauvegarderHotel(hotel);
        assertTrue("La sauvegarde doit réussir", sauvegarde);
        
        // Créer un nouvel hôtel et charger
        Hotel hotelCharge = new Hotel("Test", "Test");
        boolean chargement = dbManager.chargerHotel(hotelCharge);
        assertTrue("Le chargement doit réussir", chargement);
        
        // Vérifier que les chambres sont chargées
        assertEquals("Le nombre de chambres doit être correct", 3, hotelCharge.getChambres().size());
        
        // Vérifier les détails des chambres
        Chambre chambre1 = hotelCharge.rechercherChambre(1);
        assertNotNull("La chambre 1 doit exister", chambre1);
        assertEquals("La chambre 1 doit être de type Simple", "Simple", chambre1.getType());
        
        Chambre chambre2 = hotelCharge.rechercherChambre(2);
        assertNotNull("La chambre 2 doit exister", chambre2);
        assertEquals("La chambre 2 doit être de type Double", "Double", chambre2.getType());
        assertTrue("La chambre 2 doit avoir des lits jumeaux", 
            ((ChambreDouble) chambre2).isLitsJumeaux());
        
        Chambre chambre3 = hotelCharge.rechercherChambre(3);
        assertNotNull("La chambre 3 doit exister", chambre3);
        assertEquals("La chambre 3 doit être une Suite", "Suite", chambre3.getType());
        assertTrue("La chambre 3 doit avoir un jacuzzi", ((Suite) chambre3).hasJacuzzi());
        assertFalse("La chambre 3 ne doit pas avoir de balcon", ((Suite) chambre3).hasBalcon());
    }

    @Test
    public void testSauvegarderEtChargerClients() {
        // Ajouter des clients
        Client client1 = new Client("Dupont", "Jean", "jean.dupont@email.com", "0123456789");
        Client client2 = new Client("Martin", "Marie", "marie.martin@email.com", "0987654321");
        
        hotel.ajouterClient(client1);
        hotel.ajouterClient(client2);
        
        // Sauvegarder
        boolean sauvegarde = dbManager.sauvegarderHotel(hotel);
        assertTrue("La sauvegarde doit réussir", sauvegarde);
        
        // Créer un nouvel hôtel et charger
        Hotel hotelCharge = new Hotel("Test", "Test");
        boolean chargement = dbManager.chargerHotel(hotelCharge);
        assertTrue("Le chargement doit réussir", chargement);
        
        // Vérifier que les clients sont chargés
        assertEquals("Le nombre de clients doit être correct", 2, hotelCharge.getClients().size());
        
        // Vérifier les détails des clients
        Client clientCharge1 = hotelCharge.rechercherClientParEmail("jean.dupont@email.com");
        assertNotNull("Le client 1 doit exister", clientCharge1);
        assertEquals("Le nom du client 1 doit être correct", "Dupont", clientCharge1.getNom());
        assertEquals("Le prénom du client 1 doit être correct", "Jean", clientCharge1.getPrenom());
    }

    @Test
    public void testSauvegarderEtChargerServices() {
        // Ajouter des services
        Service service1 = new Service("Petit-déjeuner", "Petit-déjeuner buffet", 15.0);
        Service service2 = new Service("Spa", "Séance de spa", 50.0);
        service2.setDisponible(false);
        
        hotel.ajouterService(service1);
        hotel.ajouterService(service2);
        
        // Sauvegarder
        boolean sauvegarde = dbManager.sauvegarderHotel(hotel);
        assertTrue("La sauvegarde doit réussir", sauvegarde);
        
        // Créer un nouvel hôtel et charger
        Hotel hotelCharge = new Hotel("Test", "Test");
        boolean chargement = dbManager.chargerHotel(hotelCharge);
        assertTrue("Le chargement doit réussir", chargement);
        
        // Vérifier que les services sont chargés
        assertEquals("Le nombre de services doit être correct", 2, hotelCharge.getServicesDisponibles().size());
        
        // Vérifier les détails des services
        Service serviceCharge1 = hotelCharge.getServicesDisponibles().get(0);
        assertEquals("Le nom du service 1 doit être correct", "Petit-déjeuner", serviceCharge1.getNom());
        assertEquals("Le prix du service 1 doit être correct", 15.0, serviceCharge1.getPrix(), 0.01);
        assertTrue("Le service 1 doit être disponible", serviceCharge1.isDisponible());
    }

    @Test
    public void testSauvegarderEtChargerReservations() {
        // Préparer les données
        Client client = new Client("Dupont", "Jean", "jean.dupont@email.com", "0123456789");
        Chambre chambre = new ChambreSimple(1);
        
        hotel.ajouterClient(client);
        hotel.ajouterChambre(chambre);
        
        // Créer une réservation
        Reservation reservation = hotel.creerReservation(client, chambre, "01/01/2024", "05/01/2024");
        assertNotNull("La réservation doit être créée", reservation);
        
        // Ajouter un service à la réservation
        Service service = new Service("Petit-déjeuner", "Petit-déjeuner buffet", 15.0);
        hotel.ajouterService(service);
        reservation.ajouterService(service);
        
        // Sauvegarder
        boolean sauvegarde = dbManager.sauvegarderHotel(hotel);
        assertTrue("La sauvegarde doit réussir", sauvegarde);
        
        // Créer un nouvel hôtel et charger
        Hotel hotelCharge = new Hotel("Test", "Test");
        boolean chargement = dbManager.chargerHotel(hotelCharge);
        assertTrue("Le chargement doit réussir", chargement);
        
        // Vérifier que les réservations sont chargées
        assertEquals("Le nombre de réservations doit être correct", 1, hotelCharge.getReservations().size());
        
        // Vérifier les détails de la réservation
        Reservation reservationCharge = hotelCharge.getReservations().get(0);
        assertNotNull("La réservation doit exister", reservationCharge);
        assertEquals("La date de début doit être correcte", "01/01/2024", reservationCharge.getDateDebut());
        assertEquals("La date de fin doit être correcte", "05/01/2024", reservationCharge.getDateFin());
    }

    @Test
    public void testBaseExiste() {
        // La base doit exister après initialisation
        boolean existe = dbManager.baseExiste();
        assertTrue("La base de données doit exister", existe);
    }

    @Test
    public void testSauvegardeComplete() {
        // Créer un hôtel complet avec toutes les données
        hotel.ajouterChambre(new ChambreSimple(1));
        hotel.ajouterChambre(new ChambreDouble(2, false));
        
        Client client1 = new Client("Dupont", "Jean", "jean@email.com", "0123456789");
        Client client2 = new Client("Martin", "Marie", "marie@email.com", "0987654321");
        hotel.ajouterClient(client1);
        hotel.ajouterClient(client2);
        
        Service service = new Service("Spa", "Séance de spa", 50.0);
        hotel.ajouterService(service);
        
        Reservation reservation = hotel.creerReservation(client1, hotel.rechercherChambre(1), 
            "01/01/2024", "03/01/2024");
        reservation.ajouterService(service);
        
        // Sauvegarder
        boolean sauvegarde = dbManager.sauvegarderHotel(hotel);
        assertTrue("La sauvegarde complète doit réussir", sauvegarde);
        
        // Charger et vérifier
        Hotel hotelCharge = new Hotel("", "");
        boolean chargement = dbManager.chargerHotel(hotelCharge);
        assertTrue("Le chargement complet doit réussir", chargement);
        
        assertEquals("Le nombre de chambres doit être correct", 2, hotelCharge.getChambres().size());
        assertEquals("Le nombre de clients doit être correct", 2, hotelCharge.getClients().size());
        assertEquals("Le nombre de services doit être correct", 1, hotelCharge.getServicesDisponibles().size());
        assertEquals("Le nombre de réservations doit être correct", 1, hotelCharge.getReservations().size());
    }
}

