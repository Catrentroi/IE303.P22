@echo off
echo ===================================
echo    SPRING BOOT SHOES API LAUNCHER
echo ===================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Checking for Maven in common locations...
    
    if exist "C:\tools\apache-maven-3.9.5\bin\mvn.cmd" (
        echo Found Maven at C:\tools\apache-maven-3.9.5\bin\mvn.cmd
        echo Using this installation...
        echo.
        set MVN_CMD=C:\tools\apache-maven-3.9.5\bin\mvn.cmd
    ) else if exist "C:\Program Files\apache-maven\bin\mvn.cmd" (
        echo Found Maven at C:\Program Files\apache-maven\bin\mvn.cmd
        echo Using this installation...
        echo.
        set MVN_CMD=C:\Program Files\apache-maven\bin\mvn.cmd
    ) else (
        echo Maven not found in common locations.
        echo Please install Maven and try again.
        goto :END
    )
) else (
    set MVN_CMD=mvn
)

echo Starting Spring Boot application...
echo Application will be available at: http://localhost:8080
echo.

cd /d "%~dp0"
%MVN_CMD% spring-boot:run

:END
pause
