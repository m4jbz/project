#!/bin/sh

set -xe

javac -d target Main.java FileManagement.java Encrypt.java -Xlint:all
