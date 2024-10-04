FROM gradle:jdk21
WORKDIR /
COPY . .
RUN gradle build
CMD ["java", "-cp", "build/libs/ecosys-simulator.jar", "org.ecosyssimulator.Main"]
