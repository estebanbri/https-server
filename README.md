Nota: cuando se habla de cliente, un cliente puede ser un browser o tu misma app que crees
sea cliente de una app que esta bajo ssl por ende vos pasarias ser el cliente.

## Comandos a ejecutar para generar un server que funcione bajo ssl
### Del lado del server:
Paso 1: First Generate the server Certificate and public/private key and store it in keystore file
keytool -genkey -keyalg RSA -keysize 2048 -validity 360 -alias mykey -keystore myKeyStore.jks -ext san=dns:localhost

Paso 2: Configurar la ubicacion del keyStore, alias de la key public y passwords
- Si tu app es Spring Boot: Definir las properties (en caso de que tu app sea spring boot) que figuran en el application.properties que configuran a tomcat para comunicarse via https en vez de http.
- Si tu app es No es Spring Boot: Vas a tener que configurar mediante linea de comando al momento de ejecutar tu jar le pasas como system property (-D) o sino en una clase definir
la configuracion y en runtime se seteen los valores usando System.setProperty
Ejemplo:
> System.setProperty("javax.net.ssl.keyStore","clientKeyStore.key");
> System.setProperty("javax.net.ssl.keyStorePassword","qwerty");
> etc

Paso 3: Export the certficate and the public key that should be send to the client
keytool -export -alias mykey -keystore myKeyStore.jks -file mykey.crt

### Del lado del cliente:
En caso de que el cliente sea el browser: Simplemente installa el .cert haciendole doble click y listo.
Busca el almacen de certificados: "Entidades de certificación raíz de confianza"  (Trusted Root Certification Authorities) Contiene todos los certificados autorizados por CA's

En caso de que el cliente sea una app java:
Paso 1: Add the key at the client side to a TrustedStore (cacerts) to trust the server
keytool -import -file mykey.crt -alias mykey -keystore myTrustStore.jts


## Beneficios de SSL
- Client Trust 
> El cliente (browser) tiene la capacidad de hacer una "validacion" analizando el certificado publico emitido por el server al momento de hacer la primera conexion. 
> 
> El cliente (browser) mirará si dicho certificado publico del server fue emitido por un CA, fecha de caducidad etc. Si es valido mostrara un candado verde en la URL, sino un candaro rojo si no puede validar dicho certificado, le avisara al cliente que al sitio/api que intenta conectarse no es segura por este motivo
- Encrypted Communication
> La informacion enviada y recibida entre el cliente y servidor esta cifrada (cifrada por un par de llaves llamadas shared key [simetric key] generada por el cliente)
> 

Escenario real de conexion SSL:

Paso 1:
El cliente (browser) hace un request a yahoo.com.
![This is an image](https://myoctocat.com/assets/images/base-octocat.svg)

Paso 2:
El server le envia al cliente su certificado ssl + la llave publica del server.
![This is an image](https://myoctocat.com/assets/images/base-octocat.svg)

Paso 3:
El cliente valida el certificado si fue emitido por un CA y los parametros si son validos.
En este paso el cliente valida que es realmente yahoo.com a quien nos estaremos conectando y no a otro server (hacker).
Si el encuentra en su TrustStore el certificado entonces es valido le aparece un candado verde en el navegador y sino uno rojo avisando que es peligroso ingresar a dicha web.
![This is an image](https://myoctocat.com/assets/images/base-octocat.svg)

Paso 4:
El cliente genera un par de shared key (simetric key). Una de las dos key generadas por el cliente
la va a enviar al server en una caja cerrada con la llave publica del server.
![This is an image](https://myoctocat.com/assets/images/base-octocat.svg)

Paso 5:
Cuando el server recibe la caja cerrada usa su clave privada para abrir dicha caja que contiene la shared key enviada por el cliente.
![This is an image](https://myoctocat.com/assets/images/base-octocat.svg

Paso 6:
Listo, todo request y response tanto desde el cliente como desde el server van a estar cifrados usando la misma shared key (generada por el cliente)
![This is an image](https://myoctocat.com/assets/images/base-octocat.svg)


Resultado final de tu ssl server al ser llamado desde un navegador y que en el cliente se haya instalado el certificado
![This is an image](https://myoctocat.com/assets/images/base-octocat.svg)

Fuente: 
- https://www.youtube.com/watch?v=33VYnE7Bzpk (Sunny Classroom: How SSL certificate works?)
- https://www.youtube.com/watch?v=eBEq0Kv7vsw (Code Java: Configure HTTPS for Spring Boot application on localhost with self-signed certificate)
- https://www.youtube.com/watch?v=HLSmjZ5vN0w (Romanian Coder: [Spring Boot Security] #15 Enable HTTPS/SSL in Spring Boot)(este video muestra redireccion http puerto 80 a puerto securizado en nuestro caso 8443 de nuestro server)
- https://www.youtube.com/watch?v=VSi3KFlVAbE (iamakrem: SSL with Java example using simple client server echo app)
- https://github.com/greenlearner01/ssl
- https://www.youtube.com/watch?v=QXoleqFvhSE (How to start spring boot application in ssl mode)
- https://www.youtube.com/watch?v=SbuPNXXvTJA (Access HTTPS RESTful service using RestTemplate || Bypass SSL validation in Spring RestTemplate)