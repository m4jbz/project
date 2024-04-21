import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		FileManagement fm = new FileManagement();

		byte options = 0;

		fm.cleanTerm();
		do {
			System.out.print("Escoja una opción: \n" + 
					"1) Ver cuenta(s)\n" + 
					"2) Agregar cuenta(s)\n" + 
					"3) Modificar cuenta\n" +
					"4) Borrar cuenta\n" +
					"5) Salir\n:");
			options = sc.nextByte();
			fm.cleanTerm();

			switch (options) {
				case 1:
					fm.showAccounts(fm.listOfFiles());
					break;
				case 2:
					fm.addAccount();
					break;
				case 3:
					fm.modifyAccount();
					break;
				case 4:
					fm.deleteAccount();
					break;
				case 5:
					System.out.println("¡Adiós!");
					break;
				default:
					System.out.println("Opción no válida. Intente de nuevo.");
			}
		} while (options != 5);

		sc.close();
    }
}
