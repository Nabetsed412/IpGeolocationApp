
# IpGeolocationApp

## Descripción General

IpGeolocationApp es una API desarrollada en Java que permite obtener información geográfica de una dirección IP. Se conecta a un servicio externo de geolocalización para obtener detalles como el país y las tasas de cambio. La aplicación utiliza una base de datos H2 para almacenar los datos de geolocalización, lo que permite un acceso más rápido.

## Funcionalidades

- Consulta de geolocalización por IP, proporcionando información del país y el código ISO.
- Almacenamiento y gestión de los datos de geolocalización en una base de datos H2 en memoria.
- Capacidad de consultar estadísticas de distancia (mínima, máxima y promedio).
- Interfaz de línea de comandos simple e intuitiva.

## Componentes

- **`IpGeolocationApp`**: Clase principal responsable de ejecutar la aplicación. Inicializa la aplicación y gestiona las llamadas a la API para obtener la información relacionada con la IP.
- **`ApiIpResponse`**: Maneja las respuestas de la API de geolocalización de IP.
- **`CountryResponse`**: Contiene la información específica del país devuelta por la API.
- **`ExchangeRateResponse`**: Maneja la información sobre las tasas de cambio cuando se solicita.
- **`DatabaseManager`**: Administra las interacciones con la base de datos H2, incluyendo el almacenamiento y la recuperación de datos de geolocalización.
- **`H2ServerStarter`**: Inicia y gestiona el servidor H2, asegurando que la base de datos esté accesible cuando la aplicación se ejecuta.

## Tecnologías Utilizadas

- **Java 22**: Lenguaje principal de programación.
- **Maven**: Sistema de construcción y gestión de dependencias.
- **Apache HttpClient**: Para realizar solicitudes HTTP a la API de geolocalización.
- **Jackson Databind**: Para analizar las respuestas JSON.
- **Base de datos H2**: Base de datos en memoria para almacenar datos de geolocalización.

## Requisitos

- Java 22
- Maven 3.6+

## Instrucciones de Configuración

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/your-username/IpGeolocationApp.git
cd IpGeolocationApp
```

### Paso 2: Construir el Proyecto

Usa Maven para construir el proyecto y resolver las dependencias:

```bash
mvn clean install
```

### Paso 3: Ejecutar la Aplicación

Para ejecutar la aplicación, utiliza el siguiente comando de Maven:

```bash
mvn exec:java -Dexec.mainClass="IpGeolocationApp"
```

Esto iniciará el servidor H2 y la API de geolocalización.

### Paso 4: Consultar la API

Una vez que la aplicación esté en ejecución, puedes usar la línea de comandos para consultar la geolocalización de cualquier IP:

```bash
java -jar target/IpGeolocationApp.jar 192.168.1.1
```

### Paso 5: Acceder a la Consola de la Base de Datos H2

Puedes acceder a la base de datos H2 a través de un navegador web para depuración o consultas:

```
http://localhost:8082
```

## Ejemplo de Uso

```
java -jar IpGeolocationApp.jar <DIRECCIÓN_IP>
```

Sustituye `<DIRECCIÓN_IP>` por la IP que desees consultar.


## Desarrollado por

Este proyecto fue desarrollado por [Juan Esteban Salazar](https://github.com/Nabetsed412).

## Licencia

Este proyecto está licenciado bajo la Licencia MIT.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT.
