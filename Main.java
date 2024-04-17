import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import java.util.Scanner;
import java.io.*;

public class Main
{
    public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		FileManagement fm = new FileManagement();
		String[] accounts;
		String[] encryptedFiles;

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
					fm.verifyFilesExistence();
					break;
				case 5:
					System.out.println("¡Adiós!");
					break;
				default:
					System.out.println("Opción no válida. Intente de nuevo.");
			}

			sc.nextLine();
		} while (options != 5);

		accounts = fm.requestData();
		encryptedFiles = fm.makeBinFiles(accounts);

		for (int i = 0; i < fm.numAcc; ++i) {
			try {
				SecretKey secretKey = fm.generateSecretKey(fm.key);
				fm.encryptFile(accounts[i], encryptedFiles[i], secretKey);
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
			}

			try {
				SecretKey secretKey2 = fm.generateSecretKey(fm.key);

				String decryptedContent = fm.decryptedFile(encryptedFiles[i], secretKey2);
				System.out.println(String.format("Contenido %d: ", (i+1)));
				System.out.println(decryptedContent);
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
			}

		}
		fm.deleteFiles(accounts);

		sc.close();
    }
}
