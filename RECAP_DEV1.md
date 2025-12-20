# Récapitulatif Dev 1 (Mika)

---

## Phase 1 : Model (Terminé ✅)

### Ce qui a été fait :
- Création de la classe abstraite `Chambre` dans `com.gestionhotel.model.Chambre`.
- Implémentation des attributs de base :
  - `numero` (int)
  - `prixParNuit` (double)
  - `occupee` (boolean)
  - `capacite` (int)
- Implémentation des méthodes :
  - Constructeur complet.
  - Getters et Setters.
  - `calculerPrix(int nbNuits)`.
  - `toString()`.
  - Méthode abstraite `getType()` définie.

### Instructions pour Dev 2 (Phase 1 - Suite)
**Tâche :** Implémenter les classes filles `ChambreSimple` et `ChambreDouble`.

#### 1. Classe `ChambreSimple`
- **Héritage** : Doit étendre `Chambre`.
- **Constructeur** :
  - Appeler `super(numero, 50, 1)` (Prix de base: 50€, Capacité: 1).
- **Méthode `getType()`** :
  - Retourner "Simple".
- **Attributs** : Aucun supplémentaire.

#### 2. Classe `ChambreDouble`
- **Héritage** : Doit étendre `Chambre`.
- **Attributs** :
  - `boolean litsJumeaux` (true si 2 lits simples, false si 1 lit double).
- **Constructeur** :
  - Paramètres : `numero`, `litsJumeaux`.
  - Appeler `super(numero, 80, 2)` (Prix de base: 80€, Capacité: 2).
  - Initialiser `this.litsJumeaux`.
- **Méthode `getType()`** :
  - Retourner "Double".
- **Surcharge `toString()`** :
  - Ajouter l'info sur les lits (Jumeaux ou Lit double).

---

## Phase 2 : Réservations (Terminé ✅)

### Ce qui a été fait :
- Création de la classe `Reservation` dans `com.gestionhotel.model.Reservation`.
- Implémentation des attributs :
  - `numeroReservation` (int, auto-incrémenté via static)
  - `client` (Client)
  - `chambre` (Chambre)
  - `dateDebut` (String, format "jj/mm/aaaa")
  - `dateFin` (String, format "jj/mm/aaaa")
  - `services` (ArrayList<Service>)
  - `statut` (String : "En cours", "Confirmée", "Annulée", "Terminée")
- Implémentation des méthodes :
  - Constructeur complet (marque la chambre comme occupée).
  - Getters et Setters.
  - `calculerNombreNuits()` : calcule le nombre de nuits via DateUtils.
  - `calculerPrixChambre()` : prix total de la chambre pour le séjour.
  - `calculerPrixServices()` : somme des prix de tous les services.
  - `calculerPrixTotal()` : chambre + services.
  - `ajouterService(Service s)` : ajoute un service à la réservation.
  - `annuler()` : annule la réservation et libère la chambre.
  - `terminer()` : termine la réservation (check-out).
  - `confirmer()` : confirme la réservation.
  - `toString()` : affichage complet de la réservation.

### Instructions pour Dev 2 (Phase 2 - Suite)
**Tâche :** Intégration Client/Chambre dans le système.

### Instructions pour Dev 3 (Phase 2 - Suite)
**Tâche :** Statut + annulation des réservations.

### Instructions pour Dev 4 (Phase 2 - Suite)
**Tâche :** ValidationUtils + Exceptions personnalisées.

---

## Phase 3 : Hotel Core (Terminé ✅)

### Ce qui a été fait :
- Implémentation complète de la classe `Hotel` dans `com.gestionhotel.core.Hotel`.
- Attributs :
  - `nom` (String)
  - `adresse` (String)
  - `chambres` (ArrayList<Chambre>)
  - `clients` (ArrayList<Client>)
  - `reservations` (ArrayList<Reservation>)
  - `servicesDisponibles` (ArrayList<Service>)

### Méthodes de gestion des chambres (Dev 1) :
- `ajouterChambre(Chambre c)` : ajoute une chambre (vérifie doublon).
- `afficherToutesLesChambres()` : affiche toutes les chambres.
- `afficherChambresDisponibles()` : affiche les chambres libres.
- `getChambresDisponibles()` : retourne ArrayList des chambres libres.
- `rechercherChambre(int numero)` : recherche par numéro.
- `rechercherChambresParType(String type)` : recherche par type (Simple/Double/Suite).
- `rechercherChambresParPrix(double prixMax)` : recherche par prix max.

### Méthodes de gestion des clients (Dev 2) :
- `ajouterClient(Client c)` : ajoute un client.
- `afficherTousLesClients()` : affiche tous les clients.
- `rechercherClient(int numero)` : recherche par numéro.
- `rechercherClientParEmail(String email)` : recherche par email.

### Méthodes de gestion des réservations (Dev 3) :
- `creerReservation(Client, Chambre, dateDebut, dateFin)` : crée une réservation.
- `afficherToutesLesReservations()` : affiche toutes les réservations.
- `afficherReservationsClient(Client c)` : affiche les réservations d'un client.
- `rechercherReservation(int numero)` : recherche par numéro.
- `annulerReservation(int numero)` : annule une réservation.
- `terminerReservation(int numero)` : termine une réservation (check-out).

### Méthodes de gestion des services :
- `ajouterService(Service s)` : ajoute un service.
- `afficherServices()` : affiche tous les services.

---

## Prochaines étapes (Phase 4)
- Phase 4 : MenuPrincipal + chambres/clients
