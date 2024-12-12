# Usa una imagen base de OpenJDK
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo .jar generado por Maven o Gradle
COPY target/*.jar app.jar

# Expone el puerto del microservicio articulos
EXPOSE 8095

# Comando para ejecutar el microservicio
ENTRYPOINT ["java", "-jar", "app.jar"]
