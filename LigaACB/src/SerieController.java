import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase SerieController extiende SuperController y se encarga de gestionar las operaciones relacionadas
 * con la entidad "series" en la base de datos
 */
public class SerieController extends SuperController {

    /**
     * Constructor SerieController con la conexión de la base de datos proporcionada
     * @param connection La conexión de la base de datos
     */
    public SerieController(Connection connection) {
        super(connection, "series");
    }

    /**
     * Crea la tabla 'series' en la base de datos si aún no existe
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void createSeries() throws SQLException {

        if(doesTableExist("series")) {
            System.out.println("La tabla 'series' ya existe");
        } else {
            String sql = "CREATE TABLE IF NOT EXISTS series (\n" +
                    "    idSerie SERIAL PRIMARY KEY,\n" +
                    "    nombre VARCHAR(255)\n" +
                    ");";

            PreparedStatement pst = connection.prepareStatement(sql);
            pst.executeUpdate();

            System.out.println("Tabla 'series' creada correctamente");
            pst.close();
        }
    }

    /**
     * Lee los datos de un archivo CSV y los inserta en la tabla 'series'
     */
    public void readCsv() {
        String csvFilePath = "LigaACB/resources/seriesDeElementos.csv";

        try{
            CSVReader reader = new CSVReader(new FileReader(csvFilePath));

            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                insertIntoSerie(record);
            }
            System.out.println("Tabla 'series' completada");

        } catch (IOException | CsvException e){
            e.printStackTrace();
        }
    }

    /**
     * Inserta un nuevo registro en la tabla 'series'
     * @param data Los datos a insertar
     */
    public void insertIntoSerie(String[] data) {
        String sql = "INSERT INTO series (nombre) VALUES (?)";

        try {
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, data[0]);

            pst.executeUpdate();
            pst.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
