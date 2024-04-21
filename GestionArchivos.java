import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import java.util.Scanner;
import java.util.Arrays;
import java.io.*;

class GestionArchivos extends Encriptacion
{
    static final String llave = "papupapu";
	static Scanner sc = new Scanner(System.in);

	public void modificarCuenta()
	{
		String nuevoCorreo;
		String nuevaContra;
		byte numCuentas;

		mostrarCuentas(listaDeArchivos());

		if (listaDeArchivos().length != 0) {
			System.out.print("Número de la cuenta a modificar: ");
			numCuentas = sc.nextByte();

			String[] encrypted = listaDeArchivos();
			String changedFile = String.format("files/account%d.txt", numCuentas);

			File fl = new File(encrypted[numCuentas-1]);
			File ac = new File(changedFile);

			if (fl.exists()) {
				System.out.print("Nuevo correo: ");
				nuevoCorreo = sc.next();
				System.out.print("Nueva contraseña: ");
				nuevaContra = sc.next();

				fl.delete();

				try (BufferedWriter writer = new BufferedWriter(new FileWriter(changedFile, false))) {
					writer.write(nuevoCorreo);
					writer.newLine();
					writer.write(nuevaContra);
				} catch (IOException e) {
					System.err.println("Error al crear el archivo: " + e.getMessage());
				}

				try {
					SecretKey llaveSecreta = generarLlaveSecreta(llave);
					encriptarArchivo(changedFile, encrypted[numCuentas-1], llaveSecreta);
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

	public void eliminarCuenta()
	{
		byte numCuentas;
		mostrarCuentas(listaDeArchivos());

		if (listaDeArchivos().length != 0) {
			System.out.print("Número de la cuenta a eliminar: ");
			numCuentas = sc.nextByte();

			String[] cuentasEncriptadas = listaDeArchivos();

			File fl = new File(cuentasEncriptadas[numCuentas-1]);

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

	public void agregarCuentas()
	{
		String[] cuentas = listaDeArchivos();
		int f = 0;

		if (cuentas.length > 0 && cuentas.length < 10)
			f = Character.getNumericValue(cuentas[cuentas.length-1].charAt(16));
		else if (cuentas.length > 9)
			f = Integer.parseInt(String.format("%c%c", cuentas[cuentas.length-1].charAt(15) +
						cuentas[cuentas.length-1].charAt(16)));

		System.out.print("Numero de cuentas: ");
		int numCuentas = sc.nextInt();

		String[] cuentasNuevas = pedirDatos(f, (f + numCuentas), numCuentas);
		String[] cuentasEncriptadas = listaDeArchivosBin(f, (f + numCuentas));

		hacerArchivosBin(cuentasNuevas, cuentasEncriptadas, numCuentas);
		eliminarArchivos(cuentasNuevas, cuentasNuevas.length);
	}

	public void mostrarCuentas(String[] cuentas)
	{
		if (cuentas.length > 0) {
			for (int i = 0; i < cuentas.length; ++i) {
				try {
					SecretKey llaveSecreta = generarLlaveSecreta(llave);

					String contenidoDesencriptado = archivoDesencriptado(cuentas[i], llaveSecreta);
					System.out.println("--- --- --- --- ---");
					System.out.println(String.format("Cuenta %d: ", (i+1))); 
					System.out.println(contenidoDesencriptado);
				} catch (IOException | GeneralSecurityException e) {
					e.printStackTrace();
				}
			}
			System.out.println("--- --- --- --- ---");
		} else System.out.println("No hay cuentas por mostrar."); 
	}

	public String[] pedirDatos(int i, int f, int numCuentas)
	{
		int a = 0;
		String[] cuentas = new String[numCuentas];
		String[] correos = new String[numCuentas];
		String[] contras = new String[numCuentas];

		for (int j = i; j < f; ++j) {
			System.out.print(String.format("%d° Correo: ", (a+1)));
			correos[a] = sc.next();
			System.out.print(String.format("%d° Contraseña: ", (a+1)));
			contras[a] = sc.next();
			if ((j+1) > 9)
				cuentas[a] = String.format("files/account%d.txt", (j+1));
			else 
				cuentas[a] = String.format("files/account0%d.txt", (j+1));
			a++;
		}

		for (int j = 0; j < (f - i); ++j) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(cuentas[j], false))) {
				writer.write(correos[j]);
				writer.newLine();
				writer.write(contras[j]);
			} catch (IOException e) {
				System.err.println("Error al crear el archivo: " + e.getMessage());
			}
		}

		return cuentas;
	}

	public String[] listaDeArchivos()
	{
		File directorio = new File("files");
		File[] archivos = directorio.listFiles();
		String[] lista = new String[archivos.length];

		for (int i = 0; i < archivos.length; ++i) {
			lista[i] = "files/" + archivos[i].getName();
		}

		Arrays.sort(lista);

		return lista;
	}

	public String[] listaDeArchivosBin(int i, int f)
	{
		String[] archivosDecriptados = new String[f - i];
		int a = 0;
		
		for (int j = i; j < f; ++j) {
			if ((j+1) > 9)
				archivosDecriptados[a] = String.format("files/accrypted%d.bin", (j+1));
			else 
				archivosDecriptados[a] = String.format("files/accrypted0%d.bin", (j+1));
			a++;
		}

		return archivosDecriptados;
	}

	public void hacerArchivosBin(String[] cuentas, String[] cuentasEncriptadas, int n)
	{
		for (int i = 0; i < n; ++i) {
			try {
				SecretKey llaveSecreta = generarLlaveSecreta(llave);
				encriptarArchivo(cuentas[i], cuentasEncriptadas[i], llaveSecreta);
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
	}

	public void eliminarArchivos(String[] archivos, int n)
	{
		for (int i = 0; i < n; ++i) {
			File fl = new File(archivos[i]);

			if (fl.exists()) {
				if (!fl.delete()) {
					System.out.println("El archivo NO se elimino.");
				}
			} else {
				System.out.println("Archivo no existe.");
			}
			
		}
	}

	public void limpiarTerminal()
	{
		System.out.print("\033[H\033[2J");
	}

}
