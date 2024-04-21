import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import java.util.Scanner;
import java.util.Arrays;
import java.io.*;

class FileManagement extends Encrypt
{
    static final String key = "papupapu";
	static Scanner sc = new Scanner(System.in);

	public void modifyAccount()
	{
		String newMail;
		String newPass;
		byte numAcc;

		showAccounts(listOfFiles());

		if (listOfFiles().length != 0) {
			System.out.print("Número de la cuenta a modificar: ");
			numAcc = sc.nextByte();

			String[] encrypted = listOfFiles();
			String changedFile = String.format("files/account%d.txt", numAcc);

			File fl = new File(encrypted[numAcc-1]);
			File ac = new File(changedFile);

			if (fl.exists()) {
				System.out.print("Nuevo correo: ");
				newMail = sc.next();
				System.out.print("Nueva contraseña: ");
				newPass = sc.next();

				fl.delete();

				try (BufferedWriter writer = new BufferedWriter(new FileWriter(changedFile, false))) {
					writer.write(newMail);
					writer.newLine();
					writer.write(newPass);
				} catch (IOException e) {
					System.err.println("Error al crear el archivo: " + e.getMessage());
				}

				try {
					SecretKey secretKey = generateSecretKey(key);
					encryptFile(changedFile, encrypted[numAcc-1], secretKey);
					System.out.println("Cuenta modificada con exito.");
				} catch (IOException | GeneralSecurityException e) {
					e.printStackTrace();
				}

				ac.delete();

			} else
				System.out.println("El archivo no existe.");
		} else
			System.out.println("Ni por modificar.");
	}

	public void deleteAccount()
	{
		byte numAcc;
		showAccounts(listOfFiles());

		if (listOfFiles().length != 0) {
			System.out.print("Número de la cuenta a eliminar: ");
			numAcc = sc.nextByte();

			String[] encrypted = listOfFiles();

			File fl = new File(encrypted[numAcc-1]);

			if (fl.exists()) {
				if(fl.delete())
					System.out.println("La cuenta ha sido eliminada.");
				else
					System.out.println("La cuenta no pudo ser eliminada.");
			} else {
				System.out.println("El archivo no existe.");
			}
		
		} else
			System.out.println("Ni por eliminar.");
	}

	public void addAccount()
	{
		String[] acc = listOfFiles();
		int lastNum = 0;

		if (acc.length > 0 && acc.length < 10)
			lastNum = Character.getNumericValue(acc[acc.length-1].charAt(16));
		else if (acc.length > 9)
			lastNum = Integer.parseInt(String.format("%c%c", acc[acc.length-1].charAt(15) + acc[acc.length-1].charAt(16)));

		System.out.print("Numero de cuentas: ");
		int numAcc = sc.nextInt();

		String[] accounts = requestData(lastNum, lastNum+numAcc, numAcc);
		String[] encryptedAccounts = listOfBinFiles(accounts, lastNum, lastNum+numAcc);

		makeBinFiles(accounts, encryptedAccounts, numAcc);
		deleteFiles(accounts, accounts.length);
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

	public String[] listOfFiles()
	{
		File directory = new File("files");
		File[] files = directory.listFiles();
		String[] str = new String[files.length];

		for (int i = 0; i < files.length; ++i) {
			str[i] = "files/"+files[i].getName();
		}

		Arrays.sort(str);

		return str;
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

	public void cleanTerm()
	{
		System.out.print("\033[H\033[2J");
	}

}
