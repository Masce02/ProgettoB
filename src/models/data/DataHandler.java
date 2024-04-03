package models.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import models.file.FileHandler;
import models.record.RecordCenter;
import models.record.RecordCity;
import models.record.RecordOperator;
import models.record.RecordWeather;
import utils.Constants;

/**
 * La classe {@code DataHandler} &egrave; responsabile della gestione dei dati
 * nel
 * sistema.
 * <p>
 * Fornisce metodi per aggiungere nuovi record, generare chiavi primarie uniche,
 * aggiornare record esistenti e recuperare dati in base a determinate
 * condizioni.
 * </p>
 * <p>
 * Utilizza un'istanza condivisa di DataStorage per accedere ai dati
 * sottostanti.
 * </p>
 * 
 * @see models.file.FileHandler
 * @see models.record.RecordCenter
 * @see models.record.RecordCity
 * @see models.record.RecordOperator
 * @see models.record.RecordWeather
 * @see utils.Constants
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class DataHandler extends DataQuery {

    /**
     * Crea un nuovo gestore dei dati inizializzando l'istanza condivisa di
     * {@code DataStorage}.
     */
    private static DataStorage dataStorage = new DataStorage();

    /**
     * Crea un nuovo gestore dei dati inizializzando l'istanza condivisa di
     * {@code DataStorage}.
     * 
     * Questo costruttore crea un'istanza di {@code DataHandler} che user&agrave;
     * l'istanza
     * condivisa di {@code DataStorage} per gestire i dati.
     */
    public DataHandler() {
        super(dataStorage);
    }

    /**
     * Genera una chiave primaria unica per una mappa di dati.
     *
     * @param map La mappa di dati di cui generare la chiave primaria.
     * @return Una chiave primaria unica basata sugli ID esistenti nella mappa.
     */
    public Integer generatePrimaryKey(HashMap<Integer, ?> map) {
        int highestKey = 0;

        for (int key : map.keySet()) {
            if (key > highestKey) {
                highestKey = key;
            }
        }

        return highestKey + 1;
    }

    /**
     * Aggiunge un nuovo record operatore al sistema.
     *
     * @param nameSurname Il nome e cognome dell'operatore.
     * @param taxCode     Il codice fiscale dell'operatore.
     * @param email       L'indirizzo email dell'operatore.
     * @param username    Il nome utente dell'operatore.
     * @param password    La password dell'operatore.
     * @param centerID    L'ID del centro associato all'operatore.
     * @return Il record operatore appena aggiunto.
     * @throws IllegalArgumentException Se esiste gi&agrave; un operatore con lo
     *                                  stesso
     *                                  nome utente e password.
     */
    public RecordOperator addNewRecord(String nameSurname,
            String taxCode,
            String email,
            String username,
            String password,
            Integer centerID) {

        RecordOperator operator = new RecordOperator(
                generatePrimaryKey(dataStorage.operatorMap),
                nameSurname,
                taxCode,
                email,
                username,
                password,
                centerID);

        List<QueryCondition> conditions = new ArrayList<>();
        conditions.add(new QueryCondition("username", username));
        conditions.add(new QueryCondition("password", password));

        RecordOperator[] result = getOperatorBy(conditions);
        if (result.length > 0) {
            throw new IllegalArgumentException("L'utente esiste già");
        }

        try {
            FileHandler.appendToFile(Constants.Path.Files.OPERATOR, operator.toString());
            dataStorage.operatorMap.put(operator.ID(), operator);
            return operator;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Errore nella scrittura dei dati.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    /**
     * Aggiunge un nuovo record centro al sistema.
     *
     * @param centerName   Il nome del centro.
     * @param streetName   Il nome della via/piazza.
     * @param streetNumber Il numero civico.
     * @param CAP          Il CAP.
     * @param townName     Il nome del comune.
     * @param districtName Il nome della provincia.
     * @param cityIDs      Gli ID delle citt&agrave; associate al centro.
     * @return Il record centro appena aggiunto.
     * @throws IllegalArgumentException Se esiste gi&agrave; un centro con gli
     *                                  stessi
     *                                  dettagli.
     */
    public RecordCenter addNewRecord(String centerName,
            String streetName,
            String streetNumber,
            String CAP,
            String townName,
            String districtName,
            Integer[] cityIDs) {

        RecordCenter center = new RecordCenter(
                generatePrimaryKey(dataStorage.centerMap),
                centerName,
                streetName,
                streetNumber,
                CAP,
                townName,
                districtName,
                cityIDs);

        List<QueryCondition> conditions = new ArrayList<>();

        conditions.add(new QueryCondition("centerName", centerName));
        conditions.add(new QueryCondition("streetName", streetName));
        conditions.add(new QueryCondition("streetNumber", streetNumber));
        conditions.add(new QueryCondition("CAP", CAP));
        conditions.add(new QueryCondition("townName", townName));
        conditions.add(new QueryCondition("districtName", districtName));
        conditions.add(new QueryCondition("cityID", cityIDs));

        RecordCenter[] result = getCenterBy(conditions);
        if (result.length > 0) {
            throw new IllegalArgumentException("Il centro esiste già");
        }

        try {
            FileHandler.appendToFile(Constants.Path.Files.CENTER, center.toString());
            dataStorage.centerMap.put(center.ID(), center);
            return center;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Errore nella scrittura dei dati.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    /**
     * Aggiunge un nuovo record meteorologico al sistema.
     *
     * @param cityID           L'ID della citt&agrave; associata al record
     *                         meteorologico.
     * @param centerID         L'ID del centro associata al record meteorologico.
     * @param date             La data di rilevamento del record meteorologico.
     * @param wind             Dati sul vento.
     * @param humidity         Dati sull'umidit&agrave;.
     * @param pressure         Dati sulla pressione atmosferica.
     * @param temperature      Dati sulla temperatura.
     * @param precipitation    Dati sulla precipitazione.
     * @param glacierElevation Dati sull'elevazione dei ghiacciai.
     * @param glacierMass      Dati sulla massa dei ghiacciai.
     * @return Il record meteorologico appena aggiunto.
     */
    public RecordWeather addNewRecord(
            Integer cityID,
            Integer centerID,
            String date,
            RecordWeather.WeatherData wind,
            RecordWeather.WeatherData humidity,
            RecordWeather.WeatherData pressure,
            RecordWeather.WeatherData temperature,
            RecordWeather.WeatherData precipitation,
            RecordWeather.WeatherData glacierElevation,
            RecordWeather.WeatherData glacierMass) {

        RecordWeather newWeather = new RecordWeather(
                generatePrimaryKey(dataStorage.weatherMap),
                cityID,
                centerID,
                date,
                wind,
                humidity,
                pressure,
                temperature,
                precipitation,
                glacierElevation,
                glacierMass);

        try {
            FileHandler.appendToFile(Constants.Path.Files.WEATHER, newWeather.toString());
            dataStorage.weatherMap.put(newWeather.ID(), newWeather);
            return newWeather;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Errore nella scrittura dei dati!",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    /**
     * Aggiorna un record citt&agrave; nel sistema.
     *
     * @param city Il record citt&agrave; da aggiornare.
     */
    public void updateRecord(RecordCity city) {
        updateRecord(Constants.Path.Files.CITY, city.ID(), city);
        dataStorage.cityMap.put(city.ID(), city);
    }

    /**
     * Aggiorna un record operatore nel sistema.
     *
     * @param operator Il record operatore da aggiornare.
     */
    public void updateRecord(RecordOperator operator) {
        updateRecord(Constants.Path.Files.OPERATOR, operator.ID(), operator);
        dataStorage.operatorMap.put(operator.ID(), operator);
    }

    /**
     * Aggiorna un record centro nel sistema.
     *
     * @param center Il record centro da aggiornare.
     */
    public void updateRecord(RecordCenter center) {
        updateRecord(Constants.Path.Files.CENTER, center.ID(), center);
        dataStorage.centerMap.put(center.ID(), center);
    }

    /**
     * Aggiorna un record meteorologico nel sistema.
     *
     * @param weather Il record meteorologico da aggiornare.
     */
    public void updateRecord(RecordWeather weather) {
        updateRecord(Constants.Path.Files.WEATHER, weather.ID(), weather);
        dataStorage.weatherMap.put(weather.ID(), weather);
    }

    /**
     * Aggiorna un record esistente nel sistema.
     *
     * @param filePath Il percorso del file in cui sono archiviati i dati del
     *                 record.
     * @param ID       L'ID del record da aggiornare.
     * @param object   L'oggetto Record da utilizzare per l'aggiornamento.
     */
    private void updateRecord(String filePath, int ID, Object object) {
        try {
            List<String> lines = FileHandler.readFile(filePath);

            for (int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i).split(Constants.CSV_SEPARATOR);

                if (Integer.parseInt(line[0]) == ID) {
                    lines.set(i, object.toString());
                    break;
                }
            }

            FileHandler.writeFile(filePath, lines);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Errore nella scrittura dei dati.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
