#!/bin/bash
javac ./*.java
javac ./Train_Files/*.java
cd ./Train_Files
java Asteroids_Train $1
cd ..
