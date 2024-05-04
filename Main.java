import java.util.Scanner;
import javax.swing.*;

public class Main {
	public static void main(String[] args) {
	GestionArchivos ga = new GestionArchivos();
	Scanner sc = new Scanner(System.in);
	String opcion = "0";

	ga.limpiarTerminal();

	do {
		opcion = JOptionPane.showInputDialog(null,
									   "Escoja una opción: \n" + 
										 "1) Ver cuenta(s)\n" + 
										 "2) Agregar cuenta(s)\n" + 
										 "3) Modificar cuenta\n" +
										 "4) Eliminar cuenta\n" +
										 "5) Salir");
		ga.limpiarTerminal();

		switch (opcion) {
			case "1":
				ga.esUsuario(ga,()->ga.mostrarCuentas(ga.listaDeCuentas()));
				break;
			case "2":
				ga.esUsuario(ga,()->ga.agregarCuentas());
				break;
			case "3":
				ga.esUsuario(ga,()->ga.modificarCuenta());
				break;
			case "4":
				ga.esUsuario(ga,()->ga.eliminarCuenta());
				break;
			case "5":
				System.out.println("¡Adiós!");
				break;
			default:
				System.out.println("Opción no válida. Intente de nuevo.");
		}
	} while (!opcion.equals("5"));

	sc.close();
	}
}
