# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Gradle 래퍼 및 설정 파일 복사
COPY settings.gradle build.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# elasticsearch-api 모듈 소스 복사
COPY elasticsearch-api ./elasticsearch-api

# 빌드
RUN ./gradlew :elasticsearch-api:bootJar --no-daemon

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/elasticsearch-api/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8081

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
