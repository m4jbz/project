# Funcionamiento del proyecto

Se divide en 3 clases (Main, GestionArchivos, y Encriptacion). La clase Encriptacion utiliza
los métodos para encriptar proporcianados por Java haciendo uso de una key de 256 bits y el 
algoritmo de cifrado AES (Estandar de Encriptacion Avanzada), dando así una buena seguridad 
en los archivos.

La clase GestionArchivos se encarga de utilizar los métodos de la clase Encriptacion en los
archivos de texto para generar los archivos binarios. Basicamente se crea un array de String
el cual cuenta con la ruta y el nombre de los archivos, primero en texto para luego pasarlos
a archivos binarios y eliminar los archivos de texto anteriores. A su vez incluye los 4 
métodos principales de una base de datos (ver, agregar, modificar, y eliminar, *cuentas en 
este caso*).

Por último la clase Main solo se encarga de hacer el menú de opciones, siendo estas los 4
métodos mencionados en el parráfo anterior.

## Compilar y ejecutar

Para compilar en un sistema Linux hace falta con ejecutar el script **build.sh** y luego sim
plemente ejecutar el programa.

```bash
./build.sh
java -cp target Main
```
