package com.gestionhotel.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.gestionhotel.core.Hotel;

/**
 * Tests unitaires pour la classe Reservation.
 * Vérifie la création, le calcul des prix, et la gestion des statuts.
 * 
 * @author Tests Phase 4
 */
public class TestReservation {

    private Client client;
    private ChambreSimple chambre;
    private Hotel hotel;

    @Before
    public void setUp() {
        hotel = new Hotel("Hôtel Test", "123 Rue Test");
        client = new Client("Dupont", "Jean", "jean@test.com", "0612345678");
        chambre = new ChambreSimple(1);
        hotel.ajouterClient(client);
        hotel.ajouterChambre(chambre);
    }

    @Test
    public void testCreationReservation() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        
        assertNotNull(res);
        assertEquals(client, res.getClient());
        assertEquals(chambre, res.getChambre());
        assertEquals("01/01/2024", res.getDateDebut());
        assertEquals("05/01/2024", res.getDateFin());
        assertEquals("En cours", res.getStatut());
    }

    @Test
    public void testCalculerNombreNuits() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        int nuits = res.calculerNombreNuits();
        assertEquals(4, nuits);
    }

    @Test
    public void testCalculerPrixTotal() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        double prix = res.calculerPrixTotal();
        
        // Prix = (prix par nuit * nombre de nuits) + services
        double prixAttendu = chambre.getPrixParNuit() * 4;
        assertEquals(prixAttendu, prix, 0.01);
    }

    @Test
    public void testCalculerPrixTotalAvecServices() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        Service service = new Service("Spa", "Massage", 50.0);
        res.ajouterService(service);
        
        double prix = res.calculerPrixTotal();
        double prixAttendu = (chambre.getPrixParNuit() * 4) + 50.0;
        assertEquals(prixAttendu, prix, 0.01);
    }

    @Test
    public void testCalculerPrixTotalAvecFidelite() {
        // Créer plusieurs réservations pour obtenir le statut Or (10%)
        for (int i = 0; i < 3; i++) {
            ChambreSimple ch = new ChambreSimple(i + 10);
            hotel.ajouterChambre(ch);
            Reservation r = hotel.creerReservation(client, ch, "01/01/2024", "05/01/2024");
            if (r != null) {
                r.confirmer();
            }
        }
        
        Reservation res = new Reservation(client, chambre, "10/01/2024", "15/01/2024");
        double prixBase = res.calculerPrixTotal();
        double prixAvecFidelite = res.calculerPrixTotalAvecFidelite(hotel);
        
        // Prix avec réduction de 10% (statut Or)
        double prixAttendu = prixBase * 0.90;
        assertEquals(prixAttendu, prixAvecFidelite, 0.01);
    }

    @Test
    public void testConfirmer() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        res.confirmer();
        
        assertEquals("Confirmée", res.getStatut());
    }

    @Test
    public void testAnnuler() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        res.annuler("Changement de plan");
        
        assertEquals("Annulée", res.getStatut());
        assertTrue(res.estAnnulee());
        assertNotNull(res.getRaison());
    }

    @Test
    public void testTerminer() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        res.confirmer();
        res.terminer();
        
        assertEquals("Terminée", res.getStatut());
    }

    @Test
    public void testAjouterService() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        Service service = new Service("Petit-déjeuner", "Buffet", 15.0);
        
        res.ajouterService(service);
        
        assertTrue(res.getServices().contains(service));
    }

    @Test
    public void testRetirerService() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        Service service = new Service("Petit-déjeuner", "Buffet", 15.0);
        
        res.ajouterService(service);
        res.retirerService(service);
        
        assertFalse(res.getServices().contains(service));
    }

    @Test
    public void testEstAnnulee() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        assertFalse(res.estAnnulee());
        
        res.annuler("Test");
        assertTrue(res.estAnnulee());
    }

    @Test
    public void testEstConfirmee() {
        Reservation res = new Reservation(client, chambre, "01/01/2024", "05/01/2024");
        assertFalse(res.estConfirmee());
        
        res.confirmer();
        assertTrue(res.estConfirmee());
    }
}

