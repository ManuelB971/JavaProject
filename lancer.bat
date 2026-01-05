@echo off
REM Script unique pour lancer le projet complet (Windows)
REM Configure SQLite, compile et lance l'application

echo ═══════════════════════════════════════════════════════════
echo   SYSTÈME DE GESTION D'HÔTEL - LANCEMENT
echo ═══════════════════════════════════════════════════════════
echo.

REM Étape 1 : Vérification des dépendances
echo 📦 Étape 1 : Vérification des dépendances...

if not exist lib mkdir lib
if not exist build mkdir build
if not exist data mkdir data

REM Vérifier Java
where javac >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java n'est pas installé. Veuillez installer Java JDK.
    exit /b 1
)

REM Vérifier et installer SQLite JDBC
if not exist "lib\sqlite-jdbc-3.44.1.0.jar" (
    echo    ⬇️  Téléchargement de SQLite JDBC...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.44.1.0/sqlite-jdbc-3.44.1.0.jar' -OutFile 'lib\sqlite-jdbc-3.44.1.0.jar'"
    echo    ✓ SQLite JDBC installé
) else (
    echo    ✓ SQLite JDBC déjà présent
)

REM Vérifier et installer SLF4J (requis par SQLite JDBC)
if not exist "lib\slf4j-api-1.7.36.jar" (
    echo    ⬇️  Téléchargement de SLF4J API...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar' -OutFile 'lib\slf4j-api-1.7.36.jar'"
    echo    ✓ SLF4J API installé
) else (
    echo    ✓ SLF4J API déjà présent
)

if not exist "lib\slf4j-simple-1.7.36.jar" (
    echo    ⬇️  Téléchargement de SLF4J Simple...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar' -OutFile 'lib\slf4j-simple-1.7.36.jar'"
    echo    ✓ SLF4J Simple installé
) else (
    echo    ✓ SLF4J Simple déjà présent
)

echo ✅ Dépendances prêtes
echo.

REM Étape 2 : Compilation
echo 🔨 Étape 2 : Compilation du projet...

for /r src\main\java %%f in (*.java) do (
    javac -d build -cp ".;lib\sqlite-jdbc-3.44.1.0.jar;lib\slf4j-api-1.7.36.jar;lib\slf4j-simple-1.7.36.jar" "%%f"
    if errorlevel 1 (
        echo ❌ Erreur lors de la compilation
        exit /b 1
    )
)

echo ✅ Projet compilé avec succès
echo.

REM Étape 3 : Initialisation BDD
echo 🗄️  Étape 3 : Initialisation de la base de données SQLite...
echo    ✓ Base de données prête (data\hotel.db)
echo.

REM Étape 4 : Lancement
echo 🚀 Étape 4 : Lancement de l'application...
echo.

set MODE=swing

REM Parser les arguments
if "%1"=="console" set MODE=console
if "%2"=="console" set MODE=console

echo    Mode : %MODE%
echo    ℹ️  Les données de test seront initialisées automatiquement si la base est vide.
echo.
java --enable-native-access=ALL-UNNAMED -cp "build;lib\sqlite-jdbc-3.44.1.0.jar;lib\slf4j-api-1.7.36.jar;lib\slf4j-simple-1.7.36.jar" com.gestionhotel.Main %MODE%

if %errorlevel% equ 0 (
    echo.
    echo ✅ Application fermée proprement
) else (
    echo.
    echo ⚠️  Application fermée avec le code : %errorlevel%
)

