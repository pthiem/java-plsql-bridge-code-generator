Welcome to the Java-PLSQL-Bridge-Code-Generator wiki!

<a name="what_is_it"/>
# What Is It?

A tool to help calling PL/SQL stored procedures from Java procedures easily. 

Code Generation of a PL/SQL data access layer (aka DAO), and more, for Java applications.

Generate all the JDBC from PL/SQL package specifications, and get on with making your application awesome.

<a name="toc"/>
# Table of Contents

* [Table of Contents](#toc)
* [What Is It?](#what_is_it)
* [Download Now](#download_now)
* [Features](#features)
* [The Problem](#the_problem)
* [The Solution](#the_solution)
* [Example Developer Cycle](#example_developer_cycle)
* [ Run the Demo](#run_the_demo)
* [Integrating with a EJB server (OC4J 10.1.3.5.0 in particular)](#integrating_oc4j_101350)
* [Integrating with a other EJB environments](#integrating_ejb_other)
* [Integrating with a Spring](#integrating_spring)
* [Integrating with a Web Service](#integrating_web_service)
* [About](#about)
* [Limitations](#limitations)
* [Best Practices](#best_practises)
* [Alternatives](#alternatives)
* [TODO](#TODO)

<a name="download_now">
# Download Now
[java-plsql-bridge-code-generator-0.1.zip](https://github.com/downloads/pthiem/java-plsql-bridge-code-generator/java-plsql-bridge-code-generator-0.1.zip)

<a name="features"/>
# Features
* Generate JDBC accessing code based on your existing PL\SQL packages.
* Generated code is typed and as with getters and setters based on argument names.
* Uses simple common types.
* Converts cursors to lists.
* Can be integrated into your runtime transacational system.
* No more JDBC code writing.
* Return typed cursors as typed Java objects.
* Returns untyped cursors as Maps of objects.
* Handles most common PL\SQL types.

<a name="the_problem"/>
# The Problem

Your write tons of JDBC code, or the like just so you can move your data between your Java application layer and your PL\SQL stored procedure layer. But it is dumb, cut and paste code, that is the same every time except for the procedure name and the parameters.

<a name="the_solution"/>
# The Solution

Use a code generator to generate the JDBC code for you.

You write your PL\SQL package.

You generate the JDBC code.

You write Java code to call the generated code

<a name="example_developer_cycle"/>
# Example Developer Cycle

Create new PL\SQL package with procedures in your database, e.g. 

    create package pkg_hello_world as
    ...
    procedure p_hello_world(a_name in varchar2, a_message out varchar2)
    ...

Create DOS script to generate source, e.g. generate_pkg_hello_world.bat:

    call "%BRIDGE_HOME%\Generator\bin\Generate_Code.bat" CodeGenerator.properties core pkg_hello_world
    
Write Code to call the generated code.
    
    // Get EJB, for example
    Pkg_hello_world pkg_hello_world = (Pkg_hello_world) new InitialContext().lookup("ejb/Pkg_hello_world");

    // Prepare arguments
    P_hello_world_arguments arguments = new P_hello_world_arguments();
    arguments.setA_name("Joe Bloggs");

    // Do call
    pkg_hello_world.p_hello_world(arguments);

    // Use results
    System.out.println(arguments.getA_message());

<a name="run_the_demo"/>
# Run the Demo

This demo runs as standalone (plain JDBC, no EJB, no Spring)

Extract zip project to a directory (here after referred to as ...).

Edit ...\java-plsql-bridge-code-generator\Demo\src\plsql_bridge.properties.example:

* Save as plsql_bridge.properties
* Add database YOUR_HOST and YOUR_SID to property jdbcURL.
    
Edit ...\java-plsql-bridge-code-generator\Demo\bin\CodeGenerator.properties.example:

* Save as CodeGenerator.properties
* Add database YOUR_HOST and YOUR_SID to property jdbcURL.
    
Setup your database by running the following scripts:

    ...\java-plsql-bridge-code-generator\Demo\sql\plsql_bridge_test_table.sql
    ...\java-plsql-bridge-code-generator\Demo\sql\pkg_plsql_bridge_test.pks
    ...\java-plsql-bridge-code-generator\Demo\sql\pkg_plsql_bridge_test.pkb
    
Generate the code:

    > cd ...\java-plsql-bridge-code-generator\Demo\bin
    > generate_pkg_plsql_bridge_test.bat
    
    > call "..\..\java-plsql-bridge-code-generator\Generator\bin\Generate_Code.bat" CodeGenerator.properties core pkg_plsql_bridge_test
    
    > java -classpath "C:\java-plsql-bridge-code-generator\Generator\bin\..\classes;C:\java-plsql-bridge-code-generator\Generator\bin\..\lib\ojdbc14.jar" plsql_bridge.PlsqlJavaBridgeGenerator "C:\java-plsql-bridge-code-generator\Generator\bin\.." "CodeGenerator.properties" "core" "pkg_plsql_bridge_test"
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/package_metadata.xml
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/Pkg_plsql_bridge_test.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/Pkg_plsql_bridge_test_impl.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_get_cursor_recordtype_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_get_cursor_rowtype_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_get_cursor_untyped_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_blob_simple_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_clob_simple_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_date_simple_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_float_simple_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_number_decimal_simple_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_number_in_out_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_number_simple_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_numeric_simple_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_types_in_simple_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_types_out_simple_arguments.java
    Generating ../src/plsql_bridge/pkg_plsql_bridge_test/P_test_varchar2_simple_arguments.java
    >   
Compile the generated code:

    > make_demo.bat

    > javac -classpath ..\lib\plsql-bridge-runtime.jar;..\lib\commons-logging-1.1.1.jar;..\classes\ -d ..\classes ..\src\demo\*.java ..\src\plsql_bridge\pkg_plsql_bridge_test\*.java

    > copy ..\src\*.properties ..\classes
    ..\src\plsql_bridge.properties
            1 file(s) copied.

    > copy ..\src\plsql_bridge\pkg_plsql_bridge_test\*.xml ..\classes\plsql_bridge\pkg_plsql_bridge_test
    ..\src\plsql_bridge\pkg_plsql_bridge_test\package_metadata.xml
            1 file(s) copied.
    >   
Run the generated code:

    > run_demo.bat

    > java -classpath ..\lib\plsql-bridge-runtime.jar;..\lib\ojdbc14.jar;..\lib\commons-beanutils.jar;..\lib\commons-collections.jar;..\lib\commons-logging-1.1.1.jar;..\classes test.MainDemo
    Cursor typed with recordtype.
    1:2123,asfsdf23 afdafr erwe rwerwr er w
    1:2123,asfsdf23 afdafr erwe rwerwr er w
    Cursor typed with rowtype.
    2:2123,asfsdf23 afdafr erwe rwerwr er w
    2:2123,asfsdf23 afdafr erwe rwerwr er w
    Cursor, untyped.
    3:{fld_number_float_null=null...
    ...
    Test, in out fields.
    6:null,1001,null,2002
    6:,,901,902

    >

<a name="integrating_oc4j_101350"/>     
# Integrating with a EJB server (OC4J 10.1.3.5.0 in particular)

Generated annotations only supports OC4J 10.1.3.5.0 at present.

Jars

* Include **plsql-bridge-runtime.jar** in your project.
* Also include some recent versions of commons-beanutils.jar, commons-collections.jar, commons-logging-1.1.1.jar.
* Also include an Oracle JDBC driver, i.e. ojdbc14.jar.

Create a directory to contain the batch scripts to generate code.

In that directory create CodeGenerator.properties.

    #
    # Connection information
    #
    jdbcURL = jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS_LIST = (ADDRESS = (COMMUNITY = tcp.world) (PROTOCOL = TCP) (Host = <YOUR HOST>) (Port = 1521))) (CONNECT_DATA = (SID = <YOUR_SID>)))
    jdbcUser = <YOUR USERNAME>
    jdbcPassword = <YOUR PASSWORD>

    # Output for generated source
    resultDir = ../src
    
    #
    # Target Platform
    #
    # Use to subclass generated objects to add your own annotations (transaction attributes, security, etc)
    # Default default
    # Options: oc4j1013, default
    #    oc4j1013 - annotations, for oc4j10.1.3.5.0 EJB 3.0
    #    default - no annotations, can be run with plain jdbc
    
    targetPlatform=oc4j1013

In that directory create generate_\<YOUR_PACKAGE\>.bat for each package

    call "%BRIDGE_HOME%\Generator\bin\Generate_Code.bat" CodeGenerator.properties core <YOUR_PACKAGE>

Generate JAVA JDBC code for you package

    Run generate_<YOUR_PACKAGE>.bat

Create and configure your_project/src/plsql_bridge.properties 

    #
    # Use if you want to extend (aka make a subclass) the executor to get / close your own connection datasource.
    #
    PlsqlJavaBridgeExecutorImplClass=plsql_bridge.MyPlsqlJavaBridgeExecutorImpl

Implement plsql_bridge.MyPlsqlJavaBridgeExecutorImpl 

    // Extend the PlsqlJavaBridgeExecutor class.
    // Get specific data source.
        
    package plsql_bridge;

    import java.sql.SQLException;
    import javax.naming.InitialContext;
    import javax.sql.DataSource;
    import plsql_bridge.PlsqlJavaBridgeExecutor;

    public class MyPlsqlJavaBridgeExecutor extends PlsqlJavaBridgeExecutor {

        public MyPlsqlJavaBridgeExecutor() {
        }

        protected DataSource getDataSource() throws Exception {
            InitialContext ic = new InitialContext();
            return (DataSource) ic.lookup("jdbc/MY_DATASOURCE");
        }

        public void execute(String string, String string1, Object object) throws Exception {
            try {
                super.execute(string, string1, object);
            } catch (SQLException e) {
                throw e;
            }
        }
        
    }

Write code to call your package.

Compile your source.

Execute your code.

<a name="integrating_ejb_other"/>
# Integrating with a other EJB environments

* Not yet implemented.
* You can help!
* Need to improve templates (annotation usage) and for new target platform option.
* Need to subclass the executor to get the data source correctly.
* Need to handle exceptions correctly.

<a name="integrating_spring"/>
# Integrating with a Spring

* Not yet implemented.
* You can help!
* Need to improve templates (annotation usage) and for new target platform option.
* Need to subclass the executor to get the data source correctly.
* Need to handle exceptions correctly.

<a name="integrating_web_service"/>
# Integrating with a Web Service

* Not yet implemented.
* You can help!
* Need to improve templates (annotation usage) and for new target platform option.
* Need to subclass the executor to get the data source correctly.
* Need to handle exceptions correctly.

<a name="about"/>
# About

This project stems from the need to interface PL\SQL stored procedures regularly from OC4J 10.1.3.5.0 applications.

Created June 2011.

<a name="limitations"/>
# Limitations

Out of the box for support only:

1. For plain JDBC connections 
1. OC4J 10.1.3.5.0 EJB 3.0 annotations

User must extend the Executor for custom connection management.

Deep typing of arguments in stored procedures not handled.

Overloaded stored procedures not handled.

Only common PL\SQL types not handled.

<a name="licence"/>
# Licence

Free and unrestricted.

<a name="best_practises"/>
# Best Practices

* Subclass the executor to get the correct data source and handle exceptions.
* Use simple types.
* Use typed cursors.
* Where specific transactional or security annotations are required, use the "abstract" configuration setting, and extend the generated classes and add your required annocations.
* Where this tool does not handle your PLSQL, dont try to work around, just use plain old JDBC.

<a name="alternatives"/>
# Alternatives

* [Oracle JPublisher] (http://www.google.com.au/search?q=jpublisher+11)
* [JDBC Wizard (Formerly OrindaBuild)] (http://www.orindasoft.com)

<a name="TODO"/>
# TODO

* Please help!!!
* Documentation.
* Unit tests.
* Support annotations for Spring transactions.
* Support annotations for standard EJB 3.0.
* Support annotations for web services EJB 3.0.
* Improve type support.
* Document type support.
* Configuration for different names for java versions of packages and procedures, ie underscore removal, camel case.
* Constructor for arguments with parameter.