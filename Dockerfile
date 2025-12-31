# 构建阶段
FROM amazoncorretto:21-alpine3.20 AS build
RUN apk add --no-cache gradle
WORKDIR /app
COPY build.gradle settings.gradle gradle/ ./
COPY src ./src
RUN gradle build --no-daemon

# 运行阶段
FROM amazoncorretto:21-alpine3.20
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]