package com.gestionhotel.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.gestionhotel.core.Hotel;
import com.gestionhotel.model.Chambre;
import com.gestionhotel.model.ChambreSimple;
import com.gestionhotel.model.ChambreDouble;
import com.gestionhotel.model.Suite;
import com.gestionhotel.utils.SQLiteDatabaseManager;

/**
 * Panneau de gestion des chambres avec interface graphique.
 * Permet d'ajouter, modifier, supprimer et rechercher des chambres.
 * 
 * @author Dev 4 (Phase 4)
 */
public class PanneauChambres extends JPanel {

    private Hotel hotel;
    private FenetrePrincipale fenetrePrincipale;
    private JTable tableChambres;
    private DefaultTableModel modelTable;
    private JTextField champNumero;
    private JComboBox<String> comboType;
    private JCheckBox checkLitsJumeaux;
    private JCheckBox checkJacuzzi;
    private JCheckBox checkBalcon;
    private JTextField champRecherche;
    private JComboBox<String> comboFiltreType;

    public PanneauChambres(Hotel hotel, FenetrePrincipale fenetrePrincipale) {
        this.hotel = hotel;
        this.fenetrePrincipale = fenetrePrincipale;
        initialiserInterface();
        actualiser();
    }

    private void initialiserInterface() {
        setLayout(new BorderLayout());

        // Panneau supérieur : Table
        String[] colonnes = {"N°", "Type", "Prix/nuit", "Capacité", "Statut"};
        modelTable = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableChambres = SwingUtils.creerTable(modelTable);
        JScrollPane scrollPane = new JScrollPane(tableChambres);

        // Panneau central : Formulaire
        JPanel panelFormulaire = creerFormulaire();

        // Panneau inférieur : Boutons et filtres
        JPanel panelBoutons = creerBoutons();
        JPanel panelFiltres = creerFiltres();

        // Assemblage
        JPanel panelGauche = new JPanel(new BorderLayout());
        panelGauche.add(panelFiltres, BorderLayout.NORTH);
        panelGauche.add(scrollPane, BorderLayout.CENTER);
        panelGauche.add(panelBoutons, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelGauche, panelFormulaire);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.6);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel creerFormulaire() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulaire Chambre"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Numéro
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Numéro :"), gbc);
        gbc.gridx = 1;
        JPanel panelNumero = new JPanel(new BorderLayout());
        champNumero = new JTextField(15);
        champNumero.setToolTipText("Laissez vide pour génération automatique (ex: " + hotel.genererPrefixeChambre() + "00001)");
        panelNumero.add(champNumero, BorderLayout.CENTER);
        JButton btnGenerer = new JButton("Auto");
        btnGenerer.setToolTipText("Générer automatiquement le prochain numéro");
        btnGenerer.addActionListener(e -> genererNumeroAutomatique());
        panelNumero.add(btnGenerer, BorderLayout.EAST);
        panel.add(panelNumero, gbc);

        // Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Type :"), gbc);
        gbc.gridx = 1;
        comboType = new JComboBox<>(new String[]{"Simple", "Double", "Suite"});
        comboType.addActionListener(e -> mettreAJourOptions());
        panel.add(comboType, gbc);

        // Options spécifiques (panneau dynamique)
        JPanel panelOptions = new JPanel(new GridBagLayout());
        GridBagConstraints gbcOptions = new GridBagConstraints();
        gbcOptions.insets = new Insets(5, 5, 5, 5);
        gbcOptions.anchor = GridBagConstraints.WEST;

        checkLitsJumeaux = new JCheckBox("Lits jumeaux");
        checkJacuzzi = new JCheckBox("Jacuzzi");
        checkBalcon = new JCheckBox("Balcon");

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(panelOptions, gbc);

        // Boutons formulaire
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel panelBoutonsForm = new JPanel(new FlowLayout());
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterChambre());
        JButton btnReinitialiser = new JButton("Réinitialiser");
        btnReinitialiser.addActionListener(e -> reinitialiserFormulaire());
        panelBoutonsForm.add(btnAjouter);
        panelBoutonsForm.add(btnReinitialiser);
        panel.add(panelBoutonsForm, gbc);

        mettreAJourOptions();

        return panel;
    }

    private void mettreAJourOptions() {
        // Cette méthode sera appelée pour mettre à jour les options selon le type
        // Pour l'instant, on garde les checkboxes visibles pour tous les types
    }

    private JPanel creerBoutons() {
        JPanel panel = new JPanel(new FlowLayout());
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerChambre());
        panel.add(btnSupprimer);
        return panel;
    }

    private JPanel creerFiltres() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Filtrer par type :"));
        comboFiltreType = new JComboBox<>(new String[]{"Tous", "Simple", "Double", "Suite"});
        comboFiltreType.addActionListener(e -> filtrer());
        panel.add(comboFiltreType);
        panel.add(new JLabel("Rechercher :"));
        champRecherche = new JTextField(15);
        champRecherche.addActionListener(e -> rechercher());
        panel.add(champRecherche);
        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.addActionListener(e -> rechercher());
        panel.add(btnRechercher);
        JButton btnToutAfficher = new JButton("Tout afficher");
        btnToutAfficher.addActionListener(e -> actualiser());
        panel.add(btnToutAfficher);
        return panel;
    }

    public void actualiser() {
        modelTable.setRowCount(0);
        for (Chambre chambre : hotel.getChambres()) {
            Object[] row = {
                hotel.formaterNumeroChambre(chambre.getNumero()),
                chambre.getType(),
                SwingUtils.formaterPrix(chambre.getPrixParNuit()),
                chambre.getCapacite() + " pers.",
                chambre.isOccupee() ? "Occupée" : "Libre"
            };
            modelTable.addRow(row);
        }
        fenetrePrincipale.mettreAJourStatusBar(hotel.getChambres().size() + " chambre(s)");
    }

    private void genererNumeroAutomatique() {
        int numero = hotel.genererProchainNumeroChambre();
        champNumero.setText(hotel.formaterNumeroChambre(numero));
    }

    private void ajouterChambre() {
        int numero;
        String numeroStr = champNumero.getText().trim();
        
        // Si le champ est vide, générer automatiquement
        if (numeroStr.isEmpty()) {
            numero = hotel.genererProchainNumeroChambre();
        } else {
            // Parser le numéro (peut être formaté avec préfixe ou juste un nombre)
            numero = hotel.parserNumeroChambre(numeroStr);
            if (numero == -1) {
                SwingUtils.afficherErreur(this, "Format de numéro invalide. Utilisez le format " + 
                    hotel.genererPrefixeChambre() + "00001 ou laissez vide pour génération automatique.", "Erreur");
                return;
            }
        }

        if (hotel.rechercherChambre(numero) != null) {
            SwingUtils.afficherErreur(this, "Une chambre avec ce numéro existe déjà.", "Erreur");
            return;
        }

        String type = (String) comboType.getSelectedItem();
        Chambre chambre = null;

        switch (type) {
            case "Simple":
                chambre = new ChambreSimple(numero);
                break;
            case "Double":
                boolean litsJumeaux = checkLitsJumeaux.isSelected();
                chambre = new ChambreDouble(numero, litsJumeaux);
                break;
            case "Suite":
                boolean jacuzzi = checkJacuzzi.isSelected();
                boolean balcon = checkBalcon.isSelected();
                chambre = new Suite(numero, jacuzzi, balcon);
                break;
        }

        if (chambre != null) {
            hotel.ajouterChambre(chambre);
            
            // Sauvegarder automatiquement dans SQLite
            SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
            dbManager.sauvegarderHotel(hotel);
            
            actualiser();
            reinitialiserFormulaire();
            SwingUtils.afficherSucces(this, "Chambre " + hotel.formaterNumeroChambre(numero) + 
                " ajoutée avec succès.", "Succès");
        }
    }

    private void supprimerChambre() {
        int row = tableChambres.getSelectedRow();
        if (row == -1) {
            SwingUtils.afficherErreur(this, "Veuillez sélectionner une chambre à supprimer.", "Erreur");
            return;
        }

        String numeroFormate = (String) modelTable.getValueAt(row, 0);
        int numero = hotel.parserNumeroChambre(numeroFormate);
        if (numero == -1) {
            SwingUtils.afficherErreur(this, "Erreur lors de la lecture du numéro de chambre.", "Erreur");
            return;
        }
        Chambre chambre = hotel.rechercherChambre(numero);

        if (chambre != null) {
            if (chambre.isOccupee()) {
                SwingUtils.afficherErreur(this, "Impossible de supprimer une chambre occupée.", "Erreur");
                return;
            }

            boolean confirmer = SwingUtils.demanderConfirmation(
                this,
                "Êtes-vous sûr de vouloir supprimer la chambre " + hotel.formaterNumeroChambre(numero) + " ?",
                "Confirmation de suppression"
            );

            if (confirmer) {
                hotel.getChambres().remove(chambre);
                
                // Sauvegarder automatiquement dans SQLite
                SQLiteDatabaseManager dbManager = SQLiteDatabaseManager.getInstance();
                dbManager.sauvegarderHotel(hotel);
                
                actualiser();
                SwingUtils.afficherSucces(this, "Chambre supprimée avec succès.", "Succès");
            }
        }
    }

    private void rechercher() {
        String recherche = champRecherche.getText().trim().toLowerCase();
        String filtreType = (String) comboFiltreType.getSelectedItem();

        modelTable.setRowCount(0);
        int count = 0;
        for (Chambre chambre : hotel.getChambres()) {
            boolean matchType = filtreType.equals("Tous") || chambre.getType().equals(filtreType);
            String numeroFormate = hotel.formaterNumeroChambre(chambre.getNumero());
            boolean matchRecherche = recherche.isEmpty() || 
                numeroFormate.toLowerCase().contains(recherche) ||
                String.valueOf(chambre.getNumero()).contains(recherche) ||
                chambre.getType().toLowerCase().contains(recherche);

            if (matchType && matchRecherche) {
                Object[] row = {
                    numeroFormate,
                    chambre.getType(),
                    SwingUtils.formaterPrix(chambre.getPrixParNuit()),
                    chambre.getCapacite() + " pers.",
                    chambre.isOccupee() ? "Occupée" : "Libre"
                };
                modelTable.addRow(row);
                count++;
            }
        }
        fenetrePrincipale.mettreAJourStatusBar(count + " chambre(s) trouvée(s)");
    }

    private void filtrer() {
        rechercher();
    }

    private void reinitialiserFormulaire() {
        champNumero.setText("");
        comboType.setSelectedIndex(0);
        checkLitsJumeaux.setSelected(false);
        checkJacuzzi.setSelected(false);
        checkBalcon.setSelected(false);
        tableChambres.clearSelection();
    }
}

