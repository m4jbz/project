import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import java.util.Scanner;
import java.io.*;

class FileManagement extends Encrypt
{
    static final String key = "papupapu";
	static Scanner sc = new Scanner(System.in);

	public int numberOfFiles()
	{
		int cont = 0, i = 0;
		boolean flag = true;

		while (flag) {
			File fl = null;
			if ((i+1) > 9)
				fl = new File(String.format("files/accrypted%d.bin", (i+1)));
			else 
				fl = new File(String.format("files/accrypted0%d.bin", (i+1)));
			i++;
			if (fl.exists()) cont++;
			else flag = false;
		}

		return cont;
	}

	public void addAccount()
	{
		String[] acc = listOfFiles(numberOfFiles());
		int lastNum = 0;

		if (acc.length > 0 && acc.length < 10)
			lastNum = Character.getNumericValue(acc[acc.length-1].charAt(16));
		else if (acc.length > 0 && acc.length > 9)
			lastNum = Integer.parseInt(String.format("%c%c", acc[acc.length-1].charAt(15) + acc[acc.length-1].charAt(16)));

		System.out.print("Numero de cuentas: ");
		int numAcc = sc.nextInt();

		String[] accounts = requestData(lastNum, lastNum+numAcc, numAcc);
		String[] encryptedAccounts = listOfBinFiles(accounts, lastNum, lastNum+numAcc);

		makeBinFiles(accounts, encryptedAccounts, numAcc);
		deleteFiles(accounts, accounts.length);
	}

	public void deleteFiles(String[] files, int n)
	{
		for (int i = 0; i < n; ++i) {
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

	public String[] listOfFiles(int a)
	{
		String[] str = new String[a];
		
		for (int i = 0; i < a; ++i)
			if ((i+1) > 9)
				str[i] = String.format("files/accrypted%d.bin", (i+1));
			else 
				str[i] = String.format("files/accrypted0%d.bin", (i+1));

		return str;
	}

	public void showAccounts(String[] str)
	{
		if (str.length > 0) {
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
		} else System.out.println("No hay cuentas por mostrar.");
	}

	public String[] requestData(int n, int m, int numAcc)
	{
		int a = 0;
		String[] accounts = new String[numAcc];
		String[] mails = new String[numAcc];
		String[] passwds = new String[numAcc];

		for (int i = n; i < m; ++i) {
			System.out.print(String.format("%d° Correo: ", (a+1)));
			mails[a] = sc.next();
			System.out.print(String.format("%d° Contraseña: ", (a+1)));
			passwds[a] = sc.next();
			if ((i+1) > 9)
				accounts[a] = String.format("files/account%d.txt", (i+1));
			else 
				accounts[a] = String.format("files/account0%d.txt", (i+1));
			a++;
		}

		for (int i = 0; i < m-n; ++i) {
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

	public String[] listOfBinFiles(String[] accounts, int n, int m)
	{
		String[] encryptedFiles = new String[m-n];
		int a = 0;
		
		for (int i = n; i < m; ++i) {
			if ((i+1) > 9)
				encryptedFiles[a] = String.format("files/accrypted%d.bin", (i+1));
			else 
				encryptedFiles[a] = String.format("files/accrypted0%d.bin", (i+1));
			a++;
		}

		return encryptedFiles;
	}

	public void showContent(String[] encryptedAccounts)
	{
		for (int i = 0; i < 1; ++i) {
			try {
				SecretKey secretKey2 = generateSecretKey(key);

				String decryptedContent = decryptedFile(encryptedAccounts[i], secretKey2);
				System.out.println(String.format("Contenido %d: ", (i+1)));
				System.out.println(decryptedContent);
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
	}

	public void makeBinFiles(String[] accounts, String[] encryptedAccounts, int n)
	{
		for (int i = 0; i < n; ++i) {
			try {
				SecretKey secretKey = generateSecretKey(key);
				encryptFile(accounts[i], encryptedAccounts[i], secretKey);
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
	}

}
