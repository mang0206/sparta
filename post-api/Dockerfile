# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Gradle 래퍼 및 설정 파일 복사
COPY settings.gradle build.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# post-api 모듈 소스 복사
COPY post-api ./post-api

# 빌드
RUN ./gradlew :post-api:bootJar --no-daemon

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/post-api/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
