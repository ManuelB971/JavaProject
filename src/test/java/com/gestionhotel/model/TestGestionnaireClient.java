package com.gestionhotel.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.gestionhotel.model.Client;
import com.gestionhotel.model.GestionnaireClient;

/**
 * Classe de tests unitaires pour GestionnaireClient.
 * Teste toutes les fonctionnalités du gestionnaire de clients.
 * 
 * @author Dev 2 (Phase 3)
 */
public class TestGestionnaireClient {

    private GestionnaireClient gestionnaire;
    private Client client1;
    private Client client2;
    private Client client3;

    /**
     * Initialisation avant chaque test.
     * Crée un gestionnaire vierge et des clients de test.
     */
    @Before
    public void setUp() {
        gestionnaire = new GestionnaireClient();
        client1 = new Client("Dupont", "Jean", "jean.dupont@email.com", "0123456789");
        client2 = new Client("Martin", "Marie", "marie.martin@email.com", "0987654321");
        client3 = new Client("Bernard", "Pierre", "pierre.bernard@email.com", "0555555555");
    }

    // ===========================
    // TESTS D'AJOUT
    // ===========================

    @Test
    public void testAjouterClientValide() {
        // Arrange & Act
        boolean resultat = gestionnaire.ajouterClient(client1);

        // Assert
        assertTrue("L'ajout d'un client valide doit retourner true", resultat);
        assertEquals("Le nombre de clients doit être 1", 1, gestionnaire.obtenirNombreClients());
    }

    @Test
    public void testAjouterClientNull() {
        // Act
        boolean resultat = gestionnaire.ajouterClient(null);

        // Assert
        assertFalse("L'ajout d'un client null doit retourner false", resultat);
        assertEquals("Le nombre de clients doit rester 0", 0, gestionnaire.obtenirNombreClients());
    }

    @Test
    public void testAjouterPlusieursClients() {
        // Act
        gestionnaire.ajouterClient(client1);
        gestionnaire.ajouterClient(client2);
        gestionnaire.ajouterClient(client3);

        // Assert
        assertEquals("Le nombre de clients doit être 3", 3, gestionnaire.obtenirNombreClients());
    }

    // ===========================
    // TESTS DE SUPPRESSION
    // ===========================

    @Test
    public void testSupprimerClient() {
        // Arrange
        gestionnaire.ajouterClient(client1);
        int numeroClient = client1.getNumeroClient();

        // Act
        boolean resultat = gestionnaire.supprimerClient(numeroClient);

        // Assert
        assertTrue("La suppression d'un client existant doit retourner true", resultat);
        assertEquals("Le nombre de clients doit être 0", 0, gestionnaire.obtenirNombreClients());
    }

    @Test
    public void testSupprimerClientInexistant() {
        // Act
        boolean resultat = gestionnaire.supprimerClient(999);

        // Assert
        assertFalse("La suppression d'un client inexistant doit retourner false", resultat);
    }

    // ===========================
    // TESTS DE RÉCUPÉRATION
    // ===========================

    @Test
    public void testObtenirClientExistant() {
        // Arrange
        gestionnaire.ajouterClient(client1);
        int numeroClient = client1.getNumeroClient();

        // Act
        Client client = gestionnaire.obtenirClient(numeroClient);

        // Assert
        assertNotNull("Le client ne doit pas être null", client);
        assertEquals("Le client obtenu doit être client1", client1, client);
    }

    @Test
    public void testObtenirClientInexistant() {
        // Act
        Client client = gestionnaire.obtenirClient(999);

        // Assert
        assertNull("Le client inexistant doit retourner null", client);
    }

    // ===========================
    // TESTS DE RECHERCHE
    // ===========================

    @Test
    public void testRechercherParEmail() {
        // Arrange
        gestionnaire.ajouterClient(client1);

        // Act
        Client client = gestionnaire.rechercherParEmail("jean.dupont@email.com");

        // Assert
        assertNotNull("Le client doit être trouvé", client);
        assertEquals("Le client trouvé doit être client1", client1, client);
    }

    @Test
    public void testRechercherParEmailInexistant() {
        // Arrange
        gestionnaire.ajouterClient(client1);

        // Act
        Client client = gestionnaire.rechercherParEmail("inexistant@email.com");

        // Assert
        assertNull("Aucun client ne doit être trouvé", client);
    }

    @Test
    public void testRechercherParTelephone() {
        // Arrange
        gestionnaire.ajouterClient(client1);

        // Act
        Client client = gestionnaire.rechercherParTelephone("0123456789");

        // Assert
        assertNotNull("Le client doit être trouvé", client);
        assertEquals("Le client trouvé doit être client1", client1, client);
    }

    @Test
    public void testRechercherParTelephoneInexistant() {
        // Arrange
        gestionnaire.ajouterClient(client1);

        // Act
        Client client = gestionnaire.rechercherParTelephone("0000000000");

        // Assert
        assertNull("Aucun client ne doit être trouvé", client);
    }

    // ===========================
    // TESTS DE MODIFICATION
    // ===========================

    @Test
    public void testModifierClient() {
        // Arrange
        gestionnaire.ajouterClient(client1);
        int numeroClient = client1.getNumeroClient();

        // Act
        boolean resultat = gestionnaire.modifierClient(numeroClient, "Durand", "Jacques", "jacques@email.com", "0111111111");

        // Assert
        assertTrue("La modification doit retourner true", resultat);
        assertEquals("Le nom doit être modifié", "Durand", client1.getNom());
        assertEquals("Le prénom doit être modifié", "Jacques", client1.getPrenom());
        assertEquals("L'email doit être modifié", "jacques@email.com", client1.getEmail());
        assertEquals("Le téléphone doit être modifié", "0111111111", client1.getTelephone());
    }

    @Test
    public void testModifierClientInexistant() {
        // Act
        boolean resultat = gestionnaire.modifierClient(999, "Nom", "Prenom", "email@test.com", "0000000000");

        // Assert
        assertFalse("La modification d'un client inexistant doit retourner false", resultat);
    }

    // ===========================
    // TESTS DE VÉRIFICATION
    // ===========================

    @Test
    public void testEmailExiste() {
        // Arrange
        gestionnaire.ajouterClient(client1);

        // Act & Assert
        assertTrue("L'email existant doit être trouvé", gestionnaire.emailExiste("jean.dupont@email.com"));
        assertFalse("L'email inexistant ne doit pas être trouvé", gestionnaire.emailExiste("inexistant@email.com"));
    }

    @Test
    public void testTelephoneExiste() {
        // Arrange
        gestionnaire.ajouterClient(client1);

        // Act & Assert
        assertTrue("Le téléphone existant doit être trouvé", gestionnaire.telephoneExiste("0123456789"));
        assertFalse("Le téléphone inexistant ne doit pas être trouvé", gestionnaire.telephoneExiste("0000000000"));
    }

    @Test
    public void testClientExiste() {
        // Arrange
        gestionnaire.ajouterClient(client1);
        int numeroClient = client1.getNumeroClient();

        // Act & Assert
        assertTrue("Le client existant doit être trouvé", gestionnaire.clientExiste(numeroClient));
        assertFalse("Le client inexistant ne doit pas être trouvé", gestionnaire.clientExiste(999));
    }

    // ===========================
    // TESTS D'AFFICHAGE
    // ===========================

    @Test
    public void testObtenirTousLesClients() {
        // Arrange
        gestionnaire.ajouterClient(client1);
        gestionnaire.ajouterClient(client2);
        gestionnaire.ajouterClient(client3);

        // Act
        var clients = gestionnaire.obtenirTousLesClients();

        // Assert
        assertNotNull("La liste ne doit pas être null", clients);
        assertEquals("La liste doit contenir 3 clients", 3, clients.size());
        assertTrue("client1 doit être dans la liste", clients.contains(client1));
        assertTrue("client2 doit être dans la liste", clients.contains(client2));
        assertTrue("client3 doit être dans la liste", clients.contains(client3));
    }

    @Test
    public void testObtenirTousLesClientsVide() {
        // Act
        var clients = gestionnaire.obtenirTousLesClients();

        // Assert
        assertNotNull("La liste ne doit pas être null", clients);
        assertEquals("La liste vide doit avoir 0 clients", 0, clients.size());
    }

    // ===========================
    // TESTS D'EFFACEMENT
    // ===========================

    @Test
    public void testEffacerTousLesClients() {
        // Arrange
        gestionnaire.ajouterClient(client1);
        gestionnaire.ajouterClient(client2);
        gestionnaire.ajouterClient(client3);
        assertEquals("3 clients doivent être ajoutés", 3, gestionnaire.obtenirNombreClients());

        // Act
        gestionnaire.effacerTousLesClients();

        // Assert
        assertEquals("Aucun client ne doit rester", 0, gestionnaire.obtenirNombreClients());
    }

    // ===========================
    // TESTS D'INTÉGRITÉ
    // ===========================

    @Test
    public void testAjouterDoublon() {
        // Arrange
        gestionnaire.ajouterClient(client1);
        
        // Act - Créer un autre client avec le même numéro (simulé par réutilisation)
        // Note: Dans la pratique, les numéros sont auto-incrémentés, donc les doublons
        // sont évités automatiquement par le compteur statique
        boolean result = gestionnaire.ajouterClient(client1);

        // Assert
        assertFalse("L'ajout d'un doublon doit retourner false", result);
    }

    @Test
    public void testNombreClientsAprèsOpérations() {
        // Arrange & Act
        gestionnaire.ajouterClient(client1);
        gestionnaire.ajouterClient(client2);
        assertEquals("2 clients doivent être présents", 2, gestionnaire.obtenirNombreClients());

        gestionnaire.supprimerClient(client1.getNumeroClient());
        assertEquals("1 client doit rester", 1, gestionnaire.obtenirNombreClients());

        gestionnaire.ajouterClient(client3);
        assertEquals("2 clients doivent être présents", 2, gestionnaire.obtenirNombreClients());
    }
}
