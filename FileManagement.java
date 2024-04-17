import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import java.util.Scanner;
import java.io.*;

class FileManagement extends Encrypt
{
	static int numAcc;
    static final String key = "papupapu";
	static Scanner sc = new Scanner(System.in);

	public void deleteFiles(String[] files)
	{
		for (int i = 0; i < numAcc; ++i) {
			File fl = new File(files[i]);

			if (fl.exists()) {
				if (!fl.delete()) {
					System.out.println("El archivo NO se elimino.");
				}
			} else {
				System.out.println("Archivo no existe.");
			}
		}
	}

	public void verifyFilesExistence()
	{
		int cont = 0, i = 0;
		boolean flag = true;

		while (flag) {
			File fl = new File(String.format("files/account%d_encrypted.bin", (i+1)));
			i++;

			if (fl.exists())
				cont++;
			else
				flag = false;
		}

		if (cont > 0)
			showAccounts(listOfFiles(cont));
		else 
			System.out.println("No hay cuentas por mostrar.");
	}

	public String[] listOfFiles(int a)
	{
		String[] str = new String[a];
		
		for(int i = 0; i < a; ++i)
			str[i] = String.format("files/account%d_encrypted.bin", (i+1));

		return str;
	}

	public void showAccounts(String[] str)
	{
		for (int i = 0; i < str.length; ++i) {
			try {
				SecretKey secretKey = generateSecretKey(key);

				String decryptedContent = decryptedFile(str[i], secretKey);
				System.out.println("--- --- --- --- ---");
				System.out.println(String.format("Cuenta %d: ", (i+1)));
				System.out.println(decryptedContent);
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
		System.out.println("--- --- --- --- ---");
	}


	public String[] requestData()
	{
		System.out.print("Numero de cuentas: ");
		numAcc = sc.nextInt();

		String[] accounts = new String[numAcc];
		String[] mails = new String[numAcc];
		String[] passwds = new String[numAcc];

		for (int i = 0; i < numAcc; ++i) {
			System.out.print(String.format("%d° Correo: ", (i+1)));
			mails[i] = sc.next();
			System.out.print(String.format("%d° Contraseña: ", (i+1)));
			passwds[i] = sc.next();

			accounts[i] = String.format("files/account%d.txt", (i+1));
		}

		for (int i = 0; i < numAcc; ++i) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(accounts[i], false))) {
				writer.write(mails[i]);
				writer.newLine();
				writer.write(passwds[i]);
			} catch (IOException e) {
				System.err.println("Error al crear el archivo: " + e.getMessage());
			}
		}

		return accounts;
	}

	public String[] makeBinFiles(String[] accounts)
	{
		String[] encryptedFiles = new String[numAcc];
		
		for (int i = 0; i < numAcc; ++i) {
			encryptedFiles[i] = String.format("files/account%d_encrypted.bin", (i+1));
		}

		return encryptedFiles;
	}

}
