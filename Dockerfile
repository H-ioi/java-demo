# 多阶段构建：构建阶段
FROM maven:3.9.6-amazoncorretto-21 AS build

# 设置工作目录
WORKDIR /app

# 复制pom.xml文件
COPY pom.xml .

# 下载依赖（利用Docker缓存层）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 打包应用（跳过测试）
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:21-slim

# 安装必要的工具
RUN apt-get update && apt-get install -y curl tzdata && rm -rf /var/lib/apt/lists/*

# 设置时区
ENV TZ=Asia/Shanghai

# 创建非root用户
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

# 设置工作目录
WORKDIR /app

# 从构建阶段复制jar包
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# 启动应用（默认使用本地配置）
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=local"]