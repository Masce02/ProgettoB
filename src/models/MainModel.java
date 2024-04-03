package models;

import models.data.DataHandler;
import models.data.DataStorage;
import models.file.FileHandler;
import models.logic.*;

/**
 * La classe {@code MainModel} rappresenta il modello principale
 * dell'applicazione.
 * <p>
 * Questa classe gestisce l'accesso ai dati, la logica pertinente agli
 * operatori, ai centri di monitoraggio
 * e alle citt&agrave; e la gestione dei file.
 * </p>
 * <p>
 * E' un componente centrale nell'architettura MVC dell'applicazione.
 * </p>
 * 
 * @see models.data.DataHandler
 * @see models.data.DataStorage
 * @see models.file.FileHandler
 * @see models.logic.LogicOperator
 * @see models.logic.LogicCenter
 * @see models.logic.LogicCity
 * 
 * @author Andrea Tettamanti
 * @author Luca Mascetti
 * @version 1.0
 * @since 16/09/2023
 */
public class MainModel {

    /**
     * Gestisce le operazioni di lettura e scrittura dei file nell'applicazione.
     */
    public FileHandler file;

    /**
     * Archivia i dati utilizzati nell'applicazione.
     */
    public DataStorage dataStorage;

    /**
     * Gestisce l'accesso e la manipolazione dei dati nell'applicazione.
     */
    public DataHandler data;

    /**
     * Gestisce la logica specifica dell'operatore.
     */
    public LogicOperator logicOperator;

    /**
     * Gestisce la logica specifica del centro di monitoraggio.
     */
    public LogicCenter logicCenter;

    /**
     * Gestisce la logica specifica della citt&agrave;.
     */
    public LogicCity logicCity;

    /**
     * Costruttore della classe {@code MainModel}.
     * <p>
     * Inizializza le varie componenti del modello, inclusi i gestori dei file, dei
     * dati
     * e le istanze delle classi di logica.
     * </p>
     */
    public MainModel() {
        file = new FileHandler();
        data = new DataHandler();
        logicOperator = new LogicOperator(data);
        logicCenter = new LogicCenter(data);
        logicCity = new LogicCity(data);
    }
}
