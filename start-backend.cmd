@echo off
cd /d "%~dp0"
echo Starting DigiWallet Backend...
java -jar target\DigiWallet-0.0.1-SNAPSHOT.jar > logs\backend.log 2> logs\backend-error.log
