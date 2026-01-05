package com.gestionhotel.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.gestionhotel.core.Hotel;
import com.gestionhotel.model.*;

/**
 * Tests unitaires pour FideliteManager.
 * Vérifie le calcul des statuts de fidélité et l'application des réductions.
 * 
 * @author Tests Phase 4
 */
public class TestFideliteManager {

    private Hotel hotel;
    private Client client;

    @Before
    public void setUp() {
        hotel = new Hotel("Hôtel Test", "123 Rue Test");
        client = new Client("Dupont", "Jean", "jean@test.com", "0612345678");
        hotel.ajouterClient(client);
    }

    @Test
    public void testStatutBronze() {
        String statut = FideliteManager.calculerStatutFidelite(client, hotel);
        assertEquals("Bronze", statut);
    }

    @Test
    public void testStatutArgent() {
        creerReservations(1);
        String statut = FideliteManager.calculerStatutFidelite(client, hotel);
        assertEquals("Argent", statut);
    }

    @Test
    public void testStatutOr() {
        creerReservations(3);
        String statut = FideliteManager.calculerStatutFidelite(client, hotel);
        assertEquals("Or", statut);
    }

    @Test
    public void testStatutPlatine() {
        creerReservations(5);
        String statut = FideliteManager.calculerStatutFidelite(client, hotel);
        assertEquals("Platine", statut);
    }

    @Test
    public void testReductionBronze() {
        double reduction = FideliteManager.calculerReduction(client, hotel);
        assertEquals(0.0, reduction, 0.01);
    }

    @Test
    public void testReductionArgent() {
        creerReservations(1);
        double reduction = FideliteManager.calculerReduction(client, hotel);
        assertEquals(5.0, reduction, 0.01);
    }

    @Test
    public void testReductionOr() {
        creerReservations(3);
        double reduction = FideliteManager.calculerReduction(client, hotel);
        assertEquals(10.0, reduction, 0.01);
    }

    @Test
    public void testReductionPlatine() {
        creerReservations(5);
        double reduction = FideliteManager.calculerReduction(client, hotel);
        assertEquals(15.0, reduction, 0.01);
    }

    @Test
    public void testAppliquerReduction() {
        creerReservations(3); // Statut Or = 10%
        double prixInitial = 100.0;
        double prixAvecReduction = FideliteManager.appliquerReductionFidelite(prixInitial, client, hotel);
        assertEquals(90.0, prixAvecReduction, 0.01);
    }

    @Test
    public void testAppliquerReductionPlatine() {
        creerReservations(5); // Statut Platine = 15%
        double prixInitial = 200.0;
        double prixAvecReduction = FideliteManager.appliquerReductionFidelite(prixInitial, client, hotel);
        assertEquals(170.0, prixAvecReduction, 0.01);
    }

    @Test
    public void testObtenirNombreReservations() {
        creerReservations(3);
        int nombre = FideliteManager.obtenirNombreReservations(client, hotel);
        assertEquals(3, nombre);
    }

    @Test
    public void testObtenirNombreReservationsAvecAnnulees() {
        creerReservations(3);
        // Annuler une réservation
        if (!hotel.getReservations().isEmpty()) {
            hotel.getReservations().get(0).annuler("Test");
        }
        int nombre = FideliteManager.obtenirNombreReservations(client, hotel);
        assertEquals(2, nombre); // Les annulées ne comptent pas
    }

    @Test
    public void testObtenirDepenseTotale() {
        creerReservations(2);
        double depense = FideliteManager.obtenirDepenseTotale(client, hotel);
        assertTrue(depense > 0);
    }

    @Test
    public void testCalculerEconomiesTotales() {
        creerReservations(3); // Or = 10%
        double depense = FideliteManager.obtenirDepenseTotale(client, hotel);
        double economies = FideliteManager.calculerEconomiesTotales(client, hotel);
        double expected = depense * 0.10;
        assertEquals(expected, economies, 0.01);
    }

    @Test
    public void testObtenirReservationsPourNiveauSuperieur() {
        creerReservations(2); // Or nécessite 3, donc 1 de plus
        int necessaires = FideliteManager.obtenirReservationsPourNiveauSuperieur(client, hotel);
        assertEquals(1, necessaires);
    }

    @Test
    public void testObtenirReservationsPourNiveauSuperieurPlatine() {
        creerReservations(5); // Déjà Platine
        int necessaires = FideliteManager.obtenirReservationsPourNiveauSuperieur(client, hotel);
        assertEquals(0, necessaires);
    }

    @Test
    public void testAvecClientNull() {
        String statut = FideliteManager.calculerStatutFidelite(null, hotel);
        assertEquals("Bronze", statut);
        
        double reduction = FideliteManager.calculerReduction(null, hotel);
        assertEquals(0.0, reduction, 0.01);
    }

    @Test
    public void testAvecHotelNull() {
        String statut = FideliteManager.calculerStatutFidelite(client, null);
        assertEquals("Bronze", statut);
    }

    // Méthode utilitaire pour créer des réservations
    private void creerReservations(int nombre) {
        ChambreSimple chambre = new ChambreSimple(1);
        hotel.ajouterChambre(chambre);
        
        for (int i = 0; i < nombre; i++) {
            ChambreSimple ch = new ChambreSimple(i + 10);
            hotel.ajouterChambre(ch);
            Reservation res = hotel.creerReservation(client, ch, "01/01/2024", "05/01/2024");
            if (res != null) {
                res.confirmer();
            }
        }
    }
}

