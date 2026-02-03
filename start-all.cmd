@echo off
echo ========================================
echo   DigiWallet Application Launcher
echo ========================================
echo.
echo This script will start both backend and frontend servers
echo.
echo Backend (Spring Boot): http://localhost:8080
echo Frontend (Python HTTP): http://localhost:3000
echo.
pause

REM Start backend in new window
echo Starting Backend Server...
start "DigiWallet Backend" cmd /k "cd /d %~dp0 && mvnw.cmd spring-boot:run"

REM Wait a bit for backend to initialize
timeout /t 5 /nobreak > nul

REM Start frontend in new window (using Python HTTP server)
echo Starting Frontend Server...
start "DigiWallet Frontend" cmd /k "cd /d %~dp0\frontend && python -m http.server 3000"

echo.
echo Both servers are starting...
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo Swagger UI: http://localhost:8080/swagger-ui.html
echo.
echo Press any key to exit this launcher (servers will continue running)
pause > nul
