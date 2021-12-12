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

Se ha definido una sola de estas suites puesto que esta funcionalidad ya se ha probado en el [proyecto de caja negra](https://github.com/juliosanz/vv_caja_negra/tree/main):

* **PracticaIntegracion**: Suite que se ejecuta por defecto, agrupa a todas las pruebas.

Para ejecutar a una suite se usa el parámetro `-DrunSuite` al que se le tiene que indicar el nombre de la suite que se quiere ejecutar de la siguiente manera: 

```
mvn clean install -DrunSuite=PracticaIntegracion
```

También, se puede ejecutar más de una suite a la vez separando los nombres de las suites por comas en el orden deseado:

```
mvn clean install -DrunSuite=<suite_1>,<suite_2>
```

Esta configuración también permite ejecutar los test de una clase individual. Por ejemplo:

```
mvn clean install -DrunSuite=TestInvalidUser -DfailIfNoTests=true
```

Ejecuta exclusivamente los test definidos en la clase [TestInvalidUser.java](src/test/java/com/practica/integracion/TestInvalidUser.java). El parámetro `-DfailIfNoTests` es un parámetro opcional de _Surefire_ que por defecto está inicializado a `false` que permite fallar la compilación si por alguna razón no se ha ejecutado ningún tests. Este caso puede producirse si se escribe mal el nombre de la clase de pruebas que se quiere ejecutar y Maven no consigue ningún test por ejemplo.

Nota técnica: A diferencia de las declaraciones de las Suites, no se ha agregado ningún `DisplayName` para los tests de clases individuales ya que un bug de _Surefire_ no permite que se muestren por la consola de comandos como debería.

## Checklist de entrega del repo

- [x] Cada alumno debe crearse un usuario de Github
- [X] Un miembro del grupo debe hacer fork a este proyecto y añadir al repositorio forkeado a sus compañeros de grupo y al profesor como colaboradores.
- [X] Descargar el proyecto utilizando git clone desde el terminal o desde el IDE.
- [X] Seguir las indicaciones del enunciado para añadir las dependencias necesarias para empezar a realizar las pruebas
- [X] Modificar este fichero añadiendo el número de grupo correspondiente y las instrucciones para ejecutar la práctica
- [X] Asegurarse que el profesor tiene acceso al repo de git
