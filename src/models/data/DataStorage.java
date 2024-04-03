package models.data;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import utils.Constants;
import models.file.FileHandler;
import models.record.RecordCenter;
import models.record.RecordCity;
import models.record.RecordOperator;
import models.record.RecordWeather;
import models.record.RecordWeather.WeatherData;

/**
 * La classe {@code DataStorage} gestisce l'archiviazione dei dati
 * dell'applicazione.
 * <p>
 * Contiene mappe che consentono l'accesso ai dati relativi alle citt&agrave;,
 * agli
 * operatori, alle citt&agrave; e alle condizioni meteo.
 * </p>
 * <p>
 * Inoltre, questa classe carica i dati iniziali dai file di dati quando viene
 * istanziata.
 * </p>
 * 
 * @see utils.Constants
 * @see models.file.FileHandler
 * @see models.record.RecordCenter
 * @see models.record.RecordCity
 * @see models.record.RecordOperator
 * @see models.record.RecordWeather
 * @see models.record.RecordWeather.WeatherData
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class DataStorage {

    /**
     * Mappa che archivia i dati delle citt&agrave; in base all'ID.
     */
    public HashMap<Integer, RecordCity> cityMap;

    /**
     * Mappa che archivia i dati degli operatori in base all'ID.
     */
    public HashMap<Integer, RecordOperator> operatorMap;

    /**
     * Mappa che archivia i dati dei centri in base all'ID.
     */
    public HashMap<Integer, RecordCenter> centerMap;

    /**
     * Mappa che archivia i dati delle condizioni meteo in base all'ID.
     */
    public HashMap<Integer, RecordWeather> weatherMap;

    /**
     * Costruttore della classe {@code DataStorage}.
     * Inizializza le mappe dei dati e carica i dati iniziali dai file di dati.
     */
    public DataStorage() {

        cityMap = createCityMap();
        operatorMap = createOperatorMap();
        centerMap = createCenterMap();
        weatherMap = createWeatherMap();

    }

    /**
     * Restituisce un oggetto {@code RecordCity} in base all'ID specificato.
     * 
     * @param geonameID L'ID della citt&agrave; da cercare.
     * @return Un oggetto {@code RecordCity} corrispondente all'ID specificato, o
     *         null se non trovato.
     */
    public RecordCity getCityByID(Integer geonameID) {
        return cityMap.get(geonameID);
    }

    /**
     * Restituisce un oggetto {@code RecordOperator} in base all'ID specificato.
     * 
     * @param operatorID L'ID dell'operatore da cercare.
     * @return Un oggetto {@code RecordOperator} corrispondente all'ID specificato,
     *         o null se non trovato.
     */
    public RecordOperator getOperatorByID(Integer operatorID) {
        return operatorMap.get(operatorID);
    }

    /**
     * Restituisce un oggetto {@code RecordCenter} in base all'ID specificato.
     * 
     * @param centerID L'ID del centro da cercare.
     * @return Un oggetto {@code RecordCenter} corrispondente all'ID specificato, o
     *         null se non trovato.
     */
    public RecordCenter getCenterByID(Integer centerID) {
        return centerMap.get(centerID);
    }

    /**
     * Restituisce un oggetto {@code RecordWeather} in base all'ID specificato.
     * 
     * @param weatherID L'ID di dati climatici da cercare.
     * @return Un oggetto {@code RecordWeather} corrispondente all'ID specificato, o
     *         null se non trovato.
     */
    public RecordWeather getWeatherByID(Integer weatherID) {
        return weatherMap.get(weatherID);
    }

    /**
     * Crea una mappa dei dati delle citt&agrave; a partire dai dati presenti nel file di
     * dati delle citt&agrave;.
     * 
     * @return Una mappa dei dati delle citt&agrave;, dove la chiave &egrave; l'ID della
     *         citt&agrave; e
     *         il valore &egrave; l'oggetto {@code RecordCity}.
     * @throws IOException Se si verifica un errore durante la lettura del file di
     *                     dati delle citt&agrave;.
     */
    private HashMap<Integer, RecordCity> createCityMap() {
        HashMap<Integer, RecordCity> map = new HashMap<>();

        try {
            List<String> lines = FileHandler.readFile(Constants.Path.Files.CITY);

            RecordCity[] cityCoords = new RecordCity[lines.size() - 1];

            for (int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i).split(Constants.CSV_SEPARATOR);

                if (line.length == 7) {
                    cityCoords[i - 1] = new RecordCity(
                            Integer.parseInt(line[0]),
                            line[1],
                            line[2],
                            line[3],
                            line[4],
                            Double.parseDouble(line[5].replace(",", ".")),
                            Double.parseDouble(line[6].replace(",", ".")));
                } else if (line.length == 6) {
                    cityCoords[i - 1] = new RecordCity(
                            Integer.parseInt(line[0]),
                            line[1],
                            line[2],
                            line[3],
                            "unset",
                            Double.parseDouble(line[4].replace(",", ".")),
                            Double.parseDouble(line[5].replace(",", ".")));
                } else
                    System.out.println("Errore: " + line[0]);
            }

            for (RecordCity cityCoord : cityCoords) {
                map.put(cityCoord.ID(), cityCoord);
            }

            return map;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Errore nella lettura dei dati.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    /**
     * Crea una mappa dei dati degli operatori a partire dai dati presenti nel file
     * di dati degli operatori.
     * 
     * @return Una mappa dei dati degli operatori, dove la chiave &egrave; l'ID
     *         dell'operatore e il valore &egrave; l'oggetto {@code RecordOperator}.
     * @throws IOException Se si verifica un errore durante la lettura del file di
     *                     dati degli operatori.
     */
    private HashMap<Integer, RecordOperator> createOperatorMap() {
        HashMap<Integer, RecordOperator> map = new HashMap<>();

        try {
            List<String> lines = FileHandler.readFile(Constants.Path.Files.OPERATOR);

            RecordOperator[] operatorDatas = new RecordOperator[lines.size() - 1];

            for (int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i).split(Constants.CSV_SEPARATOR);

                operatorDatas[i - 1] = new RecordOperator(
                        Integer.parseInt(line[0]),
                        line[1],
                        line[2],
                        line[3],
                        line[4],
                        line[5],
                        line[6].equals(Constants.EMPTY_STRING) ? null : Integer.parseInt(line[6]));
            }

            for (RecordOperator operatorData : operatorDatas) {
                map.put(operatorData.ID(), operatorData);
            }

            return map;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Errore nella lettura dei dati.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Crea una mappa dei dati dei centri a partire dai dati presenti nel file di
     * dati dei centri.
     * 
     * @return Una mappa dei dati dei centri, dove la chiave &egrave; l'ID del
     *         centro e il
     *         valore &egrave; l'oggetto {@code RecordCenter}.
     * @throws IOException Se si verifica un errore durante la lettura del file di
     *                     dati dei centri.
     */
    private HashMap<Integer, RecordCenter> createCenterMap() {
        HashMap<Integer, RecordCenter> map = new HashMap<>();

        try {
            List<String> lines = FileHandler.readFile(Constants.Path.Files.CENTER);

            RecordCenter[] centerDatas = new RecordCenter[lines.size() - 1];

            for (int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i).split(Constants.CSV_SEPARATOR);

                centerDatas[i - 1] = new RecordCenter(
                        Integer.parseInt(line[0]),
                        line[1],
                        line[2],
                        line[3],
                        line[4],
                        line[5],
                        line[6],
                        Arrays.stream(line[7].split(Pattern.quote(Constants.CSV_SUB_SEPARATOR))).map(Integer::parseInt)
                                .toArray(Integer[]::new));
            }

            for (RecordCenter centerData : centerDatas) {
                map.put(centerData.ID(), centerData);
            }

            return map;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Errore nella lettura dei dati.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Crea una mappa dei dati delle condizioni meteo a partire dai dati presenti
     * nel file di dati delle condizioni meteo.
     * 
     * @return Una mappa dei dati delle condizioni meteo, dove la chiave &egrave;
     *         l'ID
     *         delle condizioni meteo e il valore &egrave; l'oggetto
     *         {@code RecordWeather}.
     * @throws IOException Se si verifica un errore durante la lettura del file di
     *                     dati delle condizioni meteo.
     */
    private HashMap<Integer, RecordWeather> createWeatherMap() {
        HashMap<Integer, RecordWeather> map = new HashMap<>();

        try {
            List<String> lines = FileHandler.readFile(Constants.Path.Files.WEATHER);

            RecordWeather[] weatherDatas = new RecordWeather[lines.size() - 1];

            for (int i = 1; i < lines.size(); i++) {
                String[] line = lines.get(i).split(Constants.CSV_SEPARATOR);

                List<WeatherData> weatherDataList = new ArrayList<>();

                for (int j = 4; j < line.length; j++) {
                    String[] data = line[j].split(Pattern.quote(Constants.CSV_SUB_SEPARATOR));
                    weatherDataList.add(new RecordWeather.WeatherData(
                            data[0].equals(Constants.EMPTY_STRING) ? null : Integer.parseInt(data[0]),
                            data[1].equals(Constants.EMPTY_STRING) ? null : data[1]));
                }

                weatherDatas[i - 1] = new RecordWeather(
                        Integer.parseInt(line[0]),
                        Integer.parseInt(line[1]),
                        Integer.parseInt(line[2]),
                        line[3],
                        weatherDataList.get(0),
                        weatherDataList.get(1),
                        weatherDataList.get(2),
                        weatherDataList.get(3),
                        weatherDataList.get(4),
                        weatherDataList.get(5),
                        weatherDataList.get(6));
            }

            for (RecordWeather weatherData : weatherDatas) {
                map.put(weatherData.ID(), weatherData);
            }

            return map;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Errore nella lettura dei dati.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

}
