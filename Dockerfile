FROM openjdk:17
LABEL authors="HSI"
COPY build/libs/*.jar step-mate.jar
ENTRYPOINT ["java", "-jar","step-mate.jar"]