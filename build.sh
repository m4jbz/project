#!/bin/sh

set -xe

javac -d target Main.java GestionArchivos.java Encriptacion.java -Xlint:all
