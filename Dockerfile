FROM gradle:jdk21
WORKDIR /
COPY . .
RUN gradle build
CMD ["java", "-cp", "build/libs/app.jar", "org.ecosyssimulator.Main"]
