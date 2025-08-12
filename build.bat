@echo off
REM === Create bin directory if it doesn't exist ===
if not exist bin mkdir bin

REM === Compile all Java files ===
echo Compiling Java files...
for /R src %%f in (*.java) do (
    javac -cp "lib\mysql-connector-j-8.3.0.jar;src" -d bin "%%f"
)
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

REM === Run the application ===
echo Running application...
java -cp "bin;lib\mysql-connector-j-8.3.0.jar" ui.MainApp
pause
