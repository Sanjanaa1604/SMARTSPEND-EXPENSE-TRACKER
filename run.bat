@echo off
setlocal EnableExtensions

echo.
echo ==========================================
echo          SMARTSPEND - JAVA FX APP
echo ==========================================
echo.

REM ==========================================
REM PROJECT FOLDER
REM ==========================================

set "PROJECT_DIR=%~dp0"

echo Project folder:
echo %PROJECT_DIR%
echo.

REM ==========================================
REM CHECK JAVA
REM ==========================================

echo Checking Java...

where java >nul 2>&1
if errorlevel 1 (
    echo.
    echo ERROR: Java was not found in PATH.
    echo.
    pause
    exit /b 1
)

where javac >nul 2>&1
if errorlevel 1 (
    echo.
    echo ERROR: javac was not found in PATH.
    echo.
    pause
    exit /b 1
)

echo Java detected:
java -version
echo.

REM ==========================================
REM JAVAFX
REM ==========================================

set "FX=%PROJECT_DIR%lib\javafx-sdk-21.0.11\lib"

echo Checking JavaFX...
echo Location:
echo %FX%
echo.

if not exist "%FX%\javafx.controls.jar" (
    echo.
    echo ERROR: JavaFX was not found.
    echo.
    pause
    exit /b 1
)

echo JavaFX 21.0.11 found!
echo.

REM ==========================================
REM MYSQL CONNECTOR
REM ==========================================

echo Checking MySQL Connector/J...

set "MYSQL_JAR="

for %%J in ("%PROJECT_DIR%lib\mysql-connector-j-*.jar") do (
    if exist "%%~fJ" (
        set "MYSQL_JAR=%%~fJ"
    )
)

if not defined MYSQL_JAR (
    echo.
    echo ERROR: MySQL Connector/J was not found.
    echo.
    echo Please check:
    echo %PROJECT_DIR%lib
    echo.
    pause
    exit /b 1
)

echo MySQL Connector/J found:
echo %MYSQL_JAR%
echo.

REM ==========================================
REM BUILD FOLDER
REM ==========================================

echo Cleaning previous build...

if exist "%PROJECT_DIR%out" (
    rmdir /s /q "%PROJECT_DIR%out"
)

mkdir "%PROJECT_DIR%out"

REM ==========================================
REM COPY RESOURCES
REM ==========================================

echo Copying resources...

if exist "%PROJECT_DIR%resources" (
    xcopy /E /I /Q "%PROJECT_DIR%resources" "%PROJECT_DIR%out" >nul
)

REM ==========================================
REM COMPILE SMARTSPEND
REM ==========================================

echo.
echo ==========================================
echo       COMPILING SMARTSPEND
echo ==========================================
echo.

javac ^
--module-path "%FX%" ^
--add-modules javafx.controls,javafx.fxml ^
-cp "%MYSQL_JAR%" ^
-sourcepath "%PROJECT_DIR%src" ^
-d "%PROJECT_DIR%out" ^
"%PROJECT_DIR%src\com\smartspend\Main.java"

if errorlevel 1 (
    echo.
    echo ==========================================
    echo        COMPILATION FAILED
    echo ==========================================
    echo.
    pause
    exit /b 1
)

echo.
echo ==========================================
echo      COMPILATION SUCCESSFUL!
echo ==========================================
echo.

REM ==========================================
REM RUN SMARTSPEND
REM ==========================================

echo Starting SmartSpend...
echo.

java ^
--module-path "%FX%" ^
--add-modules javafx.controls,javafx.fxml ^
-cp "%PROJECT_DIR%out;%MYSQL_JAR%" ^
com.smartspend.Main

if errorlevel 1 (
    echo.
    echo ==========================================
    echo       SMARTSPEND RUNTIME ERROR
    echo ==========================================
    echo.
    pause
)

endlocal