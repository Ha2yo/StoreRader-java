FROM amazoncorretto:21

WORKDIR /app

COPY . .

# 개행문자 오류 해결 [unix와 window 시스템 차이]
RUN sed -i 's/\r$//' gradlew

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test

RUN mv /app/build/libs/backend-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]