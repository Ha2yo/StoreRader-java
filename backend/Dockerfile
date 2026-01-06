FROM amazoncorretto:21

WORKDIR /app

# 인자 정리
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar","app.jar"]