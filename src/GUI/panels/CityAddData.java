package GUI.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.MaskFormatter;

import GUI.GUI;
import GUI.Widget;
import GUI.Widget.ComboItem;
import models.CurrentOperator;
import models.MainModel;
import models.record.RecordCenter;
import models.record.RecordCity;
import utils.Constants;
import utils.Constants.Legend;
import utils.Functions;
import utils.Interfaces;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.EventObject;

/**
 * La classe {@code CityAddData} rappresenta un pannello per l'aggiunta di dati
 * di una citt&agrave; da parte dell'operatore.
 * <p>
 * Il pannello consente all'operatore di inserire i dati relativi alla
 * citt&agrave;
 * selezionata quali:
 * data di rilevamento dati, punteggi per le varie categorie ed eventualmente
 * dei commenti che li descrivono.
 * </p>
 * <p>
 * La classe gestisce la validazione della data inserita, il limite di caratteri
 * per i commenti dei dati e la funzionalit&agrave; per il salvataggio dei dati
 * inseriti.
 * </p>
 * 
 * @see GUI.GUI
 * @see GUI.Widget
 * @see GUI.Widget.ComboItem
 * @see models.CurrentOperator
 * @see models.MainModel
 * @see models.record.RecordCenter
 * @see models.record.RecordCity
 * @see utils.Constants
 * @see utils.Functions
 * @see utils.Interfaces
 *
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 15-09-2023
 */
public class CityAddData extends JPanel implements Interfaces.UIPanel {

    /**
     * L'identificatore unico per questo pannello.
     */
    public static String ID = "CityAddData";

    /**
     * Riferimento all'interfaccia utente grafica (GUI) associata alla barra del
     * menu.
     */
    private GUI gui;

    /**
     * Riferimento al modello principale associato a questo pannello.
     */
    private MainModel mainModel;

    /**
     * Il pattern per la data.
     */
    private static String datePattern = "dd/MM/yyyy";

    /**
     * La maschera per per la data.
     */
    private static String dateMask = datePattern.replaceAll("[dMy]", "#");

    /** Campo di testo per il nome del Centro */
    private JTextField textfieldCenterName = new JTextField();

    /**
     * ComboBox per la selezione della citt&agrave;.
     */
    private JComboBox<ComboItem> comboboxCityName = new JComboBox<>();

    /**
     * Campo di testo formattato per la data di rilevamento.
     */
    private JFormattedTextField textfieldDate = new JFormattedTextField();

    /**
     * Tabella per l'inserimento dati.
     */
    private JTable table = new JTable();

    /**
     * Modello predefinito per la tabella.
     */
    private DefaultTableModel defaulmodelTable = new DefaultTableModel();

    /**
     * Bottone per il salvataggio dei dati.
     */
    private JButton buttonPerformSave = new Widget.Button("Salva il nuovo set di dati");

    /**
     * Lunghezza massima consentita per i commenti.
     */
    private final int maxCommentLength = 256;

    /**
     * Matrice di dati predefiniti per la tabella.
     */
    private static String[][] data = {
            { "Vento", "Velocità del vento (km/h)" },
            { "Umidita'", "% di Umidità;" },
            { "Pressione", "In hPa" },
            { "Temperatura", "In C°" },
            { "Precipitazioni", "In mm di pioggia" },
            { "Altitudine dei ghiacciai", "In m" },
            { "Massa dei ghiacciai", "In kg" }
    };

    /**
     * Crea una nuova istanza di {@code CityAddData}.
     * 
     * @param mainModel Il modello principale dell'applicazione.
     */
    public CityAddData(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    /**
     * Cancella i dati nella tabella.
     */
    public void clearTableData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            model.setValueAt("", i, 1);
            model.setValueAt("", i, 2);
        }
    }

    /**
     * Aggiunge azioni agli elementi dell'interfaccia.
     */
    private void addActionEvent() {

        textfieldDate.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textfieldDate.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                String dateString = textfieldDate.getText();

                if (dateString.equals(dateMask.replace("#", " "))) {
                    textfieldDate.setText(Functions.getCurrentDateString());
                    textfieldDate.setForeground(Color.GRAY);
                    return;
                }

                if (!Functions.isDateValid(dateString)) {
                    JOptionPane.showMessageDialog(null,
                            "La data inserita non &egrave; corretta.",
                            "Data errata",
                            JOptionPane.ERROR_MESSAGE);
                    textfieldDate.setForeground(Color.RED);
                } else {
                    textfieldDate.setForeground(Color.BLACK);
                }
            }
        });

        buttonPerformSave.addActionListener(e -> {

            Integer cityID = ((ComboItem) comboboxCityName.getSelectedItem()).getValue();
            String date = textfieldDate.getText();

            Object[][] tableData = new Object[defaulmodelTable.getRowCount()][defaulmodelTable.getColumnCount() - 1];

            for (int i = 0; i < defaulmodelTable.getRowCount(); i++) {
                String scoreCell = defaulmodelTable.getValueAt(i, 1) != null
                        ? defaulmodelTable.getValueAt(i, 1).toString()
                        : "";
                String commentCell = Functions.charsetString(defaulmodelTable.getValueAt(i, 2).toString());

                // Verifica lunghezza commento
                if (commentCell.length() > maxCommentLength) {

                    JOptionPane.showMessageDialog(this,
                            "Il commento supera il limite di " + maxCommentLength + " caratteri.",
                            "Limite di caratteri superato", JOptionPane.WARNING_MESSAGE);
                    return;// non salvo i dati
                }

                tableData[i] = new Object[] {
                        !scoreCell.isEmpty() ? Integer.parseInt(scoreCell) : null,
                        !commentCell.isEmpty() ? commentCell.trim() : Constants.EMPTY_STRING
                };
            }

            try {
                mainModel.logicCenter.addDataToCenter(
                        cityID,
                        date,
                        tableData);
                JOptionPane.showMessageDialog(this,
                        "Dati salvati con successo!",
                        "Salvataggio dati",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception exception) {

                JOptionPane.showMessageDialog(this,
                        "Errore nella scrittura dei dati!",
                        "Errore nel salvataggio dati",
                        JOptionPane.ERROR_MESSAGE);
            }

            this.clearTableData();
        });

    }

    /**
     * La classe interna {@code IntegerCellEditor} &egrave; un editor per celle con
     * valori
     * interi.
     */
    public static class IntegerCellEditor extends DefaultCellEditor {

        private static final JComboBox<String> comboBox = new JComboBox<>(new String[] { "", "1", "2", "3", "4", "5" });

        /**
         * Costruisce un nuovo {@code IntegerCellEditor}.
         * <p>
         * Inizializza l'editor di celle con un JComboBox contenente valori interi.
         * </p>
         */
        public IntegerCellEditor() {
            super(comboBox);
        }

        /**
         * Restituisce il valore intero selezionato dal JComboBox.
         * <p>
         * Se il valore selezionato &egrave; un intero valido, lo converte e lo
         * restituisce.
         * Se il valore selezionato non pu&ograve; essere convertito in un intero,
         * restituisce
         * null.
         * </p>
         *
         * @return Il valore intero selezionato, o null se il valore selezionato non
         *         &egrave;
         *         un intero valido.
         */
        @Override
        public Integer getCellEditorValue() {
            try {
                String value = comboBox.getSelectedItem().toString();
                return Integer.parseInt(value);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * La classe interna {@code TooltipCellRender} &egrave; un rendere per celle con
     * tooltip.
     */
    static class TooltipCellRenderer extends JTextArea implements TableCellRenderer {

        public TooltipCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            setText(value != null ? value.toString() : "");
            setToolTipText(value != null ? value.toString() : "");

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }

    @Override
    public CityAddData createPanel(GUI gui) {
        this.gui = gui;

        try {
            MaskFormatter maskFormatter = new MaskFormatter(dateMask);
            maskFormatter.install(textfieldDate);
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Formato data non valido. Assicurati di inserire la data nel formato corretto.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }

        textfieldCenterName.setEnabled(false);
        textfieldCenterName.setEditable(false);

        textfieldDate.setText(Functions.getCurrentDateString());
        textfieldDate.setForeground(Color.GRAY);

        defaulmodelTable.addColumn("Categoria");
        defaulmodelTable.addColumn("Punteggio");
        defaulmodelTable.addColumn("Commento");

        for (String[] rowInitData : data) {
            defaulmodelTable.addRow(new Object[] { rowInitData[0], "", "" });
        }

        table.setModel(defaulmodelTable);
        table.getColumnModel().getColumn(1).setCellEditor(new IntegerCellEditor());
        table.getColumnModel().getColumn(2).setCellRenderer(new TooltipCellRenderer());

        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);

        table.getColumnModel().getColumn(0).setCellEditor(new NonEditableCellEditor());

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(40);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();

                if (col == 0 && row >= 0 && row < Legend.LEGENDS.length) {
                    String legendMessage = Legend.LEGENDS[row];
                    JOptionPane.showMessageDialog(null, legendMessage, "Legenda", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        });

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(new Widget.FormPanel(gui.appTheme, "Centro operatore", textfieldCenterName));
        topPanel.add(new Widget.FormPanel(gui.appTheme, "Città selezionata", comboboxCityName));
        topPanel.add(new Widget.FormPanel(gui.appTheme, "Data rilevazione", textfieldDate));

        add(topPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);
        add(buttonPerformSave, BorderLayout.SOUTH);

        gui.appTheme.registerPanel(topPanel);

        addActionEvent();

        return this;
    }

    /**
     * La classe {@code NonEditableCellEditor} &egrave; un editor per celle non
     * modificabili.
     */
    class NonEditableCellEditor extends DefaultCellEditor {

        public NonEditableCellEditor() {
            super(new JTextField());
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return false;
        }
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public void onOpen(Object[] args) {

        comboboxCityName.removeAllItems();
        CurrentOperator currentOperator = CurrentOperator.getInstance();

        if (currentOperator.isUserLogged()) {
            Integer centerID = currentOperator.getCurrentOperator().centerID();

            RecordCenter center = mainModel.data.getCenterBy(centerID);

            if (center != null) {

                Integer[] cityIDs = center.cityIDs();

                for (Integer cityID : cityIDs) {
                    RecordCity city = mainModel.data.getCityBy(cityID);
                    if (city != null) {
                        comboboxCityName.addItem(new ComboItem(city.name(), city.ID()));
                    }
                }

                textfieldCenterName.setText(center.centerName());

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Per inserire un nuovo set di dati devi prima aver creato o selezionato il tuo centro.",
                        "Centro non creato",
                        JOptionPane.ERROR_MESSAGE);
                gui.goToPanel(CenterCreateNew.ID, args);
            }

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Per inserire un nuovo set di dati devi prima essere loggato.",
                    "Utente non loggato",
                    JOptionPane.ERROR_MESSAGE);
            gui.goToPanel(OperatorHome.ID, null);
        }
    }
}
