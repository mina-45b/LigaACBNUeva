import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Clase TablaMenu representa un meú interactivo para gestionar tablas relacionadas con elementos químicos
 * Proporciona opciones para crear, borar, completar, ver, seleccionar, modificar y eliminar tablas
 */
public class TablaMenu {
	private int option; //Almacena la opción seleccionada por el usuario

	/**
	 * Constructor por defecto de la clase TablaMenu
	 */
	public TablaMenu() {
		super();
	}

	/**
	 * Método que muestra el menú principal y permite al usuario seleccionar una opción
	 * @return La opción seleccionada por el usuario
	 */
	public int mainMenu() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		do {
			//Menú principal con diferentes opciones
			System.out.println("\n====== MENU PRINCIPAL ======\n");

			System.out.println("1.  Crear todas las tablas");
			System.out.println("2.  Borrar todas las tablas\n");

			System.out.println("Completar tablas:");
			System.out.println("3.  Completar tabla estados");
			System.out.println("4.  Completar tabla series");
			System.out.println("5.  Completar tabla elementos\n");

			System.out.println("Ver tablas: ");
			System.out.println("6.  Muestra elementos químicos");
			System.out.println("7.  Muestra series");
			System.out.println("8.  Muestra estados\n");

			System.out.println("Select tablas:");
			System.out.println("9.  Seleccionar elementos por texto");
			System.out.println("10. Seleccionar elementos por año");
			System.out.println("11. Seleccionar elemento por símbolo\n");

			System.out.println("Modificar tablas:");
			System.out.println("12. Modificar nombre de los estados");
			System.out.println("13. Modificar idSerie de los elementos a partir de un año\n");

			System.out.println("Eliminar tablas:");
			System.out.println("14. Eliminar elementos por ID");
			System.out.println("15. Eliminar elementos por idEstado\n");

			System.out.println("16. Salir");

			System.out.println("Esculli opció: ");
			try {
				option = Integer.parseInt(br.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.println("valor no vàlid");
				e.printStackTrace();

			}

		} while (option != 1 && option != 2 && option != 3 && option != 4 && option != 5 && option != 6 && option != 7
				&& option != 8 && option != 9 && option != 10 && option != 11 && option != 12 && option != 13 && option != 14 && option != 15 && option != 16);

		return option;
	}
}
