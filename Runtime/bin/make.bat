javac -classpath ..\..\Generator\lib\ojdbc14.jar;..\lib\commons-logging-1.1.1.jar;..\lib\commons-beanutils.jar -d ..\classes ..\src\plsql_bridge\PlsqlJavaBridgeExecutor.java

jar cvf ..\plsql-bridge-runtime.jar -C ..\classes .