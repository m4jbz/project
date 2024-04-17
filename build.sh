#!/bin/sh

set -xe

javac -d target Main.java Encrypt.java FileManagement.java -Xlint:all
