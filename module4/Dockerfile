FROM amazoncorretto:17-alpine-jdk
COPY ./target/*jar-with-dependencies.jar app.jar
RUN mkdir ./cert
ENTRYPOINT ["java","-jar","/app.jar"]