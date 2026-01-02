# 🎯 Quick Start Guide - Projet Gestion Hôtel

## ⚡ 30 secondes pour démarrer

```bash
# 1. Compiler le projet
mvn clean install

# 2. Exécuter les tests
mvn test

# 3. Lancer l'application
mvn exec:java -Dexec.mainClass="com.gestionhotel.Main"
```

---

## 📋 Checklist - Configuration minimale

- ✅ `pom.xml` présent → Maven configure
- ✅ `mvn clean install` exécuté → Dépendances téléchargées
- ✅ `.classpath` présent → Eclipse/VSCode configuré
- ✅ `.vscode/extensions.json` → Extensions recommandées
- ✅ `mvn test` réussi → Tests JUnit fonctionnels

---

## 🧪 Tests JUnit

### Exécuter les tests
```bash
# Tous les tests
mvn test

# Un seul fichier de test
mvn test -Dtest=TestGestionnaireClient

# Avec rapport détaillé
mvn test -e
```

### Résultat attendu
```
Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 🏗️ Structure Maven

```
src/
├── main/java/          ← Code applicatif
│   └── com/gestionhotel/
│       ├── Main.java
│       ├── core/       ← Logique métier
│       ├── model/      ← Classes modèles
│       ├── ui/         ← Interface utilisateur
│       └── utils/      ← Utilitaires
└── test/java/          ← Tests unitaires
    └── com/gestionhotel/
        └── model/
            └── TestGestionnaireClient.java

target/                 ← Fichiers compilés (généré)
├── classes/            ← Code applicatif compilé
└── test-classes/       ← Tests compilés
```

---

## 🔧 Commandes courantes

| Commande | Description |
|----------|-------------|
| `mvn clean` | Nettoie les fichiers compilés |
| `mvn compile` | Compile le code source |
| `mvn test` | Exécute les tests |
| `mvn package` | Crée un JAR |
| `mvn clean install` | Tout nettoyer et compiler |
| `mvn exec:java -Dexec.mainClass="com.gestionhotel.Main"` | Exécute l'application |

---

## 💡 Tips & Tricks

### VS Code
- Installer "Extension Pack for Java" (Ctrl+Shift+X)
- Ouvrir un fichier de test et cliquer "Run Test"
- Ctrl+Shift+P → "Java: Test Explorer"

### IntelliJ IDEA
- Clic droit sur le test → Run
- Alt+Shift+F10 pour exécuter les derniers tests
- Ctrl+Shift+F10 pour debugger

### Maven
- `mvn dependency:tree` → Voir les dépendances
- `mvn help:describe -Dplugin=compiler` → Aide sur un plugin
- `mvn -v` → Vérifier la version

---

## 🆘 Problèmes courants

### "Cannot find JUnit"
```bash
mvn clean install  # Réinstaller les dépendances
```

### "Tests not found"
```bash
# Vérifier le chemin src/test/java/com/gestionhotel/...
# Les fichiers doivent commencer par "Test" ou finir par "Test"
```

### VS Code ne reconnaît pas JUnit
```
Ctrl+Shift+P → Java: Clean Language Server Workspace
```

---

## 📚 Documentation

- `JUNIT_SETUP.md` - Guide détaillé JUnit
- `JUNIT_RESOLUTION_REPORT.md` - Rapport de résolution
- `pom.xml` - Configuration Maven complète

---

**Status : ✅ Ready to use**
