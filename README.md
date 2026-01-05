# 🏨 Système de Gestion d'Hôtel - Projet Java B3

Application Java complète de gestion d'hôtel avec interface graphique Swing, base de données SQLite, et système de fidélité.

## 📋 Table des matières

- [Fonctionnalités](#-fonctionnalités)
- [Structure du projet](#-structure-du-projet)
- [Installation et lancement](#-installation-et-lancement)
- [Architecture](#-architecture)
- [Phases du projet](#-phases-du-projet)
- [Technologies utilisées](#-technologies-utilisées)
- [Tests](#-tests)

## ✨ Fonctionnalités

### Gestion des chambres
- ✅ Création et gestion de chambres (Simple, Double, Suite)
- ✅ Génération automatique de numéros de chambres (format : HTLXXXXX)
- ✅ Recherche par type, prix, disponibilité
- ✅ Gestion de l'occupation

### Gestion des clients
- ✅ Création et modification de clients
- ✅ Validation des emails
- ✅ Système de fidélité (Bronze, Argent, Or, Platine)
- ✅ Calcul automatique des réductions

### Gestion des réservations
- ✅ Création de réservations avec validation des dates
- ✅ Calcul automatique des prix (chambre + services)
- ✅ Application des réductions de fidélité
- ✅ Gestion des statuts (En cours, Confirmée, Annulée, Terminée)
- ✅ Annulation avec raison

### Gestion des services
- ✅ Ajout de services supplémentaires (petit-déjeuner, spa, parking, etc.)
- ✅ Gestion de la disponibilité

### Statistiques
- ✅ Taux d'occupation
- ✅ Chiffre d'affaires
- ✅ Statistiques par type de chambre
- ✅ Statistiques des réservations

### Interface utilisateur
- ✅ **Interface console** complète avec menus interactifs
- ✅ **Interface graphique Swing** avec onglets :
  - Panneau Chambres
  - Panneau Clients
  - Panneau Réservations
  - Panneau Services
  - Panneau Statistiques
  - Panneau Fidélité

### Persistance des données
- ✅ Sauvegarde dans **base de données SQLite**
- ✅ Compatibilité avec sauvegarde fichiers texte
- ✅ Migration automatique des fichiers vers SQLite
- ✅ Initialisation automatique avec données de test

### Système de fidélité
- ✅ 4 niveaux : Bronze, Argent, Or, Platine
- ✅ Réductions automatiques : 5%, 10%, 15%
- ✅ Calcul des économies réalisées
- ✅ Affichage du statut et des avantages

## 📁 Structure du projet

```
JavaProject/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── gestionhotel/
│   │               ├── core/              # Classes métier principales
│   │               │   ├── Hotel.java
│   │               │   └── Statistiques.java
│   │               ├── model/             # Modèles de données
│   │               │   ├── Chambre.java (abstraite)
│   │               │   ├── ChambreSimple.java
│   │               │   ├── ChambreDouble.java
│   │               │   ├── Suite.java
│   │               │   ├── Client.java
│   │               │   ├── Reservation.java
│   │               │   ├── Service.java
│   │               │   └── GestionnaireClient.java
│   │               ├── exceptions/        # Exceptions personnalisées
│   │               │   ├── HotelException.java
│   │               │   ├── BusinessException.java
│   │               │   ├── ValidationException.java
│   │               │   └── NotFoundException.java
│   │               ├── utils/             # Utilitaires
│   │               │   ├── DateUtils.java
│   │               │   ├── ValidationUtils.java
│   │               │   ├── FilePersistence.java
│   │               │   ├── SQLiteDatabaseManager.java
│   │               │   ├── FideliteManager.java
│   │               │   └── DataInitializer.java
│   │               └── ui/                # Interfaces utilisateur
│   │                   ├── MenuPrincipal.java (console)
│   │                   ├── FenetrePrincipale.java (Swing)
│   │                   ├── PanneauChambres.java
│   │                   ├── PanneauClients.java
│   │                   ├── PanneauReservations.java
│   │                   ├── PanneauServices.java
│   │                   ├── PanneauStatistiques.java
│   │                   ├── PanneauFidelite.java
│   │                   └── SwingUtils.java
│   └── test/
│       └── java/
│           └── com/
│               └── gestionhotel/
│                   ├── model/
│                   │   └── TestGestionnaireClient.java
│                   └── utils/
│                       └── TestSQLiteDatabaseManager.java
├── data/                                  # Données persistantes
│   └── hotel.db                           # Base SQLite
├── lib/                                   # Dépendances
│   ├── sqlite-jdbc-*.jar
│   ├── slf4j-*.jar
│   └── junit-*.jar
├── lancer.sh                             # Script de lancement (Linux/Mac)
├── lancer.bat                            # Script de lancement (Windows)
└── README.md
```

## 🚀 Installation et lancement

### Prérequis
- Java JDK 8 ou supérieur
- Bash (Linux/Mac) ou PowerShell (Windows)

### Lancement rapide

**Linux/Mac :**
```bash
chmod +x lancer.sh
./lancer.sh
```

**Windows :**
```cmd
lancer.bat
```

Le script va automatiquement :
1. ✅ Vérifier Java
2. ✅ Télécharger les dépendances (SQLite JDBC, SLF4J, JUnit)
3. ✅ Compiler le projet
4. ✅ Initialiser la base de données
5. ✅ Lancer l'application (Swing par défaut)

### Modes de lancement

- **Interface graphique Swing** (par défaut) : `./lancer.sh`
- **Interface console** : `./lancer.sh console`

### Initialisation des données

Les données de test sont **automatiquement initialisées** lors du premier lancement si la base de données est vide. Elles incluent :
- 14 chambres (Simple, Double, Suite)
- 8 clients
- 8 services
- ~15 réservations avec différents statuts

## 🏗️ Architecture

### Design Patterns utilisés
- **Singleton** : `SQLiteDatabaseManager`
- **Factory** : Création de chambres par type
- **MVC** : Séparation Model-View-Controller (implicite)
- **Strategy** : Calcul des prix selon le type de chambre

### Gestion des erreurs
- ✅ Try-catch global dans `Main.java`
- ✅ Exceptions personnalisées (`HotelException`, `ValidationException`, etc.)
- ✅ Validation des données avec `ValidationUtils`
- ✅ Gestion robuste des erreurs SQL

### Persistance
- ✅ **SQLite** : Base de données relationnelle principale
- ✅ **Fichiers texte** : Compatibilité et migration automatique
- ✅ Sauvegarde automatique à la fermeture

## 📚 Phases du projet

### Phase 1 : Classes de base
- Modélisation des chambres (abstraction, héritage)
- Gestion des clients avec validation
- Services hôteliers

### Phase 2 : Réservations
- Création et gestion des réservations
- Calculs de prix (chambre + services)
- Gestion des statuts et annulations

### Phase 3 : Classe Hotel
- Gestion globale de l'hôtel
- Recherches et filtres
- Statistiques complètes

### Phase 4 : Interface utilisateur + Bonus
- Menu console interactif
- **Interface graphique Swing complète**
- **Système de fidélité avec réductions**
- **Persistance SQLite**
- Gestion d'erreurs robuste

## 🛠️ Technologies utilisées

- **Java** : Langage de programmation
- **Swing** : Interface graphique
- **SQLite** : Base de données embarquée
- **JUnit 4** : Tests unitaires
- **SLF4J** : Logging

## 🧪 Tests

### Exécution des tests

```bash
# Compiler les tests
javac -d build -cp ".:lib/*" src/test/java/com/gestionhotel/**/*.java

# Exécuter les tests
java -cp "build:lib/*" org.junit.runner.JUnitCore com.gestionhotel.utils.TestSQLiteDatabaseManager
```

### Couverture des tests
- ✅ Tests unitaires pour `SQLiteDatabaseManager`
- ✅ Tests pour `GestionnaireClient`
- ⚠️ Tests à ajouter : `Hotel`, `Reservation`, `FideliteManager`

------------------------------------------------------------------------

## Répartition du Travail

Projet réalisé en équipe avec répartition.
| Phase                 | Mika (Dev 1)                  | Manuel (Dev 2)                  | Yann (Dev 3)                     | Dominique (Dev 4)               | Tests Croisés (TOUS)                                          |
| --------------------- | ----------------------------- | ----------------------------- | --------------------------------- | ------------------------------- | ------------------------------------------------------------- |
| Phase 1: Model        | Chambre abstraite + interfaces | ChambreSimple/Double          | Suite + Client(email validation)  | Service + DateUtils              | D1 teste D2, D2 teste D3, D3 teste D4, D4 teste D1            |
| Phase 2: Réservations | Reservation(prix/nuits/services) | Intégration Client/Chambre    | Statut + annulation               | ValidationUtils + Exceptions     | Rotation : D1 teste D3, D2 teste D4, D3 teste D1, D4 teste D2 |
| Phase 3: Hotel Core   | Gestion chambres (recherche/dispo) | Gestion clients                | Réservations (créer/afficher)      | Statistiques + FilePersistence   | Tous testent Hotel ensemble (intégration)                     |
| Phase 4: UI + Bonus   | MenuPrincipal + chambres/clients | Menus réservations/services    | Stats + try-catch global          | Bonus fidélité + Swing          | Cross-tests UI + démo collective                              |

------------------------------------------------------------------------
