# 1. JDK 21 이미지를 기반으로 설정
FROM eclipse-temurin:21

# ✅ nc(netcat) 설치 추가
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

# 2. 작업 디렉토리를 설정
WORKDIR /app

# 3. 로컬에서 빌드한 JAR 파일을 Docker 이미지에 복사
COPY build/libs/*.jar /app/app.jar

# 4. 포트 설정 (필요 시)
EXPOSE 8080

# 5. JAR 파일을 실행하는 명령어
CMD ["java", "-jar", "/app/app.jar"]
