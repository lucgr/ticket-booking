# Use an official Maven image to build the jar file
FROM maven:3.9-amazoncorretto-21-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Build the jar file
RUN mvn clean package

# Use an official OpenJDK runtime as a parent image
FROM amazoncorretto:21.0.6-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/reservationAPI-1.0.0.jar app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]