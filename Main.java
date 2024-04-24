import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
	{
		GestionArchivos ga = new GestionArchivos();
		Scanner sc = new Scanner(System.in);
		byte opcion = 0;

		ga.limpiarTerminal();

		do {
			System.out.print("Escoja una opción: \n" + 
					"1) Ver cuenta(s)\n" + 
					"2) Agregar cuenta(s)\n" + 
					"3) Modificar cuenta\n" +
					"4) Borrar cuenta\n" +
					"5) Salir\n:");
			opcion = sc.nextByte();

			ga.limpiarTerminal();

			switch (opcion) {
				case 1:
					ga.mostrarCuentas(ga.listaDeCuentas());
					break;
				case 2:
					ga.agregarCuentas();
					break;
				case 3:
					ga.modificarCuenta();
					break;
				case 4:
					ga.eliminarCuenta();
					break;
				case 5:
					System.out.println("¡Adiós!");
					break;
				default:
					System.out.println("Opción no válida. Intente de nuevo.");
			}
		} while (opcion != 5);

		sc.close();
    }
}
