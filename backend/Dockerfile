# --- build stage ---
# CI에서 JDK 17로 테스트하고 있다면 빌드 스테이지도 17로 맞추자
FROM gradle:8.9-jdk17 AS build
WORKDIR /app
COPY . /app

# gradlew 실행권한 부여 (권한 문제 예방)
RUN chmod +x gradlew

# 로그를 풍부하게, 테스트는 이미지 빌드 단계에선 제외
RUN ./gradlew clean bootJar -x test --no-daemon --stacktrace

# --- runtime stage ---
FROM eclipse-temurin:17-jre
WORKDIR /app
# 산출물 이름이 스냅샷/릴리즈 어떤 것이든 매칭
COPY --from=build /app/build/libs/*.jar app.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://127.0.0.1:8080/actuator/health || exit 1
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]