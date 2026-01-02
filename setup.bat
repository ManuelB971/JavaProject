@echo off
REM Script de configuration du projet Java avec Maven et JUnit

echo ======================================
echo Configuration du Projet Gestion Hotel
echo ======================================
echo.

REM Verifier Maven
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Maven n'est pas trouvé dans le PATH.
    echo Veuillez installer Maven ou ajouter Maven au PATH.
    echo.
    pause
    exit /b 1
)

echo ✅ Maven trouvé
echo.

REM Nettoyer et construire
echo 📦 Compilation du projet...
echo.

mvn clean compile test-compile -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ Compilation réussie!
    echo.
    echo 📋 Options disponibles:
    echo   - Exécuter les tests: mvn test
    echo   - Construire le JAR: mvn package
    echo   - Exécuter l'application: mvn exec:java -Dexec.mainClass="com.gestionhotel.Main"
    echo.
    echo 🎉 Le projet est prêt à utiliser!
    echo.
) else (
    echo.
    echo ❌ Erreur lors de la compilation
    echo.
)

pause
