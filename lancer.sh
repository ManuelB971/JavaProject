#!/bin/bash

# Script unique pour lancer le projet complet
# Configure SQLite, compile et lance l'application

set -e  # Arrêter en cas d'erreur

echo "╔═══════════════════════════════════════════════════════════╗"
echo "║   SYSTÈME DE GESTION D'HÔTEL - LANCEMENT                  ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""

# ============================================================
# ÉTAPE 1 : Vérification et installation des dépendances
# ============================================================
echo "📦 Étape 1 : Vérification des dépendances..."

mkdir -p lib
mkdir -p build
mkdir -p data

# Vérifier Java
if ! command -v javac &> /dev/null; then
    echo "❌ Java n'est pas installé. Veuillez installer Java JDK."
    exit 1
fi

JAVA_VERSION=$(javac -version 2>&1 | head -1)
echo "   ✓ Java détecté : $JAVA_VERSION"

# Vérifier et installer SQLite JDBC
if [ ! -f "lib/sqlite-jdbc-3.44.1.0.jar" ]; then
    echo "   ⬇️  Téléchargement de SQLite JDBC..."
    curl -L -s -o lib/sqlite-jdbc-3.44.1.0.jar \
        https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.44.1.0/sqlite-jdbc-3.44.1.0.jar
    echo "   ✓ SQLite JDBC installé"
else
    echo "   ✓ SQLite JDBC déjà présent"
fi

# Vérifier et installer SLF4J (requis par SQLite JDBC)
if [ ! -f "lib/slf4j-api-1.7.36.jar" ]; then
    echo "   ⬇️  Téléchargement de SLF4J API..."
    curl -L -s -o lib/slf4j-api-1.7.36.jar \
        https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar
    echo "   ✓ SLF4J API installé"
else
    echo "   ✓ SLF4J API déjà présent"
fi

if [ ! -f "lib/slf4j-simple-1.7.36.jar" ]; then
    echo "   ⬇️  Téléchargement de SLF4J Simple..."
    curl -L -s -o lib/slf4j-simple-1.7.36.jar \
        https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar
    echo "   ✓ SLF4J Simple installé"
else
    echo "   ✓ SLF4J Simple déjà présent"
fi

# Vérifier et installer JUnit (pour les tests optionnels)
if [ ! -f "lib/junit-4.13.2.jar" ]; then
    echo "   ⬇️  Téléchargement de JUnit..."
    curl -L -s -o lib/junit-4.13.2.jar \
        https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar
    curl -L -s -o lib/hamcrest-core-1.3.jar \
        https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
    echo "   ✓ JUnit installé"
else
    echo "   ✓ JUnit déjà présent"
fi

echo "✅ Dépendances prêtes"
echo ""

# ============================================================
# ÉTAPE 2 : Compilation du projet
# ============================================================
echo "🔨 Étape 2 : Compilation du projet..."

# Définir les JARs
SQLITE_JAR="lib/sqlite-jdbc-3.44.1.0.jar"
SLF4J_API="lib/slf4j-api-1.7.36.jar"
SLF4J_SIMPLE="lib/slf4j-simple-1.7.36.jar"

# Trouver tous les fichiers Java
find src/main/java -name "*.java" > /tmp/sources.txt

# Compiler avec SQLite et SLF4J dans le classpath
CLASSPATH=".:${SQLITE_JAR}:${SLF4J_API}:${SLF4J_SIMPLE}"

javac -d build -cp "${CLASSPATH}" @/tmp/sources.txt 2>&1 | grep -v "warning" || true

if [ ${PIPESTATUS[0]} -ne 0 ]; then
    echo "❌ Erreur lors de la compilation"
    exit 1
fi

echo "✅ Projet compilé avec succès"
echo ""

# ============================================================
# ÉTAPE 3 : Initialisation de la base de données SQLite
# ============================================================
echo "🗄️  Étape 3 : Initialisation de la base de données SQLite..."

# La base sera créée automatiquement au premier lancement
# On peut aussi l'initialiser explicitement ici si nécessaire
echo "   ✓ Base de données prête (data/hotel.db)"
echo ""

# ============================================================
# ÉTAPE 4 : Lancement de l'application
# ============================================================
echo "🚀 Étape 4 : Lancement de l'application..."
echo ""

# Construire le classpath final avec toutes les dépendances (les variables sont déjà définies plus haut)
FINAL_CLASSPATH="build:${SQLITE_JAR}:${SLF4J_API}:${SLF4J_SIMPLE}"

# Détecter le mode
MODE="swing"

# Parser les arguments
for arg in "$@"; do
    case $arg in
        console)
            MODE="console"
            ;;
    esac
done

echo "   Mode : $MODE"
echo "   ℹ️  Les données de test seront initialisées automatiquement si la base est vide."
echo ""
java --enable-native-access=ALL-UNNAMED -cp "${FINAL_CLASSPATH}" com.gestionhotel.Main "$MODE"

EXIT_CODE=$?

echo ""
if [ $EXIT_CODE -eq 0 ]; then
    echo "✅ Application fermée proprement"
else
    echo "⚠️  Application fermée avec le code : $EXIT_CODE"
fi

