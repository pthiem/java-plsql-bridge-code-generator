javac -classpath ..\lib\plsql-bridge-runtime.jar;..\lib\commons-logging-1.1.1.jar;..\classes\ -d ..\classes ..\src\demo\*.java ..\src\plsql_bridge\pkg_plsql_bridge_test\*.java
copy ..\src\*.properties ..\classes 
copy ..\src\plsql_bridge\pkg_plsql_bridge_test\*.xml ..\classes\plsql_bridge\pkg_plsql_bridge_test

