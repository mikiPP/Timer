@echo off
echo Sonar execution script

REM Execute sonar
sonar-scanner -DWORKSPACE=%CD%

pause
