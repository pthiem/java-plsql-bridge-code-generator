
rem Set environment variable for oracle jdbc library 
rem This environment varaible is used by the generator program.
rem Modify as required.
set CLASSPATH_PREFIX=C:\Users\%username%\.m2\repository\com\oracle\ojdbc6\11.2.0.3\ojdbc6-11.2.0.3.jar

rem Set path to generator program. For the quickstart, it is in the generator directory.
set GENERATOR_BAT=%~dp0\java-plsql-bridge-code-generator-1.0-bin\generator\bin\plsql-java-bridge-generator.bat

rem Run generator
pushd %~dp0
call %GENERATOR_BAT% CodeGenerator.properties core pkg_plsql_bridge_test
popd
