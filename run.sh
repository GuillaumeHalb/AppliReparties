#!/bin/sh

gnome-terminal --execute java -jar Server/target/*.jar rmi
gnome-terminal --execute java -jar Server/target/*.jar SG 1 BNP
gnome-terminal --execute java -jar Server/target/*.jar BNP 2 Natixis
gnome-terminal --execute java -jar Server/target/*.jar Natixis 3 SG
