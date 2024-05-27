package com.empresa.archivos;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Font;

public class Main {
  public static void main(String[] args) {
    // Ajustes visuales.
    UIManager.put("OptionPane.messageFont", new Font(Font.SANS_SERIF, Font.BOLD, 13));
    UIManager.put("OptionPane.messageForeground", Color.lightGray);
    UIManager.put("OptionPane.background", Color.darkGray);
    UIManager.put("Button.foreground", Color.lightGray);
    UIManager.put("Button.background", Color.darkGray);
    UIManager.put("Panel.background", Color.darkGray);

    GestionArchivos ga = new GestionArchivos();
    Scanner sc = new Scanner(System.in);
    String opcion;
    boolean valida = true;

    do {
      opcion = JOptionPane.showInputDialog(null,
                      "            == == == == == == ==\n" +
                      "                          GYM \n" +
                      "                    Curipapus\n" +
                      "            == == == == == == ==\n" +
                      "Escoja una opción: \n" + 
                      "1) Mostrar usuarios\n" + 
                      "2) Agregar usuario(s)\n" + 
                      "3) Modificar usuario\n" +
                      "4) Eliminar usuario\n" +
                      "5) Salir");

      if (!opcion.equals("5")) {
        switch (opcion) {
          case "1":
          case "2":
          case "3":
          case "4":
            break;
          default:
            valida = false;
            JOptionPane.showMessageDialog(null, "Opción no válida. Intente de nuevo.");
        }

        if (valida && ga.esUsuario()) {
          switch (opcion) {
            case "1":
                ga.mostrarCuentas(ga.listaDeCuentas());
              break;
            case "2":
                ga.agregarCuentas();
              break;
            case "3":
                ga.modificarCuenta();
              break;
            case "4":
                ga.eliminarCuenta();
              break;
            default:
              JOptionPane.showMessageDialog(null, "Opción no válida. Intente de nuevo.");
          }
        }
      } else {
        JOptionPane.showMessageDialog(null, "Adios");
        break;
      }
    } while (!opcion.equals("5"));

    sc.close();
  }
}
