package com.gestionhotel.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Classe utilitaire pour la manipulation des dates dans le système hôtelier.
 * Fournit des méthodes pour formater, valider et calculer des différences entre dates.
 * 
 * @author Dev 4
 */
public class DateUtils {
    
    // Formateurs de date
    private static final DateTimeFormatter FORMAT_FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMAT_US = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Formate une date au format français (dd/MM/yyyy).
     * 
     * @param date La date à formater
     * @return La date formatée en chaîne de caractères
     */
    public static String formaterDateFR(LocalDate date) {
        return date.format(FORMAT_FR);
    }
    
    /**
     * Formate une date au format américain (yyyy-MM-dd).
     * 
     * @param date La date à formater
     * @return La date formatée en chaîne de caractères
     */
    public static String formaterDateUS(LocalDate date) {
        return date.format(FORMAT_US);
    }
    
    /**
     * Parse une chaîne de caractères en date selon le format français.
     * 
     * @param dateStr La chaîne de caractères représentant une date
     * @return La date parsée
     * @throws IllegalArgumentException Si le format est invalide
     */
    public static LocalDate parserDateFR(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMAT_FR);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez le format JJ/MM/AAAA");
        }
    }
    
    /**
     * Parse une chaîne de caractères en date selon le format américain.
     * 
     * @param dateStr La chaîne de caractères représentant une date
     * @return La date parsée
     * @throws IllegalArgumentException Si le format est invalide
     */
    public static LocalDate parserDateUS(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMAT_US);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez le format AAAA-MM-JJ");
        }
    }
    
    /**
     * Calcule le nombre de nuits entre deux dates.
     * 
     * @param dateArrivee Date d'arrivée
     * @param dateDepart  Date de départ
     * @return Le nombre de nuits
     * @throws IllegalArgumentException Si les dates sont invalides
     */
    public static long calculerNuits(LocalDate dateArrivee, LocalDate dateDepart) {
        if (dateArrivee.isAfter(dateDepart)) {
            throw new IllegalArgumentException("La date d'arrivée doit être avant la date de départ");
        }
        return ChronoUnit.DAYS.between(dateArrivee, dateDepart);
    }
    
    /**
     * Vérifie si une date est dans le futur.
     * 
     * @param date La date à vérifier
     * @return true si la date est future, false sinon
     */
    public static boolean estDateFuture(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Vérifie si une date est dans le passé.
     * 
     * @param date La date à vérifier
     * @return true si la date est passée, false sinon
     */
    public static boolean estDatePasse(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Vérifie si une date est aujourd'hui.
     * 
     * @param date La date à vérifier
     * @return true si c'est aujourd'hui, false sinon
     */
    public static boolean estAujourdhui(LocalDate date) {
        return date.isEqual(LocalDate.now());
    }
    
    /**
     * Ajoute des jours à une date.
     * 
     * @param date    La date de départ
     * @param nbJours Nombre de jours à ajouter
     * @return La nouvelle date
     */
    public static LocalDate ajouterJours(LocalDate date, int nbJours) {
        return date.plusDays(nbJours);
    }
    
    /**
     * Vérifie si une période de réservation est valide.
     * 
     * @param dateArrivee Date d'arrivée
     * @param dateDepart  Date de départ
     * @return true si la période est valide, false sinon
     */
    public static boolean estPeriodeValide(LocalDate dateArrivee, LocalDate dateDepart) {
        return !dateArrivee.isAfter(dateDepart) && 
               !dateArrivee.isBefore(LocalDate.now()) &&
               !dateArrivee.equals(dateDepart);
    }
}

