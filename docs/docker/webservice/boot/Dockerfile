# 1) 빌드 환경 jar 생성 
FROM gradle:8.5-jdk17 AS builder 

WORKDIR /app 

# 캐시 최적화를 위해 Gradle 관련 파일 먼저 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
# 💡 build.gradle 파일이 변경되지 않으면, 아래 의존성 다운로드 과정은 캐시를 사용해 건너뜁니다!

# gradlew에 실행 권한 부여
RUN chmod +x gradlew

# 전체 소스 복사 후 빌드
COPY . .

# (만약을 위해) 다시 gradlew에 실행 권한 부여
RUN chmod +x gradlew

# Spring Boot JAR 빌드 (테스트는 생략하여 빌드 속도 향상)
RUN ./gradlew clean bootJar -x test --no-daemon


# 2) 이미지 생성 
# 2단계: 실행 환경
FROM eclipse-temurin:17-jre-alpine
# Java 17 실행 환경만 있는 초경량 이미지 사용

WORKDIR /app 
# 작업 공간은 /app

# 'builder' 스테이지에서 빌드된 JAR 파일만 복사해와서 app.jar로 이름 변경
COPY --from=builder /app/build/libs/*.jar app.jar

# 8080 포트를 외부에 노출할 것이라고 명시
EXPOSE 8080

# 컨테이너 시작 시 실행될 최종 명령어
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]