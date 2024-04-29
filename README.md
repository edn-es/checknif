## CheckNIF

Servicio Rest para validar NIF contra la AEAT

## build

`./gradlew build`

## run

(requiere un certificado a utilizar y su password correspondiente)

`-/gradlew run`

## Producción

Descargar el binario que se encontrará en la página de `releases` como `checknif` (actualmente sólo disponible para Linux)

Disponer de un certificado p12 y su password. 

- Por defecto el servicio usará `certificado.p12` pero puede ser sobreescrito mediante la variable de entorno `CHECKNIF_CERTIFICADO`

- Por defecto la pasword será `la_pwd` pero puede ser sobreescrita mediante la variable de entorno `CHECKNIF_PASSWORD`


`CHECKNIF_PASSWORD='la_pwd_del_certificado' CHECKNIF_CERTIFICADO=certificado.p12 ./checknif`

El servicio ofrece un endpoint en `http://localhost:8008/cifs` que admite una lista de CIFs y devuelve por cada uno

```shell
http -v http://localhost:8080/cifs []=B80988801

[
    {
        "nif": "B80988801",
        "nombre": "EVALUACION Y DESARROLLO DE NEGOCIOS SL",
        "result": true,
        "resultado": "IDENTIFICADO"
    }
]
```

De esta forma, dado un CIF (o varios) se pueden obtener de forma simple la Razón social de cada uno de ellos
(en caso de que result sea `true`)

