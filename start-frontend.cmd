@echo off
cd /d "%~dp0\frontend"
echo Starting DigiWallet Frontend...
powershell -ExecutionPolicy Bypass -File http-server.ps1
