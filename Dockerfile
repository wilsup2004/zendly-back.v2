FROM maven:3.8.6-openjdk-17-slim
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/*.jar"]