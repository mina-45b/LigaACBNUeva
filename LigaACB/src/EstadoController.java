
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase EstadoController extiende SuperController y se encarga de gestionar las operaciones relacionadas
 * con la entidad "estados" en la base de datos
 */
public class EstadoController extends SuperController {

    /** Scanner utilizado para la entrada del usuario */
    private Scanner scanner;

    /**
     * Constructor EstadoController con la conexión de la base de datos proporcionada
     * @param connection La conexión de la base de datos
     */
    public EstadoController(Connection connection) {
        super(connection, "estados");
        scanner = new Scanner(System.in);
    }

    /**
     * Crea la tabla 'estados' en la base de datos si aún no existe
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void createEstados() throws SQLException {

        if(doesTableExist("estados")) {
            System.out.println("La tabla 'estados' ya existe");
        } else {
            String sql = "CREATE TABLE IF NOT EXISTS estados (\n" +
                    "    idEstado SERIAL PRIMARY KEY,\n" +
                    "    nombre VARCHAR(255)\n" +
                    ");";

            PreparedStatement pst = connection.prepareStatement(sql);
            pst.executeUpdate();

            System.out.println("Tabla 'estados' creada correctamente");
            pst.close();
        }
    }

    /**
     * Lee los datos desde un archivo CSV y los inserta en la tabla 'estados
     */
    public void readCsv() {
        String csvFilePath = "LigaACB/resources/estadosDeElementos.csv";

        try {
            CSVReader reader = new CSVReader(new FileReader(csvFilePath));

            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                insertIntoEstados(record);
            }
            System.out.println("Tabla 'estados' completada");

        } catch (IOException | CsvException e){
            e.printStackTrace();
        }

    }

    /**
     * Inserta un nuevo registro en la tabla 'estados'
     * @param data Los datos a insertar
     */
    public void insertIntoEstados(String[] data){
        String sql = "INSERT INTO estados (nombre) VALUES (?)";

        try {
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, data[0]);

            pst.executeUpdate();
            pst.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Modifica el nombre de un estado en la tabla 'estados'
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void modifyEstadoName() throws SQLException {
        showTable();

        System.out.println("Indica el 'id' del estado que quieres modificar");
        int idEstado = scanner.nextInt();

        scanner.nextLine();
        System.out.print("Introduce el nuevo nombre del estado: ");
        String nuevoEstado = scanner.nextLine();


        String sql = "UPDATE estados SET nombre = ? WHERE idEstado = ?";

        try {
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, nuevoEstado);
            pst.setInt(2, idEstado);

            int filasAfectadas = pst.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Actualización exitosa");
                selectEstadoNameModify(idEstado);
            } else {
                System.out.println("La actualización no afectó ninguna fila");
            }

        } catch (SQLException e) {
           throw  new RuntimeException(e);
        }
    }

    /**
     * Muestra los detalles de un estado específico después de modificar su nombre
     * @param idEstado El ID del estado
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void selectEstadoNameModify(int idEstado) throws SQLException {
        String sql = "SELECT * FROM estados WHERE idEstado = ?";

        try {
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setInt(1, idEstado);

            try (ResultSet resultSet = pst.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    System.out.printf("%-20s | ", columnName);
                }
                System.out.println();

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        Object columnValue = resultSet.getObject(i);
                        System.out.printf("%-20s | ", columnValue);
                    }
                    System.out.println();
                }
            }

        } catch (SQLException e) {
        e.printStackTrace();
        }
    }

}
