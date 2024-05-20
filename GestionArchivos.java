import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;

class GestionArchivos {
  static final String llave = "papupapu";
  static String clave = "";
  static Scanner sc = new Scanner(System.in);

  public void mostrarCuentas(String[] cuentas) {
    int cantCuentas = cuentas.length;
    StringBuilder contenido = new StringBuilder();

    if (cantCuentas > 0) {
      for (int i = 0; i < cantCuentas; ++i) {
          contenido.append("--------------------------------\n");
          contenido.append(String.format("Cuenta %d:\n", (i+1)));

          try (BufferedReader br = new BufferedReader(new FileReader(cuentas[i]))) {
            String linea;
            while ((linea = br.readLine()) != null) {
              contenido.append(linea + "\n");
            }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      contenido.append("--------------------------------\n");
      JOptionPane.showMessageDialog(null, contenido);
    } else {
      JOptionPane.showMessageDialog(null, "No hay cuentas por mostrar.");
    }
  }

  public void agregarCuentas() {
    String[] cuentas = listaDeCuentas();
    int cantCuentas = cuentas.length;
    int numArchivo = 0;
    byte numCuentas;

    if (cantCuentas > 0 && cantCuentas < 10) {
      numArchivo = Character.getNumericValue(cuentas[cantCuentas-1].charAt(16));
    } else if (cantCuentas > 9) {
      numArchivo = Integer.parseInt(String.format("%c%c", 
             cuentas[cantCuentas-1].charAt(15) + cuentas[cantCuentas-1].charAt(16)));
    }

    numCuentas = Byte.parseByte(JOptionPane.showInputDialog(null, "Numero de cuentas:\n"));

    String[] cuentasNuevas = pedirDatos(numArchivo, (numArchivo + numCuentas), numCuentas);
  }

  public void modificarCuenta() {
    String nuevoCorreo;
    String nuevaContra;
    byte numCuenta;

    mostrarCuentas(listaDeCuentas());

    if (listaDeCuentas().length > 0) {
      numCuenta = Byte.parseByte(JOptionPane.showInputDialog(null, "Número de la cuenta a modificar:\n"));

      String actualizada = String.format("files/account%d.txt", numCuenta);
      String original = listaDeCuentas()[numCuenta-1];

      File og = new File(original);

      if (og.exists()) {
        nuevoCorreo = JOptionPane.showInputDialog(null, "Nuevo correo:\n");
        nuevaContra = JOptionPane.showInputDialog(null, "Nueva contraseña:\n");

        og.delete();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(actualizada,
                false))) {
          writer.write(nuevoCorreo);
          writer.newLine();
          writer.write(nuevaContra);
          JOptionPane.showMessageDialog(null, "Cuenta modificada con exito.");
        } catch (IOException e) {
          System.err.println("Error al crear el archivo: " + e.getMessage());
        }
      } else {
        JOptionPane.showMessageDialog(null, "El archivo no existe.");
      }
    } else {
      JOptionPane.showMessageDialog(null, "Ni por modificar.");
    }
  }

  public void eliminarCuenta() {
    byte numCuenta;

    mostrarCuentas(listaDeCuentas());

    if (listaDeCuentas().length > 0) {
      numCuenta = Byte.parseByte(JOptionPane.showInputDialog(null, "Número de la cuenta a eliminar:\n"));

      String cuentaABorrar = listaDeCuentas()[numCuenta-1];
      File cb = new File(cuentaABorrar);

      if (cb.exists()) {
        if (cb.delete()) {
          JOptionPane.showMessageDialog(null, "La cuenta ha sido eliminada.");
        }
        else {
          JOptionPane.showMessageDialog(null, "La cuenta no pudo ser eliminada.");
        }
      } else {
        System.out.println("El archivo no existe.");
      } 
    } else {
      JOptionPane.showMessageDialog(null, "Ni por eliminar.");
    }
  }

  public void esUsuario(GestionArchivos ga, Runnable metodo) {
    if (!clave.equals(llave)) {
      clave = JOptionPane.showInputDialog(null, "Escribe la clave:\n");
      if (clave.equals(llave)) {
        metodo.run();
      } else {
        JOptionPane.showMessageDialog(null, "ERROR: No eres usuario.");
      }
    } else {
      metodo.run();
    }
  }

  public String[] pedirDatos(int i, int f, int numCuentas) {
    String[] cuentas = new String[numCuentas];
    String[] correos = new String[numCuentas];
    String[] contras = new String[numCuentas];
    int a = 0;

    for (int j = i; j < f; ++j) {
      correos[a] = JOptionPane.showInputDialog(null, String.format("%d° Correo:\n", (a+1)));
      contras[a] = JOptionPane.showInputDialog(null, String.format("%d° Contraseña:\n", (a+1)));

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

  public String[] listaDeCuentas() {
    File[] archivos = new File("files").listFiles();
    String[] lista = new String[archivos.length];

    for (int i = 0; i < archivos.length; ++i) {
      lista[i] = "files/" + archivos[i].getName();
    }

    Arrays.sort(lista);

    return lista;
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
}
