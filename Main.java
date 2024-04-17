import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		FileManagement fm = new FileManagement();

		byte options = 0;

		do {
			System.out.print("Escoja una opción: \n" + 
					"1) Ver cuenta(s)\n" + 
					"2) Agregar cuenta(s)\n" + 
					"3) Modificar cuenta\n" +
					"4) Borrar cuenta\n" +
					"5) Salir\n:");
			options = sc.nextByte();
			sc.nextLine();

			switch (options) {
				case 1:
					fm.showAccounts(fm.listOfFiles(fm.numberOfFiles()));;
					break;
				case 2:
					fm.addAccount();
					break;
				case 5:
					System.out.println("¡Adiós!");
					break;
				default:
					System.out.println("Opción no válida. Intente de nuevo.");
			}

			sc.nextLine();
		} while (options != 5);

		sc.close();
    }
}
