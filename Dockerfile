# --- Build stage ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Эхлээд dependency татна (кэш ашиглахын тулд)
COPY pom.xml .
RUN mvn dependency:go-offline -q
# Дараа нь код хуулж build хийнэ
COPY src ./src
RUN mvn clean package -DskipTests -q

# --- Runtime stage ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Build stage-аас .jar файл хуулна
COPY --from=build /app/target/file-manager-service-1.0.0.jar app.jar

# 8080 порт нээнэ
EXPOSE 8080

# Нууц мэдээлэл environment variable-аар дамжина:
# docker run -e S3_ACCESS_KEY=xxx -e S3_SECRET_KEY=yyy ...
ENTRYPOINT ["java", "-jar", "app.jar"]
