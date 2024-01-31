import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Clase TablaMain contiene el método principal (main) que inicia la aplicación y gestiona el flujo del programa
 * Se encarga de interactuar con el usuario a través de un menú y realizar operaciones en las tablas relacionadas con elementos químicos
 */
public class TablaMain {

	/**
	 * Método principal que inicia la aplicación y gestiona las operaciones en las tablas de la base de datos
	 * @param args Argumentos de línea de comandos (no se utilizan en este caso)
	 * @throws IOException Excepción de E/S en caso de error durante la lectura de entrada
	 * @throws SQLException Excepción de SQL en caso de error en la conexión con la base de datos
	 * @throws ParseException Excepción de análisis de cadena en caso de error al analizar datos.
	 */
	public static void main(String[] args) throws IOException, SQLException, ParseException {
		// Se crea una instancia de TablaMenu para gestionar el menú
		TablaMenu menu = new TablaMenu();

		// Se obtiene una conexión a la base de datos mediante la clase ConnectionFactory
		ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
		Connection c = connectionFactory.connect();

		// Se crean controladores para las entidades relacionadas con las tablas
		ElementoController elementoController = new ElementoController(c);
		SerieController serieController = new SerieController(c);
		EstadoController estadoController = new EstadoController(c);

		// Se muestra el menú principal y obtiene la opción seleccionada por el usuario
		int option = menu.mainMenu();

		// Se ejecuta un bucle mientas la opción seleccionada esté dentro del rango válida
		while (option > 0 && option < 16) {
			// Se utiliza una instrucción switch para realizar la operación correspondiente según la opción seleccionada
			switch (option) {
			case 1:
				serieController.createSeries();
				estadoController.createEstados();
				elementoController.createElementos();
				break;

			case 2:
				elementoController.deleteTable();
				estadoController.deleteTable();
				serieController.deleteTable();
				break;

			case 3:
				estadoController.readCsv();
				break;

			case 4:
				serieController.readCsv();
				break;

			case 5:
				elementoController.readCsv();
				break;

			case 6:
				elementoController.showTable();
				break;

			case 7:
				serieController.showTable();
				break;

			case 8:
				estadoController.showTable();
				break;

			case 9:
				elementoController.selectText();
				break;

			case 10:
				elementoController.selectYear();
				break;

			case 11:
				elementoController.selectSimbol();
				break;

			case 12:
				estadoController.modifyEstadoName();
				break;
			case 13:
				elementoController.modifyIdSerie();
				break;
			case 14:
				elementoController.deleteElementoId();
				break;
			case 15:
				elementoController.deleteElementoIdEstado();
				break;
			case 16:
				System.exit(0);

			default:
				System.out.println("Introduce una de las opciones anteriores");
				break;

			}
			// Se muestra nuevamente el menú principal y se actualiza la opción seleccionada.
			option = menu.mainMenu();
		}

	}

}
