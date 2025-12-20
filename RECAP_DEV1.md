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

## Prochaines étapes (Phase 3 & 4)
- Phase 3 : Gestion chambres (recherche/dispo)
- Phase 4 : MenuPrincipal + chambres/clients
