#!/bin/sh

gnome-terminal --execute java -jar Server/target/*.jar rmi
gnome-terminal --execute java -jar Server/target/*.jar Goldman_Sachs 1 start
gnome-terminal --execute java -jar Server/target/*.jar JP_Morgan 2 start
gnome-terminal --execute java -jar Server/target/*.jar Citi 3 start
gnome-terminal --execute java -jar Server/target/*.jar Merill_Lynch 4 start
gnome-terminal --execute java -jar Server/target/*.jar HSBC 5 start
gnome-terminal --execute java -jar Server/target/*.jar Barclays 6 start
gnome-terminal --execute java -jar Server/target/*.jar BNP 7 start
gnome-terminal --execute java -jar Server/target/*.jar Deutsche_Bank 8 start
 #adding neighboor
gnome-terminal --execute java -jar Server/target/*.jar Goldman_Sachs JP_Morgan,Merill_Lynch add
gnome-terminal --execute java -jar Server/target/*.jar  JP_Morgan Goldman_Sachs,Citi add
gnome-terminal --execute java -jar Server/target/*.jar Citi Merill_Lynch,Barclays,BNP add
gnome-terminal --execute java -jar Server/target/*.jar Merill_Lynch Goldman_Sachs,Citi,HSBC add
gnome-terminal --execute java -jar Server/target/*.jar HSBC Merill_Lynch,Barclays add
gnome-terminal --execute java -jar Server/target/*.jar Barclays Citi,HSBC,BNP add
gnome-terminal --execute java -jar Server/target/*.jar BNP Barclays,Citi,Deutsche_Bank add
gnome-terminal --execute java -jar Server/target/*.jar Deutsche_Bank BNP add





