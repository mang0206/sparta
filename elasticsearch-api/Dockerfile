# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Gradle 래퍼 및 설정 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew && \
    sed -i 's/\r$//' ./gradlew
# 빌드
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8081

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
