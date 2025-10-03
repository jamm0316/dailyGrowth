# --- build stage ---
FROM gradle:8.9-jdk21 AS build
WORKDIR /app
COPY . /app
RUN ./gradlew clean bootJar --no-daemon

# --- runtime stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app
# 산출물 이름이 스냅샷/릴리즈 어떤 것이든 잡히도록 *.jar 사용
COPY --from=build /app/build/libs/*.jar app.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://127.0.0.1:8080/actuator/health || exit 1
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]