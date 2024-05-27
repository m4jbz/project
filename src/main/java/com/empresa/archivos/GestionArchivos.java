package com.empresa.archivos;

import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;

class GestionArchivos { static final String llave = "123pass";
  static String clave = "";
  static Scanner sc = new Scanner(System.in);

  public void mostrarCuentas(String[] cuentas) {
    int cantCuentas = cuentas.length;
    StringBuilder contenido = new StringBuilder();

    if (cantCuentas > 0) {
      for (int i = 0; i < cantCuentas; ++i) {
        contenido.append("--------------------------------\n");
        contenido.append(String.format("Miembro %d:\n", (i+1)));

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

    pedirDatos(numArchivo, (numArchivo + numCuentas), numCuentas);
  }

  public String tipoPago() {
    String pago;

    do {
      pago = JOptionPane.showInputDialog(null, String.format("Tipo de pago:\n" +
        "1) Un solo día ($40)\n"+
        "2) Semanal ($200)\n"+
        "3) Mensual ($400)\n"+
        "4) Anual   ($4000)\n"));

      String confirmacion = JOptionPane.showInputDialog(null, "Confirmar pago:\n"+
                                        "1) Si\n"+
                                        "2) No");
      if (confirmacion.equals("1")) {
        switch (pago) {
          case "1":
            pago = "Un solo día\nFalta 1 día.";
            break;
          case "2":
            pago = "Semanal\nFaltan 7 días.";
            break;
          case "3":
            pago = "Mensual\nFaltan 30 días.";
            break;
          case "4":
            pago = "Anual\nFaltan 365 días.";
            break;
          default:
            JOptionPane.showMessageDialog(null, "Opción no existe.");
            pago = "";
        }
      } else {
        JOptionPane.showMessageDialog(null, "Pago no realizado.");
        return "";
      }
    } while (pago == "");

    return pago;
  }

  public void modificarCuenta() {
    String nuevoNombre;
    int nuevoNumero = -1;
    int numCuenta;
    boolean numValido = false;
    boolean nombreValido = false;

    mostrarCuentas(listaDeCuentas());

    if (listaDeCuentas().length > 0) {
      try {
        numCuenta = Integer.parseInt(JOptionPane.showInputDialog(null, "Número del usuario a modificar:\n"));

        String actualizada = String.format("files/account%d.txt", numCuenta);
        try {
          String original = listaDeCuentas()[numCuenta-1];
          File og = new File(original);

          if (og.exists()) {
            do {
              nuevoNombre = JOptionPane.showInputDialog(null, "Nuevo nombre:\n");
              if (nuevoNombre.equals("")) {
                JOptionPane.showMessageDialog(null, "No se ingreso nada.");
                continue;
              }
              nombreValido = true;
            } while (!nombreValido);

            do {
              try {
                nuevoNumero = Integer.parseInt(JOptionPane.showInputDialog(null, "Nuevo N° de teléfono:\n"));
                numValido = true;
              } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "No se ingreso un número.");
              }
            } while (!numValido);

            String pago = tipoPago();

            if (!pago.isEmpty()) {
              og.delete();
              try (BufferedWriter writer = new BufferedWriter(new FileWriter(actualizada,
                      false))) {
                writer.write(nuevoNombre);
                writer.newLine();
                writer.write(String.valueOf(nuevoNumero));
                writer.newLine();
                writer.write(pago);
                JOptionPane.showMessageDialog(null, "Usuario modificado con exito.");
              } catch (IOException e) {
                System.err.println("Error al crear el archivo: " + e.getMessage());
              }
            }
          } else {
            JOptionPane.showMessageDialog(null, "El archivo no existe.");
          }
        } catch (ArrayIndexOutOfBoundsException e) {
          JOptionPane.showMessageDialog(null, "No existe tal usuario.");
        }
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "No ingreso un número.");
      }
    } else {
      JOptionPane.showMessageDialog(null, "Ni por modificar.");
    }
  }

  public String[] pedirDatos(int i, int f, int numCuentas) {
    String[] cuentas = new String[numCuentas];
    String[] nombres = new String[numCuentas];
    int[] n_telefono = new int[numCuentas];
    String[] pagos = new String[numCuentas];
    int a = 0;

    for (int j = i; j < f; ++j) {
      nombres[a] = JOptionPane.showInputDialog(null, String.format("%d° Nombre:\n", (a+1)));
      try {
        n_telefono[a] = Integer.parseInt(JOptionPane.showInputDialog(null,
              String.format("%d° N° de teléfono:\n", (a+1))));
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "No se ingreso un número.");
        return new String[0]; 
      }

      pagos[a] = tipoPago();

      if (pagos[a].isEmpty()) {
        return new String[0]; 
      }

      if ((j+1) > 9) {
        cuentas[a] = String.format("files/account%d.txt", (j+1));
      } else {
        cuentas[a] = String.format("files/account0%d.txt", (j+1));
      }

      a++;
    }

    for (int j = 0; j < (f - i); ++j) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(cuentas[j], false))) {
        writer.write(nombres[j]);
        writer.newLine();
        writer.write(String.valueOf(n_telefono[j]));
        writer.newLine();
        writer.write(pagos[j]);
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

  public void eliminarCuenta() {
    int numCuenta;

    mostrarCuentas(listaDeCuentas());

    if (listaDeCuentas().length > 0) {

      try {
        numCuenta = Integer.parseInt(JOptionPane.showInputDialog(null, "Número del usuario a eliminar:\n"));
        try {
          String cuentaABorrar = listaDeCuentas()[numCuenta-1];
          File cb = new File(cuentaABorrar);

          if (cb.exists()) {
            if (cb.delete()) {
              JOptionPane.showMessageDialog(null, "La cuenta ha sido eliminada.");
            } else {
              JOptionPane.showMessageDialog(null, "La cuenta no pudo ser eliminada.");
            }
          } else {
            JOptionPane.showMessageDialog(null, "El archivo no existe.");
          } 
        } catch (ArrayIndexOutOfBoundsException e) {
          JOptionPane.showMessageDialog(null, "No existe tal usuario.");
        }
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "No ingreso un número.");
      }
    } else {
      JOptionPane.showMessageDialog(null, "Ni por eliminar.");
    }
  }

  public boolean esUsuario() {
    if (!clave.equals(llave)) {
      clave = JOptionPane.showInputDialog(null, "Escribe la clave:\n");
      if (clave.equals(llave)) {
        return true;
      } else {
        JOptionPane.showMessageDialog(null, "ERROR: No eres usuario.");
      }
    } else {
      return true;
    }

    return false;
  }
}
