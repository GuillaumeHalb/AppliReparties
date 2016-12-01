#!/bin/sh

gnome-terminal --execute java -jar Server/target/*.jar rmi
gnome-terminal --execute java -jar Server/target/*.jar SG 1 start
gnome-terminal --execute java -jar Server/target/*.jar BNP 2 start
gnome-terminal --execute java -jar Server/target/*.jar Natixis 3 start
gnome-terminal --execute java -jar Server/target/*.jar SG BNP,Natixis add
gnome-terminal --execute java -jar Server/target/*.jar  BNP Natixis add
gnome-terminal --execute java -jar Server/target/*.jar Natixis SG add
