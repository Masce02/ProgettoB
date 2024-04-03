package GUI.mainElements;

import java.awt.Cursor;
import java.awt.FlowLayout;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import GUI.GUI;
import GUI.panels.CityAddData;
import GUI.panels.CityQuery;
import GUI.panels.Home;
import GUI.panels.OperatorHome;
import GUI.panels.OperatorLogin;
import GUI.panels.OperatorRegister;
import models.CurrentOperator;

/**
 * La classe {@code MenuBar} rappresenta una barra del men&ugrave; per
 * l'interfaccia
 * grafica dell'applicazione.
 * <p>
 * La barra del men&ugrave; contiene elementi per la navigazione tra diverse
 * sezioni
 * dell'applicazione e per cambiare il tema dell'interfaccia utente.
 * </p>
 * <p>
 * I principali elementi del men&ugrave; includono Home, Area Cittadino e Area
 * Operatore con le opzioni di Login, Registrazione, Gestione della citt&agrave;
 * e
 * Loguot.
 * </p>
 * 
 * @see GUI.GUI
 * @see GUI.panels.CityAddData
 * @see GUI.panels.CityQuery
 * @see GUI.panels.Home
 * @see GUI.panels.OperatorHome
 * @see GUI.panels.OperatorLogin
 * @see GUI.panels.OperatorRegister
 * @see models.CurrentOperator
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15/09/2023
 */
public class MenuBar extends JMenuBar {

    /**
     * Crea una nuova istanza di {@code MenuBar}.
     * <p>
     * Ad ogni elemento del men&ugrave; &egrave; associato un'azione che pu&ograve;
     * essere l'andare in un
     * pannello specifico o performare il logout.
     * </p>
     * 
     * @param gui L'istanza dell'interfaccia grafica principale a cui &egrave;
     *            associata la
     *            barra del men&ugrave;.
     */
    public MenuBar(GUI gui) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JMenuItem itemHome = new JMenuItem("Home");
        JMenuItem itemCityQuery = new JMenuItem("Ricerca città");

        JMenu submenuOperator = new JMenu("Area Operatore");
        JMenuItem itemOperatorLogin = new JMenuItem("Login");
        JMenuItem itemOperatorRegistration = new JMenuItem("Registrazione");
        JMenuItem itemCityAddData = new JMenuItem("Gestisci città");
        JMenuItem itemAreaLogout = new JMenuItem("Logout");

        JCheckBoxMenuItem itemToggleTheme = new JCheckBoxMenuItem("Tema scuro");
        itemToggleTheme.setSelected(gui.appTheme.isDarkTheme());

        JMenuItem[] jMenuItems = new JMenuItem[] {
                itemHome,
                itemCityQuery,
                submenuOperator,
                itemOperatorLogin,
                itemOperatorRegistration,
                itemCityAddData,
                itemAreaLogout,
                itemToggleTheme };

        itemHome.addActionListener(e -> {
            gui.goToPanel(Home.ID, null);
        });

        itemCityQuery.addActionListener(e -> {
            gui.goToPanel(CityQuery.ID, null);
        });

        submenuOperator.addActionListener(e -> {
            gui.goToPanel(OperatorHome.ID, null);
        });

        itemOperatorLogin.addActionListener(e -> {
            gui.goToPanel(OperatorLogin.ID, null);
        });

        itemOperatorRegistration.addActionListener(e -> {
            gui.goToPanel(OperatorRegister.ID, null);
        });

        itemCityAddData.addActionListener(e -> {
            gui.clearCityAddData();
            gui.goToPanel(CityAddData.ID, null);
        });

        itemAreaLogout.addActionListener(e -> {
            Integer answer = JOptionPane.showConfirmDialog(
                    this,
                    "Sei sicuro di voler uscire dall'area operatore?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                CurrentOperator.getInstance().performLogout();
                JOptionPane.showMessageDialog(this, "Verrai reindirizzato alla Home");
                gui.goToPanel(Home.ID, null);
            }
        });

        itemToggleTheme.addActionListener(e -> {
            gui.appTheme.toggleTheme();
        });

        add(itemHome);
        add(itemCityQuery);

        add(submenuOperator);
        submenuOperator.add(itemOperatorLogin);
        submenuOperator.add(itemOperatorRegistration);
        submenuOperator.add(itemCityAddData);
        submenuOperator.add(itemAreaLogout);
        add(itemToggleTheme);

        for (JMenuItem jMenuItem : jMenuItems) {
            jMenuItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
}