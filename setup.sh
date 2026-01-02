#!/bin/bash
# Script de configuration du projet Java avec Maven et JUnit

echo "======================================"
echo "Configuration du Projet Gestion Hôtel"
echo "======================================"
echo ""

# Vérifier Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven n'est pas installé. Veuillez installer Maven avant de continuer."
    exit 1
fi

echo "✅ Maven trouvé: $(mvn --version | head -1)"
echo ""

# Nettoyer et construire
echo "📦 Compilation du projet..."
mvn clean compile test-compile -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Compilation réussie!"
    echo ""
    echo "📋 Options disponibles:"
    echo "  - Exécuter les tests: mvn test"
    echo "  - Construire le JAR: mvn package"
    echo "  - Exécuter l'application: mvn exec:java -Dexec.mainClass='com.gestionhotel.Main'"
else
    echo ""
    echo "❌ Erreur lors de la compilation"
    exit 1
fi
