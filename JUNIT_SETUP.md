# Configuration JUnit - Guide Complet

## ✅ Statut

Le projet est maintenant **complètement configuré** pour JUnit 4 avec Maven.

## 📦 Ce qui a été installé

- **Maven 3.9.12** : Gestionnaire de dépendances et d'build
- **JUnit 4.13.2** : Framework de tests unitaires
- **pom.xml** : Fichier de configuration Maven avec JUnit déclaré en dépendance de test

## 🚀 Démarrage rapide

### Option 1 : Utiliser Maven directement

```bash
# Compiler le projet et les tests
mvn clean compile test-compile

# Exécuter tous les tests
mvn test

# Exécuter un test spécifique
mvn test -Dtest=TestGestionnaireClient

# Construire un JAR exécutable
mvn package

# Exécuter l'application
mvn exec:java -Dexec.mainClass="com.gestionhotel.Main"
```

### Option 2 : Utiliser les scripts de setup

**Windows:**
```bash
setup.bat
```

**Linux/Mac:**
```bash
bash setup.sh
```

## 📊 Structure du projet

```
JavaProject-main/
├── pom.xml                          # Configuration Maven avec JUnit
├── .classpath                       # Configuration Eclipse
├── .project                         # Configuration Eclipse
├── .vscode/
│   ├── settings.json               # Configuration VS Code
│   └── extensions.json             # Extensions recommandées
├── src/
│   ├── main/java/com/gestionhotel/
│   │   ├── Main.java
│   │   ├── core/
│   │   ├── model/
│   │   ├── ui/
│   │   └── utils/
│   └── test/java/com/gestionhotel/
│       └── model/
│           └── TestGestionnaireClient.java
└── target/                         # Fichiers compilés (généré)
    ├── classes/
    └── test-classes/
```

## 🔧 Configuration VS Code

VS Code devrait reconnaître automatiquement JUnit une fois que:

1. ✅ Le pom.xml est présent
2. ✅ Maven a compilé le projet (`mvn clean install`)
3. ✅ L'extension "Extension Pack for Java" est installée

Si les erreurs JUnit persistent:
1. Ouvrir la Palette de Commandes (Ctrl+Shift+P)
2. Tapez : "Java: Clean Language Server Workspace"
3. Confirmez pour redémarrer le serveur de langage

## 📝 Utilisation JUnit dans le code

```java
import org.junit.Before;     // Initialisation avant chaque test
import org.junit.Test;       // Annotation pour marquer un test
import static org.junit.Assert.*;  // Assertions de test

public class TestMaClasse {
    
    @Before
    public void setUp() {
        // Code d'initialisation avant chaque test
    }
    
    @Test
    public void testMaFonctionnalite() {
        // Arrange
        MaClasse obj = new MaClasse();
        
        // Act
        boolean resultat = obj.maFonctionnalite();
        
        // Assert
        assertTrue("Le message d'erreur", resultat);
    }
}
```

## 📚 Assertions JUnit courantes

```java
assertTrue(boolean)              // Vérifie que le booléen est true
assertFalse(boolean)             // Vérifie que le booléen est false
assertEquals(expected, actual)   // Vérifie l'égalité
assertNotEquals(expected, actual)// Vérifie l'inégalité
assertNull(object)               // Vérifie que l'objet est null
assertNotNull(object)            // Vérifie que l'objet n'est pas null
fail(message)                    // Force l'échec du test avec un message
```

## 🧪 Exécuter les tests

### Via Maven (recommandé)
```bash
mvn test
```

### Via VS Code
1. Ouvrir TestGestionnaireClient.java
2. Cliquer sur "Run Test" au-dessus de la classe ou de la méthode de test
3. Les résultats apparaissent dans le panneau "Test Explorer"

### Via IDE (IntelliJ, Eclipse)
1. Clic droit sur la classe de test
2. Sélectionner "Run" ou "Run with Coverage"

## 🐛 Dépannage

### Problème : "The import org.junit cannot be resolved"

**Solution 1 :** Recompiler avec Maven
```bash
mvn clean compile test-compile
```

**Solution 2 :** Nettoyer le cache VS Code
1. Ctrl+Shift+P → "Java: Clean Language Server Workspace"
2. Redémarrez VS Code

**Solution 3 :** Vérifier que les dépendances sont téléchargées
```bash
mvn dependency:resolve
```

### Problème : Tests ne s'exécutent pas

Vérifiez que:
- ✅ Les classes de test sont dans `src/test/java`
- ✅ Les noms des fichiers commencent par "Test" ou finissent par "Test"
- ✅ Les méthodes de test sont annotées avec `@Test`
- ✅ Les méthodes d'initialisation sont annotées avec `@Before`

### Problème : "Cannot resolve symbol 'Test'"

Le fichier ne compile pas car JUnit manque. Exécutez:
```bash
mvn clean install
```

## 📈 Prochaines étapes

1. **Ajouter plus de tests** : TestClient.java, TestReservation.java, etc.
2. **Coverage des tests** : Ajouter le plugin JaCoCo pour mesurer la couverture
3. **Integration tests** : Ajouter des tests d'intégration avec des bases de données
4. **CI/CD** : Configurer GitHub Actions pour exécuter les tests automatiquement

## 📞 Support

Pour plus d'informations:
- [Documentation Maven](https://maven.apache.org/)
- [Documentation JUnit 4](https://junit.org/junit4/)
- [VS Code Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

---

**Statut du projet : ✅ Complet et fonctionnel**
