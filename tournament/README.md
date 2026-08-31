# Tournament

Proyecto Spring Boot para gestionar grupos, equipos, partidos y apuestas de un torneo.

## Requisitos

- Java 17
- Maven (o usar `./mvnw`)
- MySQL en `localhost:3306`
- Base de datos `tournament`

## Ejecutar el proyecto

```zsh
cd "/Users/ludwingbadillo/Lbd/source/worldcup/tournament"
./mvnw spring-boot:run
```

Con la configuracion actual, los endpoints quedan con prefijo `/api`.

Ejemplo:

```zsh
curl -X GET "http://localhost:8080/api/groups" -H "Accept: application/json"
```

## Pruebas unitarias

Las pruebas unitarias usan clases terminadas en `Test`.

```zsh
cd "/Users/ludwingbadillo/Lbd/source/worldcup/tournament"
./mvnw test
```

## Pruebas de integracion

Las pruebas de integracion viven en el paquete `integracion` y terminan en `IT`.
Estas pruebas usan perfil `integration` con base de datos H2 en memoria, por lo que no modifican datos de MySQL.

Ejecutar solo integracion:

```zsh
cd "/Users/ludwingbadillo/Lbd/source/worldcup/tournament"
./mvnw verify -Pintegration-tests
```

## Cobertura con JaCoCo

JaCoCo se ejecuta en el ciclo `verify` y valida un minimo de cobertura de lineas.

```zsh
cd "/Users/ludwingbadillo/Lbd/source/worldcup/tournament"
./mvnw verify
```

Reporte HTML:

- `target/site/jacoco/index.html`

