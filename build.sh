#!/bin/sh

set -xe

javac -Xlint:all -d target Main.java GestionArchivos.java Encriptacion.java
