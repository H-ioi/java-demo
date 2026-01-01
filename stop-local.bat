@echo off
chcp 65001 >nul

echo ============================================
echo         本地应用停止脚本
echo ============================================

:: 首先检查当前进程状态
echo.
echo [1/3] 检查当前Java进程状态...
for /f "tokens=2" %%i in ('tasklist ^| findstr /i "java.exe javaw.exe"') do (
    echo 发现Java进程: %%i
)

:: 停止所有Java相关进程
echo.
echo [2/3] 停止Java进程...
set /a stopped_count=0

:: 停止java.exe进程
for /f "tokens=2" %%i in ('tasklist ^| findstr /i "java.exe"') do (
    echo 停止进程: java.exe (PID: %%i)
    taskkill /F /PID %%i >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ java.exe (PID: %%i) 已停止
        set /a stopped_count+=1
    ) else (
        echo ❌ 无法停止 java.exe (PID: %%i)
    )
)

:: 停止javaw.exe进程
for /f "tokens=2" %%i in ('tasklist ^| findstr /i "javaw.exe"') do (
    echo 停止进程: javaw.exe (PID: %%i)
    taskkill /F /PID %%i >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ javaw.exe (PID: %%i) 已停止
        set /a stopped_count+=1
    ) else (
        echo ❌ 无法停止 javaw.exe (PID: %%i)
    )
)

:: 等待1秒让进程完全停止
timeout /t 1 /nobreak >nul

:: 检查端口占用情况
echo.
echo [3/3] 检查端口9000占用...
set /a port_freed=0
for /f "tokens=5" %%i in ('netstat -ano ^| findstr ":9000"') do (
    echo 端口9000被进程PID: %%i 占用
    taskkill /F /PID %%i >nul 2>&1
    if !errorlevel! equ 0 (
        echo ✅ 进程 %%i 已停止，端口已释放
        set /a port_freed+=1
    ) else (
        echo ❌ 无法停止进程 %%i
    )
)

:: 最终状态检查
echo.
echo ============================================
echo             停止结果报告
echo ============================================

:: 检查是否还有Java进程
tasklist | findstr /i "java.exe javaw.exe" >nul
if %errorlevel% equ 0 (
    echo ❌ 仍有Java进程在运行
) else (
    echo ✅ 所有Java进程已停止
)

:: 检查端口是否释放
netstat -ano | findstr ":9000" >nul
if %errorlevel% equ 0 (
    echo ❌ 端口9000仍被占用
) else (
    echo ✅ 端口9000已释放
)

echo.
echo 总结：
if %stopped_count% gtr 0 echo 已停止 %stopped_count% 个Java进程
if %port_freed% gtr 0 echo 已释放 %port_freed% 个端口占用

echo.
echo 下次启动命令：
echo java -jar target\demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=local

echo.
pause