@echo off
echo ========================================
echo   DigiWallet Frontend Server
echo ========================================
echo.

cd /d "%~dp0"

echo Starting lightweight HTTP server...
echo Frontend will be available at: http://localhost:3000
echo Backend should be running at: http://localhost:8080
echo.
echo Press Ctrl+C to stop the server
echo.

REM Start Python HTTP server on port 3000
python -m http.server 3000
