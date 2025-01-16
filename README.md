## CheckNIF

Servicio Rest para validar NIF contra la AEAT

## build

`./gradlew build`

## database

Desde la version 0.0.5 se requiere una base de datos MySQL para validar las peticiones con API-KEY

## run

(requiere un certificado a utilizar y su password correspondiente)

`-/gradlew run`

## Producción

Descargar el binario que se encontrará en la página de `releases` como `checknif` (actualmente sólo disponible para Linux)

Disponer de un certificado p12 y su password. 

- Por defecto el servicio usará `certificado.p12` pero puede ser sobreescrito mediante la variable de entorno `CHECKNIF_CERTIFICADO`

- Por defecto la pasword será `la_pwd` pero puede ser sobreescrita mediante la variable de entorno `CHECKNIF_PASSWORD`


`CHECKNIF_PASSWORD='la_pwd_del_certificado' CHECKNIF_CERTIFICADO=certificado.p12 ./checknif`

## Endpoints

El servicio ofrece una serie de endpoints para validar NIFs de empresa, particulares y/o si está en Recargo de Equivalencia

Con al aplicacion levantada en `http://localhost:8008/` se pueden hacer las siguientes peticiones:

```shell
http -v http://localhost:8080/ X-API-KEY:1232c2 [0][nif]=B80988801 [1][nif]="123456789Q" [1][nombre]="Juan Espanol"

[
    {
        "nif": "B80988801",
        "nombre": "EVALUACION Y DESARROLLO DE NEGOCIOS SL",
        "result": true,
        "resultado": "IDENTIFICADO"
    },
    {
        "nif": "123456789Q",
        "nombre": "Juan Espanol",
        "result": false,
        "resultado": "NO IDENTIFICADO"
    }

]
```

De esta forma, dado un CIF (o varios) se pueden obtener de forma simple la Razón social de cada uno de ellos
(en caso de que result sea `true`) así como si es una persona física obtener (o no) el nombre completo de la persona

También se pueden realizar peticiones "individualizadas" a cada uno de los endpoints:

```shell
http -v http://localhost:8080/1232c2/B80988801

{
    "nif": "B80988801",
    "nombre": "EVALUACION Y DESARROLLO DE NEGOCIOS SL",
    "result": true,
    "resultado": "IDENTIFICADO"
}
```

donde 1232c2 es la API-KEY y B80988801 es el CIF a validar

En caso de querer validar una persona física, el endpoint es similar pero añadiendo la parte del nombre que se tiene:

```shell
http -v "http://localhost:8080/1232c2/123456789Q/Juan español"

{
    "nif": "123456789Q",
    "nombre": "Juan Espanol",
    "result": false,
    "resultado": "NO IDENTIFICADO"
}
```
