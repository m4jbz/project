import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Font;

public class Main {
	public static void main(String[] args) {
	UIManager.put("OptionPane.messageFont", new Font(Font.SANS_SERIF, Font.BOLD, 13));
  UIManager.put("OptionPane.messageForeground", Color.lightGray);
	UIManager.put("OptionPane.background", Color.darkGray);
	UIManager.put("Button.foreground", Color.lightGray);
	UIManager.put("Button.background", Color.darkGray);
	UIManager.put("Panel.background", Color.darkGray);

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
				JOptionPane.showMessageDialog(null, "¡Adios!");
				break;
			default:
				System.out.println("Opción no válida. Intente de nuevo.");
		}
	} while (!opcion.equals("5"));

	sc.close();
	}
}
