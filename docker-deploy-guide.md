# Docker 打包部署完整指南

## 环境准备

### 1. 安装Docker和Docker Compose

#### Windows系统
```bash
# 下载并安装 Docker Desktop
# 访问：https://docs.docker.com/desktop/install/windows-install/

# 验证安装
docker --version
docker-compose --version
```

#### Linux系统
```bash
# 安装Docker
curl -fsSL https://get.docker.com | sh
sudo usermod -aG docker $USER

# 安装Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 验证安装
docker --version
docker-compose --version
```

## 从零开始部署

### 1. 项目准备
确保项目根目录包含以下文件：
- `Dockerfile` - Docker镜像构建文件
- `docker-compose.yml` - 服务编排文件
- `pom.xml` - Maven配置文件
- `src/` - 源代码目录

### 2. 构建Docker镜像

#### 方式一：使用Docker Compose（推荐）
```bash
# 清理并构建所有服务
docker-compose down
docker-compose build --no-cache

# 查看构建的镜像
docker images | findstr demo
```

#### 方式二：单独构建应用镜像
```bash
# 构建应用镜像
docker build -t demo-app:latest .

# 查看镜像详情
docker image inspect demo-app:latest
```

### 3. 启动服务

```bash
# 启动所有服务（后台运行）
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看应用日志
docker-compose logs -f app

# 查看数据库日志
docker-compose logs -f postgres
```

### 4. 验证部署

```bash
# 检查应用健康状态
curl http://localhost:8080/api/actuator/health

# 测试API接口
curl http://localhost:8080/api/users/health

# 查看Swagger文档
# 浏览器访问：http://localhost:8080/api/swagger-ui/index.html
```

## Docker常用操作命令

### 容器管理
```bash
# 查看运行中的容器
docker ps

# 查看所有容器
docker ps -a

# 停止服务
docker-compose stop

# 重启服务
docker-compose restart

# 完全停止并清理
docker-compose down

# 进入容器内部
docker exec -it demo-app /bin/sh
```

### 镜像管理
```bash
# 查看所有镜像
docker images

# 删除镜像
docker rmi demo-app:latest

# 清理无用镜像
docker image prune
```

### 日志和监控
```bash
# 实时查看应用日志
docker logs -f demo-app

# 查看最近100行日志
docker logs --tail 100 demo-app

# 查看容器资源使用
docker stats demo-app

# 查看容器详细信息
docker inspect demo-app
```

## 生产环境部署

### 1. 环境变量配置
创建 `.env` 文件（生产环境）：
```env
# 数据库配置
POSTGRES_PASSWORD=your_secure_password
SPRING_DATASOURCE_PASSWORD=your_secure_password

# 应用配置
SPRING_PROFILES_ACTIVE=prod
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

### 2. 生产环境启动
```bash
# 使用环境变量文件启动
docker-compose --env-file .env up -d

# 或者直接设置环境变量
export POSTGRES_PASSWORD=your_secure_password
docker-compose up -d
```

### 3. 数据备份
```bash
# 备份数据库
docker exec demo-postgres pg_dump -U root javaDemo > backup.sql

# 恢复数据库
docker exec -i demo-postgres psql -U root javaDemo < backup.sql
```

## 故障排查

### 常见问题解决

#### 1. 端口冲突
```bash
# 检查端口占用
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Linux/Mac

# 修改端口（编辑docker-compose.yml）
# 将 "8080:8080" 改为 "8081:8080"
```

#### 2. 容器启动失败
```bash
# 查看详细错误信息
docker-compose logs app

# 重新构建并启动
docker-compose down
docker-compose build --no-cache
docker-compose up
```

#### 3. 数据库连接问题
```bash
# 检查数据库服务状态
docker-compose logs postgres

# 测试数据库连接
docker exec demo-postgres pg_isready -U root -d javaDemo
```

#### 4. 内存不足
```bash
# 查看容器资源限制
docker stats

# 增加Docker内存分配（Docker Desktop设置）
# 或添加内存限制到docker-compose.yml
```

## 自动化部署脚本

创建 `deploy.sh` 脚本：
```bash
#!/bin/bash

echo "开始部署Demo应用..."

# 停止现有服务
echo "停止现有服务..."
docker-compose down

# 清理旧镜像
echo "清理旧镜像..."
docker image prune -f

# 构建新镜像
echo "构建新镜像..."
docker-compose build --no-cache

# 启动服务
echo "启动服务..."
docker-compose up -d

# 等待服务就绪
echo "等待服务启动..."
sleep 30

# 健康检查
echo "执行健康检查..."
curl -f http://localhost:8080/api/actuator/health

if [ $? -eq 0 ]; then
    echo "✅ 部署成功！"
    echo "应用地址: http://localhost:8080"
    echo "Swagger文档: http://localhost:8080/api/swagger-ui/index.html"
else
    echo "❌ 部署失败，请检查日志"
    docker-compose logs app
fi
```

设置执行权限并运行：
```bash
chmod +x deploy.sh
./deploy.sh
```

## 性能优化建议

### 1. 镜像优化
- 使用多阶段构建减少镜像大小
- 使用Alpine基础镜像
- 清理构建缓存

### 2. 资源配置
```yaml
# 在docker-compose.yml中添加资源限制
services:
  app:
    deploy:
      resources:
        limits:
          memory: 1G
          cpus: '1.0'
        reservations:
          memory: 512M
          cpus: '0.5'
```

### 3. 日志管理
```bash
# 配置日志轮转
docker run --log-opt max-size=10m --log-opt max-file=3 demo-app
```

## 总结

通过以上步骤，您可以：
1. ✅ 从零开始搭建Docker环境
2. ✅ 构建和部署Spring Boot应用
3. ✅ 配置数据库服务
4. ✅ 实现健康检查和监控
5. ✅ 掌握生产环境部署技巧

现在您可以开始使用Docker来部署和管理您的应用了！