#!/bin/sh
xterm -hold -e "rmiregistry"
#xterm --hold -e java -jar Server/target/*.jar rmi
xterm -e java -jar Server/target/*.jar SG 1 
xterm -e java -jar Server/target/*.jar BNP 2 
xterm -e java -jar Server/target/*.jar Natixis 3 