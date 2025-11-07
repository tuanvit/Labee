@echo off
echo ========================================
echo Starting Labee Backend Server
echo ========================================
echo.
cd /d "%~dp0"
call mvnw.cmd spring-boot:run
