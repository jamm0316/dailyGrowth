# --- build stage ---
FROM gradle:8.9-jdk21 AS build
COPY backend /app
WORKDIR /app
RUN ./gradlew clean bootJar --no-daemon

# --- runtime stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*SNAPSHOT*.jar app.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://127.0.0.1:8080/actuator/health || exit 1
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
