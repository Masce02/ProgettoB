package GUI.panels;

import javax.swing.*;

import GUI.GUI;
import GUI.Widget;
import GUI.layouts.TwoRows;
import models.MainModel;
import utils.Interfaces;

/**
 * La classe {@code Home} rappresenta il pannello principale dell'applicazione
 * visualizzato dopo il caricamento iniziale.
 * <p>
 * Questo pannello fornisce all'utente due opzioni principali: "Cerca e
 * visualizza dati" e "Gestisci area operatore".
 * </p>
 * <p>
 * L'utente pu&ograve; selezionare una delle opzioni per avviare
 * funzionalit&agrave;
 * specifiche dell'applicazione.
 * </p>
 * 
 * @see GUI.GUI
 * @see GUI.Widget
 * @see GUI.layouts.TwoRows
 * @see models.MainModel
 * @see utils.Interfaces
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 17/09/2023
 */
public class Home extends TwoRows implements Interfaces.UIPanel {

    /**
     * L'ID univoco di questo pannello. Viene utilizzato per identificare e navigare
     * tra i diversi pannelli dell'applicazione.
     */
    public static String ID = "Home";

    /**
     * Riferimento all'interfaccia utente grafica (GUI) associata alla barra del
     * menu.
     */
    private GUI gui;

    /**
     * Pulsante per navigare alla funzionalit&agrave; di ricerca e visualizzazione
     * dei
     * dati.
     */
    private JButton buttonToFind = new Widget.Button("Cerca e visualizza dati");

    /**
     * Pulsante per navigare alla funzionalit&agrave; di gestione dell'area
     * riservata agli
     * operatori.
     */
    private JButton buttonToOperator = new Widget.Button("Gestisci area operatore");

    /**
     * Crea una nuova istanza di {@code Home}.
     *
     * @param mainModel Il modello principale dell'applicazione.
     */
    public Home(MainModel mainModel) {
    }

    /**
     * Aggiunge gli eventi di azione ai pulsanti dell'interfaccia utente.
     */
    private void addActionEvent() {
        buttonToFind.addActionListener(e -> {
            gui.goToPanel(CityQuery.ID, null);
        });

        buttonToOperator.addActionListener(e -> {
            gui.goToPanel(OperatorHome.ID, null);
        });
    }

    @Override
    public Home createPanel(GUI gui) {
        this.gui = gui;

        addTop(new Widget.LogoLabel());
        addBottom(buttonToFind);
        addBottom(buttonToOperator);

        gui.appTheme.registerPanel(topPanel);
        gui.appTheme.registerPanel(bottomPanel);

        addActionEvent();

        return this;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void onOpen(Object[] args) {
    }

}
