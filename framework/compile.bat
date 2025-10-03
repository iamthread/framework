@echo off
echo Compiling Mini Framework...

REM Create output directory
if not exist "target\classes" mkdir target\classes

REM Compile main source files
javac -cp "target\classes" -d target\classes src\main\java\com\example\minidiframework\annotation\*.java
javac -cp "target\classes" -d target\classes src\main\java\com\example\minidiframework\context\*.java
javac -cp "target\classes" -d target\classes src\main\java\com\example\minidiframework\scanner\*.java
javac -cp "target\classes" -d target\classes src\main\java\com\example\minidiframework\injection\*.java
javac -cp "target\classes" -d target\classes src\main\java\com\example\demo\model\*.java
javac -cp "target\classes" -d target\classes src\main\java\com\example\demo\repository\*.java
javac -cp "target\classes" -d target\classes src\main\java\com\example\demo\service\*.java
javac -cp "target\classes" -d target\classes src\main\java\com\example\demo\controller\*.java
javac -cp "target\classes" -d target\classes src\main\java\com\example\demo\*.java

echo Compilation completed!
echo.
echo To run the demo application:
echo java -cp target\classes com.example.demo.DemoApplication
