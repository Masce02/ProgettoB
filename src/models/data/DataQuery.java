package models.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import models.record.RecordCity;
import models.record.RecordOperator;
import models.record.RecordCenter;
import models.record.RecordWeather;

/**
 * La classe {@code DataQuery} fornisce metodi per interrogare e recuperare dati
 * dai record
 * delle citt&agrave;, degli operatori, dei centri e dei dati meteorologici presenti
 * nella
 * classe {@code DataStorage}.
 * <p>
 * Questa classe offre una serie di metodi per filtrare i dati
 * in base a condizioni specifiche e recuperare i record corrispondenti.
 * </p>
 * 
 * @see models.record.RecordCity
 * @see models.record.RecordOperator
 * @see models.record.RecordCenter
 * @see models.record.RecordWeather
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class DataQuery {

    private DataStorage dataStorage;

    /**
     * Crea una nuova istanza di DataQuery.
     *
     * @param dataStorage L'oggetto DataStorage che contiene i dati su cui
     *                    effettuare le interrogazioni.
     */
    public DataQuery(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Ottiene una citt&agrave; basata sull'ID specificato.
     *
     * @param ID L'ID della citt&agrave; da cercare.
     * @return Il record della citt&agrave; corrispondente all'ID specificato.
     * @throws IllegalArgumentException Se sono presenti pi&ugrave; citt&agrave; con lo
     *                                  stesso ID.
     */
    public RecordCity getCityBy(Integer ID) {
        RecordCity[] matchingCity = getCityBy(new QueryCondition("ID", ID));
        if (matchingCity.length > 1)
            throw new IllegalArgumentException("Sono presenti molteplici città con lo stesso ID");

        return matchingCity[0];
    }

    /**
     * Ottiene un array di citt&agrave; basato su una singola condizione di interrogazione.
     *
     * @param condition La condizione di interrogazione per filtrare le citt&agrave;.
     * @return Un array di RecordCity corrispondente alla condizione specificata.
     */
    public RecordCity[] getCityBy(QueryCondition condition) {
        ArrayList<QueryCondition> conditions = new ArrayList<>();
        conditions.add(condition);
        return getCityBy(conditions);
    }

    /**
     * Ottiene un array di citt&agrave; basato su una lista di condizioni di interrogazione.
     *
     * @param conditions La lista di condizioni di interrogazione per filtrare le
     *                   citt&agrave;.
     * @return Un array di RecordCity che soddisfano tutte le condizioni
     *         specificate.
     */
    public RecordCity[] getCityBy(List<QueryCondition> conditions) {
        List<RecordCity> matchingCity = filterData(dataStorage.cityMap.values(),
                conditions,
                this::checkCityCondition);
        return matchingCity.toArray(new RecordCity[0]);
    }

    /**
     * Ottiene un operatore basato sull'ID specificato.
     *
     * @param ID L'ID dell'operatore da cercare.
     * @return Il record dell'operatore corrispondente all'ID specificato.
     * @throws IllegalArgumentException Se sono presenti pi&ugrave; operatori con lo
     *                                  stesso
     *                                  ID.
     */
    public RecordOperator getOperatorBy(Integer ID) {
        RecordOperator[] matchingOperator = getOperatorBy(new QueryCondition("ID", ID));
        if (matchingOperator.length > 1)
            throw new IllegalArgumentException("Sono presenti molteplici operatori con lo stesso ID");

        return matchingOperator[0];
    }

    /**
     * Ottiene un array di operatori basato su una singola condizione di
     * interrogazione.
     *
     * @param condition La condizione di interrogazione per filtrare gli operatori.
     * @return Un array di RecordOperator corrispondente alla condizione
     *         specificata.
     */
    public RecordOperator[] getOperatorBy(QueryCondition condition) {
        ArrayList<QueryCondition> conditions = new ArrayList<>();
        conditions.add(condition);
        return getOperatorBy(conditions);
    }

    /**
     * Ottiene un array di operatori basato su una lista di condizioni di
     * interrogazione.
     *
     * @param conditions La lista di condizioni di interrogazione per filtrare gli
     *                   operatori.
     * @return Un array di RecordOperator che soddisfano tutte le condizioni
     *         specificate.
     */
    public RecordOperator[] getOperatorBy(List<QueryCondition> conditions) {
        List<RecordOperator> matchingOperator = filterData(dataStorage.operatorMap.values(),
                conditions,
                this::checkOperatorCondition);
        return matchingOperator.toArray(new RecordOperator[0]);
    }
    /**
     * Ottiene un array di centri.
     * @return Un array di RecordCenter che contiene tutti i centri.
     */
    public RecordCenter[] getCenters() {
        return dataStorage.centerMap.values().toArray(new RecordCenter[0]);
    }

    /**
     * Ottiene un centro basato sull'ID specificato.
     *
     * @param ID L'ID del centro da cercare.
     * @return Il record del centro corrispondente all'ID specificato.
     * @throws IllegalArgumentException Se sono presenti pi&ugrave; centri con lo
     *                                  stesso
     *                                  ID.
     */
    public RecordCenter getCenterBy(Integer ID) {
        RecordCenter[] matchingCenter = getCenterBy(new QueryCondition("ID", ID));
        if (matchingCenter.length > 1)
            throw new IllegalArgumentException("Sono presenti molteplici centri con lo stesso ID");

        return matchingCenter[0];
    }

    /**
     * Ottiene un array di centri basato su una singola condizione di
     * interrogazione.
     *
     * @param condition La condizione di interrogazione per filtrare i centri.
     * @return Un array di RecordCenter corrispondente alla condizione specificata.
     */
    public RecordCenter[] getCenterBy(QueryCondition condition) {
        ArrayList<QueryCondition> conditions = new ArrayList<>();
        conditions.add(condition);
        return getCenterBy(conditions);
    }

    /**
     * Ottiene un array di centri basato su una lista di condizioni di
     * interrogazione.
     *
     * @param conditions La lista di condizioni di interrogazione per filtrare i
     *                   centri.
     * @return Un array di RecordCenter che soddisfano tutte le condizioni
     *         specificate.
     */
    public RecordCenter[] getCenterBy(List<QueryCondition> conditions) {
        List<RecordCenter> matchingCenter = filterData(dataStorage.centerMap.values(),
                conditions,
                this::checkCenterCondition);
        return matchingCenter.toArray(new RecordCenter[0]);
    }

    /**
     * Ottiene un record meteorologico basato sull'ID specificato.
     *
     * @param ID L'ID del record meteorologico da cercare.
     * @return Il record meteorologico corrispondente all'ID specificato.
     * @throws IllegalArgumentException Se sono presenti pi&ugrave; record
     *                                  meteorologici
     *                                  con lo stesso ID.
     */
    public RecordWeather getWeatherBy(Integer ID) {
        RecordWeather[] matchingWeather = getWeatherBy(new QueryCondition("ID", ID));
        if (matchingWeather.length > 1)
            throw new IllegalArgumentException("Sono presenti molteplici record meteorologici con lo stesso ID");

        return matchingWeather[0];
    }

    /**
     * Ottiene un array di record meteorologici basato su una singola condizione di
     * interrogazione.
     *
     * @param condition La condizione di interrogazione per filtrare i record
     *                  meteorologici.
     * @return Un array di RecordWeather corrispondente alla condizione specificata.
     */
    public RecordWeather[] getWeatherBy(QueryCondition condition) {
        ArrayList<QueryCondition> conditions = new ArrayList<>();
        conditions.add(condition);
        return getWeatherBy(conditions);
    }

    /**
     * Ottiene un array di record meteorologici basato su una lista di condizioni di
     * interrogazione.
     *
     * @param conditions La lista di condizioni di interrogazione per filtrare i
     *                   record meteorologici.
     * @return Un array di RecordWeather che soddisfano tutte le condizioni
     *         specificate.
     */
    public RecordWeather[] getWeatherBy(List<QueryCondition> conditions) {
        List<RecordWeather> matchingWeather = filterData(dataStorage.weatherMap.values(),
                conditions,
                this::checkWeatherCondition);
        return matchingWeather.toArray(new RecordWeather[0]);
    }

    /**
     * Filtra una collezione di dati in base a una lista di condizioni specificate e
     * restituisce
     * una lista di elementi che soddisfano tutte le condizioni.
     *
     * @param data             La collezione di dati da filtrare.
     * @param conditions       La lista di condizioni per il filtro.
     * @param conditionChecker Una funzione che verifica se un elemento soddisfa una
     *                         condizione specifica.
     * @param <T>              Il tipo degli elementi nella collezione di dati.
     * @return Una lista di elementi che soddisfano tutte le condizioni specificate.
     */
    private <T> List<T> filterData(Collection<T> data,
            List<QueryCondition> conditions,
            BiFunction<T, QueryCondition, Boolean> conditionChecker) {
        List<T> matchingData = new ArrayList<>();

        for (T item : data) {

            boolean matches = false;

            for (QueryCondition condition : conditions) {
                if (condition.hasMultipleValues()) {
                    Object value = condition.getValue();
                    if (!(value instanceof List<?>)) {
                        throw new IllegalArgumentException("Valore non valido");
                    }
                    List<?> values = (List<?>) value;

                    for (Object val : values) {
                        if (conditionChecker.apply(item, new QueryCondition(condition.getKey(), val))) {
                            matches = true;
                            break;
                        }
                    }
                } else if (conditionChecker.apply(item, condition)) {
                    matches = true;
                } else {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                matchingData.add(item);
            }

        }

        return matchingData;
    }

    /**
     * Verifica se la condizione specificata &egrave; soddisfatta per il record
     * della citt&agrave;
     * dato.
     *
     * @param city      La citt&agrave; da verificare.
     * @param condition La condizione di interrogazione da verificare.
     * @return {@code True} se la condizione &egrave; soddisfatta, altrimenti
     *         {@code False}.
     * @throws IllegalArgumentException Se la chiave specificata nella condizione
     *                                  &egrave;
     *                                  invalida.
     */
    private boolean checkCityCondition(RecordCity city, QueryCondition condition) {

        String key = condition.getKey();
        Object value = condition.getValue();

        if (key.equals("ID")) {
            Integer targetID = (Integer) value;
            return city.ID().equals(targetID);

        } else if (key.equals("name")) {
            String targetName = (String) value;
            return city.name().equalsIgnoreCase(targetName);

        } else if (key.equals("ASCIIName")) {
            String targetASCIIName = (String) value;
            return city.ASCIIName().equalsIgnoreCase(targetASCIIName);

        } else if (key.equals("countryCode")) {
            String targetCountryCode = (String) value;
            return city.countryCode().equals(targetCountryCode);

        } else if (key.equals("countryName")) {
            String targetCountryName = (String) value;
            return city.countryName().equalsIgnoreCase(targetCountryName);

        } else if (key.equals("latitude")) {
            double targetLatitude = (double) value;
            return Math.abs(city.latitude() - targetLatitude) < generateEpsilon(targetLatitude);

        } else if (key.equals("longitude")) {
            double targetLongitude = (double) value;
            return Math.abs(city.longitude() - targetLongitude) < generateEpsilon(targetLongitude);
        }

        throw new IllegalArgumentException("Chiave non valida");

    }

    /**
     * Verifica se la condizione specificata &egrave; soddisfatta per il record
     * dell'operatore dato.
     *
     * @param operator  L'operatore da verificare.
     * @param condition La condizione di interrogazione da verificare.
     * @return {@code True} se la condizione &egrave; soddisfatta, altrimenti
     *         {@code False}.
     * @throws IllegalArgumentException Se la chiave specificata nella condizione
     *                                  &egrave;
     *                                  invalida.
     */
    private boolean checkOperatorCondition(RecordOperator operator, QueryCondition condition) {

        String key = condition.getKey();
        Object value = condition.getValue();

        if (key.equals("ID")) {
            Integer targetID = (Integer) value;
            return operator.ID().equals(targetID);

        } else if (key.equals("nameSurname")) {
            String targetNameSurname = (String) value;
            return operator.nameSurname().equals(targetNameSurname);

        } else if (key.equals("taxCode")) {
            String targetTaxCode = (String) value;
            return operator.taxCode().equals(targetTaxCode);

        } else if (key.equals("email")) {
            String targetEmail = (String) value;
            return operator.email().equals(targetEmail);

        } else if (key.equals("username")) {
            String targetUsername = (String) value;
            return operator.username().equals(targetUsername);

        } else if (key.equals("password")) {
            String targetPassword = (String) value;
            return operator.password().equals(targetPassword);

        } else if (key.equals("centerID")) {
            Integer targetCenterID = (Integer) value;
            return operator.centerID().equals(targetCenterID);
        }

        throw new IllegalArgumentException("Chiave non valida");

    }

    /**
     * Verifica se la condizione specificata &egrave; soddisfatta per il record del
     * centro
     * dato.
     *
     * @param center    Il centro da verificare.
     * @param condition La condizione di interrogazione da verificare.
     * @return {@code True} se la condizione &egrave; soddisfatta, altrimenti
     *         {@code False}.
     * @throws IllegalArgumentException Se la chiave specificata nella condizione
     *                                  &egrave;
     *                                  invalida.
     */
    private boolean checkCenterCondition(RecordCenter center, QueryCondition condition) {

        String key = condition.getKey();
        Object value = condition.getValue();

        if (key.equals("ID")) {
            Integer targetID = (Integer) value;
            return center.ID().equals(targetID);

        } else if (key.equals("centerName")) {
            String targetCenterName = (String) value;
            return center.centerName().equals(targetCenterName);

        } else if (key.equals("streetName")) {
            String targetStreetName = (String) value;
            return center.streetName().equals(targetStreetName);

        } else if (key.equals("streetNumber")) {
            String targetStreetNumber = (String) value;
            return center.streetNumber().equals(targetStreetNumber);

        } else if (key.equals("CAP")) {
            String targetCAP = (String) value;
            return center.CAP().equals(targetCAP);

        } else if (key.equals("townName")) {
            String targetTownName = (String) value;
            return center.townName().equals(targetTownName);

        } else if (key.equals("districtName")) {
            String targetDistrictName = (String) value;
            return center.districtName().equals(targetDistrictName);

        } else if (key.equals("cityID")) {
            Integer targetCityID = (Integer) value;
            Integer[] centerCityIDs = center.cityIDs();

            for (int i = 0; i < centerCityIDs.length; i++) {
                if (targetCityID.equals(centerCityIDs[i])) {
                    return true;
                }
            }
        }

        throw new IllegalArgumentException("Chiave non valida");

    }

    /**
     * Verifica se la condizione specificata &egrave; soddisfatta per il record
     * meteorologico dato.
     *
     * @param weather   Il record meteorologico da verificare.
     * @param condition La condizione di interrogazione da verificare.
     * @return {@code True} se la condizione &egrave; soddisfatta, altrimenti
     *         {@code False}.
     * @throws IllegalArgumentException Se la chiave specificata nella condizione
     *                                  &egrave;
     *                                  invalida.
     */
    private boolean checkWeatherCondition(RecordWeather weather, QueryCondition condition) {

        String key = condition.getKey();
        Object value = condition.getValue();

        if (key.equals("ID")) {
            Integer targetID = (Integer) value;
            return weather.ID().equals(targetID);

        } else if (key.equals("centerID")) {
            Integer targetCenterID = (Integer) value;
            return weather.centerID().equals(targetCenterID);

        } else if (key.equals("cityID")) {
            Integer targetCityID = (Integer) value;
            return weather.cityID().equals(targetCityID);

        } else if (key.equals("date")) {
            String targetDate = (String) value;
            return weather.date().equals(targetDate);
        }

        throw new IllegalArgumentException("Chiave non valida");
    }

    /**
     * Calcola un valore di epsilon basato sul numero di posizioni decimali del
     * valore specificato.
     *
     * @param value Il valore per il quale calcolare l'epsilon.
     * @return Il valore di epsilon calcolato.
     */
    public static double generateEpsilon(double value) {
        int decimalPlaces = computeDecimalPlaces(value);

        // Calculate epsilon as 1 / (10^decimalPlaces) for appropriate precision
        double epsilon = 1.0 / Math.pow(10, decimalPlaces);

        return epsilon;
    }

    /**
     * Calcola il numero di posizioni decimali in un valore double.
     *
     * @param value Il valore double per il quale calcolare le posizioni decimali.
     * @return Il numero di posizioni decimali nel valore specificato.
     */
    public static int computeDecimalPlaces(double value) {
        String text = Double.toString(Math.abs(value));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = 0;

        if (integerPlaces > 0) {
            decimalPlaces = text.length() - integerPlaces - 1;
        }

        return decimalPlaces;
    }

    /**
     * La classe interna {@code QueryCondition} rappresenta una condizione di query
     * per la ricerca di dati.
     */
    public static class QueryCondition {
        private String key;
        private Object value;

        /**
         * Crea una nuova istanza di {@code QueryCondition} con la chiave e il valore
         * specificati.
         *
         * @param key   La chiave della condizione.
         * @param value Il valore associato alla condizione.
         */
        public QueryCondition(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Ottiene la chiave della condizione.
         *
         * @return La chiave della condizione.
         */
        public String getKey() {
            return key;
        }

        /**
         * Ottiene il valore associato alla condizione.
         *
         * @return Il valore della condizione.
         */
        public Object getValue() {
            return value;
        }

        /**
         * Verifica se la condizione ha valori multipli.
         *
         * @return {@code True} se la condizione ha valori multipli, altrimenti
         *         {@code False}.
         */
        public boolean hasMultipleValues() {
            return value instanceof List;
        }
    }

}

