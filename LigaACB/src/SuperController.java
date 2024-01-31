import java.sql.*;

/**
 * Clase SuperController proporciona métodos de uso común para otros controladores
 */
public class SuperController {
    /** Conexión a la base de datos */
    protected Connection connection;

    /** Nombre de la tabla asociada al controlador */
    protected String tableName;

    /**
     * Constructor de SuperController con la conexión a la base de datos y el nombre de la tabla específica
     * @param connection La conexión de la base de datos
     * @param tableName El nombre de la tabla
     */
    public SuperController(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Elimina la tabla asociada de la base de datos
     */
    public void deleteTable() {
        String sql = "DROP TABLE " + tableName;

        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.executeUpdate();

            System.out.println("Tabla '" + tableName +"' eliminada correctamente");

        } catch (SQLException e) {
            System.out.println("No se pudo eliminar la tabla, verifica que exista o su relación con otras tablas");
        }

    }

    /**
     * Comprueba si existe una tabla con el nombre especificado en la base de datos
     * @param tableName El nombre de la tabla a comprobar
     * @return True si la tabla existe, false en caso contrario
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public boolean doesTableExist(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);

        return resultSet.next();
    }

    /**
     * Recupera y muestra todos los registros de la tabla asociada
     * @throws SQLException Si ocurre un error en la base de datos
     */
    public void showTable() throws SQLException {
        String sql = "SELECT * FROM " + tableName;

        try( PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet resultSet = pst.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnNumber = metaData.getColumnCount();

            for (int i = 1; i <= columnNumber; i++) {
                System.out.printf("%-22s",metaData.getColumnName(i));
            }
            System.out.println();

            //Muestra datos de la tabla
            while (resultSet.next()) {
                for (int i = 1; i <= columnNumber; i++) {
                    int columnType = metaData.getColumnType(i);

                   if (columnType == Types.DECIMAL || columnType == Types.NUMERIC) {
                        System.out.printf("%-22.4f", resultSet.getBigDecimal(i));
                    } else if (columnType == Types.INTEGER) {
                        System.out.printf("%-22d", resultSet.getInt(i));
                    } else {
                        System.out.printf("%-22s", resultSet.getString(i));
                    }
                }
                System.out.println();
            }

            resultSet.close();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
