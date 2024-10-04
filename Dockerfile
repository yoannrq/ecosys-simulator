FROM gradle:jdk21
WORKDIR /app
COPY . .
RUN cd app && gradle build
CMD ["java", "-cp", "build/libs/app.jar", "org.ecosyssimulator.Main"]
