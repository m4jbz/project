import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import java.util.Scanner;
import java.util.Arrays;
import javax.swing.*;
import java.io.*;

class GestionArchivos extends Encriptacion {
	static final String llave = "papupapu";
	static String clave = "";
	static Scanner sc = new Scanner(System.in);

	public void mostrarCuentas(String[] cuentas) {
		int cantCuentas = cuentas.length;
		StringBuilder contenido = new StringBuilder();

		if (cantCuentas > 0) {
			for (int i = 0; i < cantCuentas; ++i) {
				try {
					SecretKey llaveSecreta = generarLlaveSecreta(llave);

					String contenidoDesencriptado = archivoDesencriptado(cuentas[i], llaveSecreta);
					contenido.append(String.format("Cuenta %d:", (i+1))).append(contenidoDesencriptado).append("\n");
				} catch (IOException | GeneralSecurityException e) {
					e.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(null, contenido);
		} else {
			JOptionPane.showMessageDialog(null, "No hay cuentas por mostrar.");
		}
	}

	public void modificarCuenta() {
		String nuevoCorreo;
		String nuevaContra;
		byte numCuentas;

		mostrarCuentas(listaDeCuentas());

		if (listaDeCuentas().length > 0) {
			System.out.print("Número de la cuenta a modificar: ");
			numCuentas = sc.nextByte();

			String actualizada = String.format("files/account%d.txt", numCuentas);
			String original = listaDeCuentas()[numCuentas-1];

			File og = new File(original);
			File ac = new File(actualizada);

			if (og.exists()) {
				System.out.print("Nuevo correo: ");
				nuevoCorreo = sc.next();
				System.out.print("Nueva contraseña: ");
				nuevaContra = sc.next();

				og.delete();

				try (BufferedWriter writer = new BufferedWriter(new FileWriter(actualizada,
								false))) {
					writer.write(nuevoCorreo);
					writer.newLine();
					writer.write(nuevaContra);
				} catch (IOException e) {
					System.err.println("Error al crear el archivo: " + e.getMessage());
				}

				try {
					SecretKey llaveSecreta = generarLlaveSecreta(llave);
					encriptarArchivo(actualizada, original, llaveSecreta);
					System.out.println("Cuenta modificada con exito.");
				} catch (IOException | GeneralSecurityException e) {
					e.printStackTrace();
				}

				ac.delete();
			} else {
				System.out.println("El archivo no existe.");
			}
		} else {
			System.out.println("Ni por modificar.");
		}
	}

	public void eliminarCuenta() {
		byte numCuentas;

		mostrarCuentas(listaDeCuentas());

		if (listaDeCuentas().length > 0) {
			System.out.print("Número de la cuenta a eliminar: ");
			numCuentas = sc.nextByte();

			String cuentaABorrar = listaDeCuentas()[numCuentas-1];
			File cb = new File(cuentaABorrar);

			if (cb.exists()) {
				if (cb.delete()) {
					System.out.println("La cuenta ha sido eliminada.");
				}
				else {
					System.out.println("La cuenta no pudo ser eliminada.");
				}
			} else {
				System.out.println("El archivo no existe.");
			} 
		} else {
			System.out.println("Ni por eliminar.");
		}
	}

	public void agregarCuentas() {
		String[] cuentas = listaDeCuentas();
		int cantCuentas = cuentas.length;
		int numArchivo = 0;
		int numCuentas;

		if (cantCuentas > 0 && cantCuentas < 10) {
			numArchivo = Character.getNumericValue(cuentas[cantCuentas-1].charAt(16));
		} else if (cantCuentas > 9) {
			numArchivo = Integer.parseInt(String.format("%c%c", 
						 cuentas[cantCuentas-1].charAt(15) + cuentas[cantCuentas-1].charAt(16)));
		}

		System.out.print("Numero de cuentas: ");
		numCuentas = sc.nextInt();

		String[] cuentasNuevas = pedirDatos(numArchivo, (numArchivo + numCuentas), numCuentas);
		String[] cuentasNuevasEncriptadas = listaDeBinarios(numArchivo, (numArchivo + numCuentas));

		encriptarArchivos(cuentasNuevas, cuentasNuevasEncriptadas, numCuentas);
		eliminarArchivos(cuentasNuevas, cuentasNuevas.length);
	}

	public String[] pedirDatos(int i, int f, int numCuentas) {
		String[] cuentas = new String[numCuentas];
		String[] correos = new String[numCuentas];
		String[] contras = new String[numCuentas];
		int a = 0;

		for (int j = i; j < f; ++j) {
			System.out.print(String.format("%d° Correo: ", (a+1)));
			correos[a] = sc.next();
			System.out.print(String.format("%d° Contraseña: ", (a+1)));
			contras[a] = sc.next();

			if ((j+1) > 9) {
				cuentas[a] = String.format("files/account%d.txt", (j+1));
			} else {
				cuentas[a] = String.format("files/account0%d.txt", (j+1));
			}

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

	public void esUsuario(GestionArchivos ga, Runnable metodo) {
		if (!clave.equals(llave)) {
			System.out.print("Escribe la clave: ");
			clave = sc.next();
			if (clave.equals(llave)) {
				metodo.run();
			} else {
				System.out.println("ERROR: No eres usuario.");
			}
		} else {
			metodo.run();
		}
	}

	public String[] listaDeCuentas() {
		// obtiene la cantidad de archivos dentro del directorio files
		File[] archivos = new File("files").listFiles();
		String[] lista = new String[archivos.length];

		for (int i = 0; i < archivos.length; ++i) {
			lista[i] = "files/" + archivos[i].getName();
		}

		/* se necesita ordenar la lista para
		   evitar errores en el nombre de los archivos */
		Arrays.sort(lista);

		return lista;
	}

	public String[] listaDeBinarios(int i, int f) {
		String[] archivosBin = new String[f-i];
		int a = 0;
		
		for (int j = i; j < f; ++j) {
			if ((j+1) > 9) {
				archivosBin[a] = String.format("files/accrypted%d.bin", (j+1));
			} else {
				archivosBin[a] = String.format("files/accrypted0%d.bin", (j+1));
			}
			a++;
		}

		return archivosBin;
	}

	public void encriptarArchivos(String[] archivosTexto, String[] archivosBinarios, int n) {
		for (int i = 0; i < n; ++i) {
			try {
				SecretKey llaveSecreta = generarLlaveSecreta(llave);
				encriptarArchivo(archivosTexto[i], archivosBinarios[i], llaveSecreta);
			} catch (IOException | GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
	}

	public void eliminarArchivos(String[] archivos, int n) {
		for (int i = 0; i < n; ++i) {
			File fl = new File(archivos[i]);

			if (fl.exists()) {
				if (!fl.delete()) {
					System.out.println("El archivo no pudo ser eliminado.");
				}
			} else {
				System.out.println("Archivo no existe.");
			}
		}
	}

	public void limpiarTerminal() {
		// codigo ANSI para limpiar la terminal
		System.out.print("\033[H\033[2J");
	}
}
