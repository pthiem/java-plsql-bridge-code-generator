

del ..\java_plsql-bridge_code_generator-X.X.zip

cd ..\..\Generator\bin

call make.bat

cd ..\..\Runtime\bin

call make.bat

cd %~dp0

"c:\Program Files\7-Zip\7z.exe" ^
 -x!java_plsql_bridge_code_generator\Release ^
 -x!java_plsql_bridge_code_generator\.git ^
 -x!java_plsql_bridge_code_generator\Demo\bin\CodeGenerator.properties ^
 -x!java_plsql_bridge_code_generator\Demo\src\plsql_bridge.properties ^
 -x!java_plsql_bridge_code_generator\Demo\classes\* ^
 a ^
 ..\java_plsql_bridge_code_generator-X.X.zip ^
 ..\..\..\java_plsql_bridge_code_generator

 