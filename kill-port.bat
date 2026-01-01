@echo off
chcp 65001

echo Port Killer Too
echo ================================

:input
set /p port=Enter port number:

if "%port%"=="" goto input

echo Checking port %port%...
netstat -ano | findstr ":%port%"

if errorlevel 1 (
    echo Port %port% is not in use.
    goto ask_again
)

echo.
set /p confirm=Kill the process? (y/n): 

if /i "%confirm%"=="y" (
    for /f "tokens=5" %%i in ('netstat -ano ^| findstr ":%port%"') do (
        echo Killing process %%i...
        taskkill /f /pid %%i
    )
) else (
    echo Operation cancelled.
)

:ask_again
echo.
set /p continue=Continue? (y/n): 

if /i "%continue%"=="y" (
    echo.
    goto input
)

echo Done.
pause