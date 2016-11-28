#!/bin/sh

xterm --hold -e java -jar Server/target/*.jar rmi &
xterm -hold -e java -jar Server/target/*.jar SG 1 &
xterm -hold -e java -jar Server/target/*.jar BNP 2 &
xterm -hold -e java -jar Server/target/*.jar Natixis 3 &
