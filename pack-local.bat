@echo off
chcp 65001 >nul

echo 开始本地打包...

:: 清理并构建项目
echo 清理并构建项目...
mvn clean compile

:: 运行测试
echo 运行测试...
mvn test

:: 打包应用（跳过测试，使用本地配置）
echo 打包应用...
mvn package -DskipTests

:: 检查构建结果
if exist "target\demo-0.0.1-SNAPSHOT.jar" (
    echo ✅ 打包成功！
    echo 打包文件位置: target\demo-0.0.1-SNAPSHOT.jar
    echo.
    echo 启动命令:
    echo java -jar target\demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
    echo.
    echo 或者使用Docker运行:
    echo docker build -t demo-app-local .
    echo docker run -p 9000:9000 --network="host" demo-app-local
) else (
    echo ❌ 打包失败！
    exit /b 1
)