FROM eclipse-temurin:25-alpine-3.21 AS runtime
RUN jlink \
    --output /mini-runtime \
    --add-modules java.base,java.desktop,java.instrument,java.management,java.logging,java.naming,java.security.jgss,jdk.management,java.sql,jdk.unsupported \
    --strip-debug \
    --compress=2 \
    --no-header-files \
    --no-man-pages

FROM maven:3.9.11-eclipse-temurin-25-alpine AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn -DskipTests clean package

FROM alpine:3.21
WORKDIR /opt/app
COPY --from=runtime /mini-runtime /mini-runtime
COPY --from=builder /build/target/admin-bot-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["/mini-runtime/bin/java", "-jar", "app.jar"]