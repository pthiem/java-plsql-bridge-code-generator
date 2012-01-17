

del ..\java-plsql-bridge-code-generator-X.X.zip

cd ..\..\Generator\bin

call make.bat

cd ..\..\Runtime\bin

call make.bat

cd %~dp0

"c:\Program Files\7-Zip\7z.exe" ^
 -x!java-plsql-bridge-code-generator\Release ^
 -x!java-plsql-bridge-code-generator\.git ^
 -x!java-plsql-bridge-code-generator\Demo\bin\CodeGenerator.properties ^
 -x!java-plsql-bridge-code-generator\Demo\src\plsql_bridge.properties ^
 -x!java-plsql-bridge-code-generator\Demo\classes\* ^
 a ^
 ..\java-plsql-bridge-code-generator-X.X.zip ^
 ..\..\..\java-plsql-bridge-code-generator

 