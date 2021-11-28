# Verificación y Validación del Software 2021

GRUPO IWT-41 grupo 6

## Compilación y pruebas

Este proyecto está configurado para ser compilable desde la consola de comandos a través de Maven. Las pruebas unitarias se ejecutarán con JUnit y con el framework mockito.

Para realizar una nueva compilación del proyecto en un fichero zip si el proyecto pasa las pruebas, se ejecuta el comando:

```
mvn clean package
```

El parámetro `clean` es opcional y solo elimina los elementos que hayan podido quedar de compilaciones previamente realizadas con Maven.

Si se cambia el parámetro `package` por `install`, una copia de la compilación del proyecto se guardará en la carpeta repositorio por defecto de Maven (por ejemplo: `C:\Users\<nombre_usr>\.m2` ).

En caso de que se quiera compilar sin ejecutar pruebas, se puede agregar el parámetro `-DskipTests` de la siguiente manera:

```
mvn clean package -DskipTests
```

## Selección de la suite de pruebas

Este proyecto usa las funcionalidades del plugin [Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/) de Maven para definir Suites de pruebas.

Se han definido 1 de estas suites:

* **PracticaIntegracion**: Suite que se ejecuta por defecto, agrupa a todas las pruebas.

Para ejecutar a una suite se usa el parámetro `-DrunSuite` al que se le tiene que indicar el nombre de la suite que se quiere ejecutar de la siguiente manera: 

```
mvn clean install -DrunSuite=PracticaIntegracion
```

También, se puede ejecutar más de una suite a la vez separando los nombres de las suites por comas en el orden deseado:

```
mvn clean install -DrunSuite=<suite_1>,<suite_2>
```

## Instrucciones para utilizar este proyecto

* Cada alumno debe crearse un usuario de Github
* Un miembro del grupo debe hacer fork a este proyecto y añadir al repositorio forkeado a sus compañeros de grupo y al profesor como colaboradores.
* Descargar el proyecto utilizando git clone desde el terminal o desde el IDE.
* Seguir las indicaciones del enunciado para añadir las dependencias necesarias para empezar a realizar las pruebas
* Modificar este fichero añadiendo el número de grupo correspondiente y las instrucciones para ejecutar la práctica
