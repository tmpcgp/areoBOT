@echo off
mvn clean compile && mvn exec:java -Dexec.mainClass="com.xatkit.example.ContainerProd" 
