import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

/**
 * Clase ElementoController extiende SuperController y gestiona las operaciones relacionadas
 * con la entidad "elementos" en la base de datos
 */
public class ElementoController extends SuperController {

    /** Scanner utilizado para la entrada del usuario */
    private  Scanner scanner;

    /**
     * Constructor ElementoController con la conexión de la base de datos proporcionada
     * @param connection La conexión de la base de datos
     */
    public ElementoController(Connection connection) {
        super(connection, "elementos");
        scanner = new Scanner(System.in);
    }

    /**
     * Crea la tabla 'elementos' en la base de datos si aún no existe y las tablas
     * 'series' y 'estados' están presentes
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void createElementos() throws SQLException {

        String nombreT1 = "series";
        String nombreT2 = "estados";

        if(doesTableExist("elementos")) {
            System.out.println("La tabla 'elementos' ya existe");
        } else if (!doesTableExist(nombreT1) || !doesTableExist(nombreT2)) {
            System.out.println("No es posible crear la tabla, verifique que las tablas 'series' y 'estados' existan");
        } else {
            String sql = "CREATE TABLE IF NOT EXISTS elementos (\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    numero INT,\n" +
                    "    nombre VARCHAR(255),\n" +
                    "    simbolo VARCHAR(255),\n" +
                    "    peso DECIMAL(12,6),\n" +
                    "    idSerie SERIAL,\n" +
                    "    idEstado SERIAL,\n" +
                    "    energia VARCHAR(255),\n" +
                    "    EN DECIMAL(12,6),\n" +
                    "    fusion DECIMAL(12,6),\n" +
                    "    ebullicion DECIMAL(12,6),\n" +
                    "    EA DECIMAL(12,6),\n" +
                    "    ionizacion DECIMAL(12,6),\n" +
                    "    radio INT,\n" +
                    "    dureza DECIMAL(12,6),\n" +
                    "    modulo DECIMAL(12,6),\n" +
                    "    densidad DECIMAL(12,6),\n" +
                    "    Cond DECIMAL(12,6),\n" +
                    "    calor DECIMAL(12,6),\n" +
                    "    abundancia DECIMAL(12,6),\n" +
                    "    Dto INT,\n" +
                    "    FOREIGN KEY (idSerie) REFERENCES series(idSerie),\n" +
                    "    FOREIGN KEY (idEstado) REFERENCES estados(idEstado)\n" +
                    ");";

            PreparedStatement pst = connection.prepareStatement(sql);
            pst.executeUpdate();

            System.out.println("Tabla 'elementos' creada correctamente");
            pst.close();

        }
    }

    /**
     * Lee los datos desde un archivo CSV y los inserta en la tabla 'elementos' si las relaciones
     * con las tabals 'series' y 'estados' son válidas
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void readCsv() throws SQLException {
        String csvFilePath = "LigaACB/resources/elementosQuimicos.csv";

        try{
            CSVReader reader = new CSVReader(new FileReader(csvFilePath));

            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                if (isValidRecord(record)){
                    insertIntoElementos(record);
                } else {
                    System.out.println("Relación no existe: " + String.join(", ", record));
                }
            }

            System.out.println("Tabla 'elementos' completada");

        } catch (IOException | CsvException e){
            e.printStackTrace();
        }
    }

    /**
     * Verifica si la relación entre los elementos del registro y las tablas 'series' y 'estados' es válida
     * @param record Los datos del registro
     * @return true si la relación es válida, false de lo contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    private boolean isValidRecord(String[] record) throws SQLException {

        int idSerie = Integer.parseInt(record[4]);
        int idEstado = Integer.parseInt(record[5]);

        return doesSerieExist(idSerie) && doesEstadoExist(idEstado);
    }

    /**
     * Verifica si un estado con el ID dado existe en la tabla 'estados¡
     * @param idEstado El ID del estado a verificar
     * @return true si el estado existe, false de lo contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    private boolean doesEstadoExist(int idEstado) throws SQLException {
        String sql = "SELECT COUNT(*) FROM estados WHERE idEstado = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, idEstado);

            try (ResultSet resultSet = pst.executeQuery()) {
                // Mover al primer resultado (posiblemente único)
                resultSet.next();

                // Obtener el valor del recuento
                int rowCount = resultSet.getInt(1);

                // Si el recuento es mayor que 0, el estado existe
                return rowCount > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica si una serie con el ID dato existe en la tabla 'series'
     * @param idSerie El ID de la serie a verificar
     * @return true si la serie existe, false de lo contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    private boolean doesSerieExist(int idSerie) throws SQLException {
        String sql = "SELECT COUNT(*) FROM series WHERE idSerie = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, idSerie);

            try (ResultSet resultSet = pst.executeQuery()) {
                // Mover al primer resultado (posiblemente único)
                resultSet.next();

                // Obtener el valor del recuento
                int rowCount = resultSet.getInt(1);

                // Si el recuento es mayor que 0, el estado existe
                return rowCount > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Insertar un nuevo registro en la tabla 'elementos'
     * @param data Los datos a insertar
     */
    public void insertIntoElementos(String[] data) {
        String sql = "INSERT INTO elementos (numero, nombre, simbolo, peso, idSerie, idEstado, energia, EN, fusion, ebullicion, EA, ionizacion, radio, dureza, modulo, densidad, Cond, calor, abundancia, Dto) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try{
            PreparedStatement pst = connection.prepareStatement(sql);

            int[] decimalesIndices = {4,8,9,10,11,12,14,15,16,17,18,19};
            int[] enterosIndices = {1,13, 20};
            int [] textosIndices = {2,3,7};


            Object[][] indicesInfo = {
                    {decimalesIndices, Types.DECIMAL},
                    {enterosIndices, Types.INTEGER},
                    {textosIndices, Types.VARCHAR}
            };

            for (Object[] info : indicesInfo) {
                int[] indices = (int[]) info[0];
                int tipoDato = (int) info[1];

                for (int indice : indices) {
                    if (!data[indice - 1].isEmpty()) {
                        if (tipoDato == Types.DECIMAL) {
                            String valorDecimalString = data[indice - 1];
                            BigDecimal valorDecimal = new BigDecimal(valorDecimalString);
                            pst.setBigDecimal(indice, valorDecimal);
                        } else if (tipoDato == Types.INTEGER) {
                            pst.setInt(indice, Integer.parseInt(data[indice - 1]));
                        } else if (tipoDato == Types.VARCHAR) {
                            pst.setString(indice, data[indice - 1]);
                        }
                    } else {
                        pst.setNull(indice, tipoDato);
                    }
                }
            }

            if (!data[4].isEmpty()){
                pst.setInt(5, Integer.parseInt(data[4]));
            } else {
                pst.setInt(5, 10);
            }

            if (!data[5].isEmpty()){
                pst.setInt(6, Integer.parseInt(data[5]));
            } else {
                pst.setInt(6, 5);
            }

            pst.executeUpdate();

            pst.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Muestra los elementos de la tabla 'elementos' cuyo nombre contiene el texto proporcionado
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void selectText() throws SQLException {
        System.out.print("Ingresa el texto: ");
        String texto = scanner.nextLine();

        String sql = "SELECT * FROM elementos WHERE nombre LIKE ?";

        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, "%" + texto + "%");

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

    /**
     * Muestra los elementos de la tabla 'elementos' cuyo año de descubrimiento (Dto) está en el rango proporcionado
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void selectYear() throws SQLException {

        System.out.print("Ingrese el año inicial: ");
        int any1 = scanner.nextInt();

        System.out.print("Ingrese el año final: ");
        int any2 = scanner.nextInt();

        String sql = "SELECT * FROM elementos WHERE Dto BETWEEN ? AND ?";

        try {
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setInt(1, any1);
            pst.setInt(2, any2);

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

    /**
     * Muestra los elementos de la tabla 'elementos' cuyo símbolo es el proporcionado
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void selectSimbol() throws SQLException{

        //SELECT * FROM elementos WHERE simbolo = 'H';

        System.out.print("Indica el simbolo del elemeno: ");
        String simbolo = scanner.next();


        String sql = "SELECT * FROM elementos WHERE simbolo = ?";

        try {
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, simbolo);

            try (ResultSet resultSet = pst.executeQuery()) { //Obtiene los nombres de los campos
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

    /**
     * Modifica el ID de la serie de los elementos cuyo año de descubrimiento (Dto) es mayor que el proporcionado
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void modifyIdSerie() throws SQLException {
        showTable();

        System.out.println("Indica a partir de que año en adelante se hará la modificación: ");
        int any = scanner.nextInt();

        System.out.print("Introduce el nuevo idSerie [1 - 10]: ");
        int nuevoIdSerie = scanner.nextInt();

        String sql = "UPDATE elementos SET idSerie = ? WHERE dto > ?";

        try {
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setInt(1, nuevoIdSerie);
            pst.setInt(2, any);

            int filasAfectadas = pst.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Actualización exitosa");
                selectIdSerieModify(any);
            } else {
                System.out.println("La actualización no afectó ninguna fila");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Muestra los elementos cuyo año de descubrimiento (Dto) es mayor que el proporcionado,
     * después de realizar una modificación
     * @param any El año a partir del cual se realizó la modificación
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void selectIdSerieModify(int any) throws SQLException {
        String sql = "SELECT * FROM elementos WHERE dto > ?";

        try {
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setInt(1, any);

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

    /**
     * Elimina un elemento de la tabla 'elementos' según el ID proporcionado
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void deleteElementoId() throws SQLException {
        showTable();
        System.out.print("Introduce el id del elemento que quieres eliminar: ");
        int idElemento = scanner.nextInt();

        String sql = "DELETE FROM elementos WHERE id = ?";

        try{
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setInt(1, idElemento);

            int filasAfectadas = pst.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Eliminación exitosa");
                showTable();
            } else {
                System.out.println("La eliminación no afectó ninguna fila");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Elimina elementos de la tabla 'elementos' según el IDEstado proporcionado
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void deleteElementoIdEstado() throws SQLException {
        showTable();

        System.out.print("Introduce el idEstado de los elementos a eliminar: ");
        int idEstado = scanner.nextInt();

        String sql = "DELETE FROM elementos WHERE idEstado = ?";

        try {
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setInt(1, idEstado);

            int filasAfectadas = pst.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Eliminación exitosa");
                showTable();
            } else {
                System.out.println("La eliminación no afectó ninguna fila");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
