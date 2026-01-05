package com.gestionhotel.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.gestionhotel.model.*;

/**
 * Tests unitaires pour la classe Hotel.
 * Vérifie la gestion des chambres, clients, réservations et services.
 * 
 * @author Tests Phase 4
 */
public class TestHotel {

    private Hotel hotel;

    @Before
    public void setUp() {
        hotel = new Hotel("Hôtel Test", "123 Rue Test, Paris");
    }

    @Test
    public void testCreationHotel() {
        assertEquals("Hôtel Test", hotel.getNom());
        assertEquals("123 Rue Test, Paris", hotel.getAdresse());
        assertNotNull(hotel.getChambres());
        assertNotNull(hotel.getClients());
        assertNotNull(hotel.getReservations());
        assertNotNull(hotel.getServicesDisponibles());
        assertTrue(hotel.getChambres().isEmpty());
    }

    @Test
    public void testAjouterChambre() {
        ChambreSimple chambre = new ChambreSimple(1);
        hotel.ajouterChambre(chambre);
        
        assertEquals(1, hotel.getChambres().size());
        assertEquals(chambre, hotel.rechercherChambre(1));
    }

    @Test
    public void testAjouterChambreDupliquee() {
        ChambreSimple chambre1 = new ChambreSimple(1);
        ChambreSimple chambre2 = new ChambreSimple(1);
        
        hotel.ajouterChambre(chambre1);
        hotel.ajouterChambre(chambre2);
        
        assertEquals(1, hotel.getChambres().size());
    }

    @Test
    public void testRechercherChambre() {
        ChambreSimple chambre = new ChambreSimple(5);
        hotel.ajouterChambre(chambre);
        
        Chambre trouvee = hotel.rechercherChambre(5);
        assertNotNull(trouvee);
        assertEquals(5, trouvee.getNumero());
        
        Chambre nonTrouvee = hotel.rechercherChambre(999);
        assertNull(nonTrouvee);
    }

    @Test
    public void testRechercherChambresParType() {
        hotel.ajouterChambre(new ChambreSimple(1));
        hotel.ajouterChambre(new ChambreDouble(2, false));
        hotel.ajouterChambre(new ChambreSimple(3));
        
        assertEquals(2, hotel.rechercherChambresParType("Simple").size());
        assertEquals(1, hotel.rechercherChambresParType("Double").size());
    }

    @Test
    public void testGetChambresDisponibles() {
        ChambreSimple chambre1 = new ChambreSimple(1);
        ChambreSimple chambre2 = new ChambreSimple(2);
        chambre2.setOccupee(true);
        
        hotel.ajouterChambre(chambre1);
        hotel.ajouterChambre(chambre2);
        
        assertEquals(1, hotel.getChambresDisponibles().size());
        assertEquals(chambre1, hotel.getChambresDisponibles().get(0));
    }

    @Test
    public void testAjouterClient() {
        Client client = new Client("Dupont", "Jean", "jean@test.com", "0612345678");
        hotel.ajouterClient(client);
        
        assertEquals(1, hotel.getClients().size());
        assertTrue(hotel.getClients().contains(client));
    }

    @Test
    public void testCreerReservation() {
        ChambreSimple chambre = new ChambreSimple(1);
        Client client = new Client("Dupont", "Jean", "jean@test.com", "0612345678");
        
        hotel.ajouterChambre(chambre);
        hotel.ajouterClient(client);
        
        Reservation res = hotel.creerReservation(client, chambre, "01/01/2024", "05/01/2024");
        
        assertNotNull(res);
        assertEquals(1, hotel.getReservations().size());
        assertTrue(chambre.isOccupee());
    }

    @Test
    public void testCreerReservationChambreOccupee() {
        ChambreSimple chambre = new ChambreSimple(1);
        chambre.setOccupee(true);
        Client client = new Client("Dupont", "Jean", "jean@test.com", "0612345678");
        
        hotel.ajouterChambre(chambre);
        hotel.ajouterClient(client);
        
        Reservation res = hotel.creerReservation(client, chambre, "01/01/2024", "05/01/2024");
        
        assertNull(res);
    }

    @Test
    public void testAjouterService() {
        Service service = new Service("Spa", "Massage relaxant", 50.0);
        hotel.ajouterService(service);
        
        assertEquals(1, hotel.getServicesDisponibles().size());
        assertTrue(hotel.getServicesDisponibles().contains(service));
    }

    @Test
    public void testGenererPrefixeChambre() {
        Hotel hotel1 = new Hotel("Hôtel Le Magnifique", "Paris");
        String prefixe1 = hotel1.genererPrefixeChambre();
        assertEquals("HTL", prefixe1);
        
        Hotel hotel2 = new Hotel("Hilton", "Paris");
        String prefixe2 = hotel2.genererPrefixeChambre();
        assertEquals("HIL", prefixe2);
    }

    @Test
    public void testGenererProchainNumeroChambre() {
        int num1 = hotel.genererProchainNumeroChambre();
        int num2 = hotel.genererProchainNumeroChambre();
        
        assertTrue(num2 > num1);
    }

    @Test
    public void testFormaterNumeroChambre() {
        String format1 = hotel.formaterNumeroChambre(1);
        assertTrue(format1.startsWith("HTL"));
        assertTrue(format1.length() == 8);
        
        String format2 = hotel.formaterNumeroChambre(123);
        assertTrue(format2.startsWith("HTL"));
    }
}

