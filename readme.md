Welcome to the Java-PLSQL-Bridge-Code-Generator wiki!

<a name="what_is_it"/>
# What Is It?

A tool to help calling PL/SQL stored procedures from Java procedures easily. 

Code Generation of a PL/SQL data access layer (aka DAO), and more, for Java applications.

Generate all the JDBC from PL/SQL package specifications, and get on with making your application awesome.

Project URLs:

* [Readme](https://github.com/pthiem/java-plsql-bridge-code-generator/blob/master/readme.md)
* [Github](https://github.com/pthiem/java-plsql-bridge-code-generator)
* [Issues](https://github.com/pthiem/java-plsql-bridge-code-generator/issues)
* [Releases](https://github.com/pthiem/java-plsql-bridge-code-generator/releases)

<a name="toc"/>
# Table of Contents

* [Table of Contents](#toc)
* [What Is It?](#what_is_it)
* [Download Now](#download_now)
* [Features](#features)
* [The Problem](#the_problem)
* [The Solution](#the_solution)
* [Example Developer Cycle](#example_developer_cycle)
* [Run the Quickstart](#run_the_quickstart)
* [Integrating with Spring](#integrating_spring)
* [Integrating with a Web Service](#integrating_web_service)
* [Integrating with REST and JavaScript](#code_generation_for_rest_and_javascript)
* [About](#about)
* [Limitations](#limitations)
* [Best Practices](#best_practises)
* [Alternatives](#alternatives)
* [TODO](#TODO)

<a name="download_now">
# Download Now
[java-plsql-bridge-code-generator-1.0-bin.zip](https://github.com/pthiem/java-plsql-bridge-code-generator/releases/download/1.0/java-plsql-bridge-code-generator-1.0-bin.zip)

<a name="features"/>
# Features
* Generate JDBC accessing code based on your existing PL/SQL packages.
* Generated code is typed and as with getters and setters based on argument names.
* Uses simple common types.
* Converts cursors to lists.
* Can be integrated into your runtime transactional system.
* No more JDBC code writing.
* Return typed cursors as typed Java objects.
* Returns untyped cursors as Maps of objects.
* Handles most common PL/SQL types.

<a name="the_problem"/>
# The Problem

Your write tons of JDBC code, or the like just so you can move your data between your Java application layer and your PL/SQL stored procedure layer. 

But it is repetitive, cut and paste code, that is the same every time except for the procedure name and the parameters.

<a name="the_solution"/>
# The Solution

Use a code generator to generate the JDBC code for you.

You write your PL/SQL package.

You generate the JDBC code.

You write Java code to call the generated code

<a name="example_developer_cycle"/>
# Example Developer Cycle

1. Create new PL/SQL package with procedures in your database, e.g. 

        create package pkg_hello_world as
        ...
        procedure p_hello_world(a_name in varchar2, a_message out varchar2)
        ...

2. Create DOS script to generate source, e.g. generate\_pkg\_hello_world.bat:

        call "%GENERATOR_HOME%\bin\PlsqlJavaBridgeGenerator.bat" CodeGenerator.properties core pkg_hello_world
    
3. Write Code to call the generated code.
    
        // Get EJB, for example
        @EJB
        Pkg_hello_world pkg_hello_world;

        // Prepare arguments
        P_hello_world_arguments arguments = new P_hello_world_arguments();
        arguments.setA_name("Joe Bloggs");
        
        // Do call
        pkg_hello_world.p_hello_world(arguments);
        
        // Use results
        System.out.println(arguments.getA_message());

<a name="run_the_quickstart"/>
# Run the Quickstart

This demo can be run as a standalone program, junit and web application (with EJB).

Dependent on Maven.

Extract zip project to a directory (here after referred to as ...).

1.  Setup DB user for demo (this quickstart likes to run as user core with password core):

        create user core identified by core;
        grant connect to core;
        grant dba to core;

2. Setup DB configuration for database setup and migration:

    * Edit ...\quickstart\pom.xml
        * Make changes in the the liquibase plugin (liquibase-maven-plugin), edit the properties
            * Set database JDBC URL.
            * Set username.
            * Set password.
        * Update Oracle JDBC library dependency
            * Find dependency for com.oracle - ojdbc6.
            * Update maven coordinates appropriate to your environment.

3. Setup your database user by running the following scripts:

        cd ...\quickstart
        mvn liquibase:update

4. Setup DB configuration for the PLSQL DAO Code Generator:
    
    * Edit ...\quickstart\generate\CodeGenerator.properties
        * Set database JDBC URL.
        * Set username.
        * Set password.
    * Edit ...\quickstart\generate\generate\_pkg\_plsql\_bridge_test.bat
        * Set CLASSPATH_PREFIX to contain path to your local Oracle JDBC driver.

5. Setup DB configuration for the runtime plsql bridge component.

    * Edit ...\quickstart\src\main\resources\plsql_bridge.properties:
        * Set database JDBC URL.
        * Set username.
        * Set password.
    
6. Install the runtime Jar into your local repository

        cd ...\quickstart\generate\java-plsql-bridge-code-generator-1.0-bin
        mvn org.apache.maven.plugins:maven-install-plugin:2.5.1:install-file -Dfile=jpbcg-runtime-1.0.jar
        
    Note explicitly using version 2.5.1 of the maven-install-plugin so that it will use the pom in the jar to configure the installation. 

7. Generate the PL/SQL DAO code:

    * This quickstart comes with pre-generated code.
    * The generated code is in
        * ...\quickstart\src\main\java\plsql\_bridge\pkg\_plsql\_bridge\_test (Java files)
        * ...\quickstart\src\main\resources\plsql\_bridge\pkg\_plsql\_bridge\_test (XML configuration file)
    * A later step will regenerate the code.
 
8. Run the quickstart code (via JUnit):

        cd ...\quickstart
        mvn test

    This will compile the code and execute JUnit to run the code.
    
    It will output something like this during its execution:
    
        <snip>
        -------------------------------------------------------
         T E S T S
        -------------------------------------------------------
        Running quickstart.MainTest
        Cursor typed with recordtype.
        1:2123,asfsdf23 afdafr erwe rwerwr er w
        1:2123,asfsdf23 afdafr erwe rwerwr er w
        Cursor typed with rowtype.
        2:2123,asfsdf23 afdafr erwe rwerwr er w
        2:2123,asfsdf23 afdafr erwe rwerwr er w
        Cursor, untyped.
        3:{fld_float=23.3432342, fld_number_float_null=null, fld_numeric_float_null=null, fld_double_null=null, fld_blob_null=null, fld_date=2011-08-16 17:30:12.0, fld_blob=null, fld_clob_null=null, fld_clob=asfsdf23 afdafr erwe rwerwr er w, fld_char_null=null, fld_numeric_null=null, fld_varchar2=asfsdf23 afdafr erwe rwerwr er w, fld_float_null=null, fld_varchar2_null=null, fld_number_value=2123, fld_date_null=null, fld_char=Y, fld_double=23.3432342, fld_number_float=121.23, fld_numeric_float_value=232.
        33, fld_number_null=null, fld_numeric_value=123}
        3:{fld_float=23.3432342, fld_number_float_null=null, fld_numeric_float_null=null, fld_double_null=null, fld_blob_null=null, fld_date=2011-08-16 17:30:12.0, fld_blob=null, fld_clob_null=null, fld_clob=asfsdf23 afdafr erwe rwerwr er w, fld_char_null=null, fld_numeric_null=null, fld_varchar2=asfsdf23 afdafr erwe rwerwr er w, fld_float_null=null, fld_varchar2_null=null, fld_number_value=2123, fld_date_null=null, fld_char=Y, fld_double=23.3432342, fld_number_float=121.23, fld_numeric_float_value=232.
        33, fld_number_null=null, fld_numeric_value=123}
        Test, simple inputs.
        4:,12,343.34,,,343,102.233,,55.34,,Hello,,31/DEC/13,,A,,
        Test, simple outputs.
        5:asdfa wer vb,2013-12-31 09:11:43.0,343.345,998.22,5444.23,343,343,Y
        Test, in out fields.
        6:null,1001,null,2002
        6:,,901,902
        Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.352 sec
        <snip>     

8. Run the quickstart (via Console):

    * Import the project into Eclipse or other IDE.
    * Run class quickstart.Main.
    * Console output should be similar to the test output.

9. Run the code (via Web application):

    * Import the project into Eclipse or other IDE.
    * Deploy the project to JBoss or similar container.
    * Execute page http://localhost:8080/jpbcg-quicstart
    * Ouput should be similar to the test output.

10. Regenerate the code:

        cd ...\quickstart
        generate\generate_pkg_plsql_bridge_test.bat
        
    Output should be similar to:
    
        <snip>
        Generating ../src/main/resources/plsql_bridge/pkg_plsql_bridge_test/package_metadata.xml
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/Pkg_plsql_bridge_test.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_get_cursor_recordtype_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_get_cursor_rowtype_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_get_cursor_untyped_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_blob_simple_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_clob_simple_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_date_simple_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_float_simple_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_number_decimal_simple_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_number_in_out_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_number_simple_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_numeric_simple_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_types_in_simple_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_types_out_simple_arguments.java
        Generating ../src/main/java/plsql_bridge/pkg_plsql_bridge_test/P_test_varchar2_simple_arguments.java
        <snip>    

11. Alter the packages:

    * Add new method to ...\quickstart\sql\pkg\_plsql\_bridge\_test.pks
    * Add new method to ...\quickstart\sql\pkg\_plsql\_bridge\_test.pkb
    * Load into database:
    
            cd ...\quickstart
            mvn liquibase:update

    * Regenerate Code
    
            cd ...\quickstart
            generate\generate_pkg_plsql_bridge_test.bat
        
    * Update class quickstart.Main to call the new method.
    * Run it again (i.e. mvn test)

<a name="integrating_ee6"/>     
# Integrating with a java EE 6 server

Generated annotations should support any EE 6 server. 

It has been used on JBoss EAP 6.1.

Jars

* Include **jpbcg-runtime-{version}.jar** in your project.
* Also include some recent versions of commons-beanutils-1.8.3.jar, commons-collections-3.2.jar, commons-logging-1.1.1.jar.
* Also include an Oracle JDBC driver, i.e. ojdbc6.jar.
* If you install the jpbcg-runtime-1.0.jar jar into your maven repository, the pom will also be installed and the transitive dependencies will be automatically known to your project. 

Create a directory to contain the batch scripts to generate code.

In that directory create CodeGenerator.properties.

    #
    # Connection information
    #
    jdbcURL = jdbc:oracle:thin:@//localhost:1521/xe
    jdbcUser = <YOUR USERNAME>
    jdbcPassword = <YOUR PASSWORD>

    # Output for generated source
    # Default: output
    
    javaOutputDir = ../src/main/java
    
    # Output for generated non Java source
    # Default: output
    
    nonJavaOutputDir = ../src/main/resources
    
In that directory create generate_\<YOUR_PACKAGE\>.bat and add a line.

    call %GENERATOR_BAT% CodeGenerator.properties <YOUR_SCHEMA> <YOUR_PACKAGE>

Generate JAVA JDBC code for you package

    Run generate_<YOUR_PACKAGE>.bat

Create and configure your_project/src/plsql_bridge.properties 

    #
    # Use if you want to extend (aka make a subclass) the executor to get / close your own connection datasource.
    #
    PlsqlJavaBridgeExecutorImplClass=plsql_bridge.MyPlsqlJavaBridgeExecutor

Implement plsql_bridge.MyPlsqlJavaBridgeExecutor

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

    }

Write code to call your package.

Compile your source.

Execute your code.

<a name="integrating_spring"/>
# Integrating with Spring

* Not yet implemented.
* You can help!
* Need to extend templates (annotation usage) and for new target platform option (i.e. Add "Service" import and annotation to package class).
* Should be a few lines of code, but testing is required.
* Need to extend the executor to get the data source correctly.

<a name="integrating_web_service"/>
# Integrating with a Web Service

* Not yet implemented.
* You can help!
* It should be possible to annotate EJB to expose as a webservice

<a name="code_generation_for_rest_and_javascript"/>
# Integrating with REST and JavaScript 

* Not yet implemented.
* You can help!
* It should be possible to generate servlet that expose the PL/SQL to the client.
* It should be possible to generate javascript that calls the PL/SQL from the client. 


<a name="about"/>
# About

This project stems from the need to interface PL/SQL stored procedures regularly from OC4J 10.1.3.5.0 applications.

Created June 2011.
Updated Dec 2013 for Maven and EE 6.

<a name="limitations"/>
# Limitations

Out of the box for support only:

1. For plain JDBC connections 
1. EE 6 No-Interface EJB 3.0 annotations

User must extend the Executor for custom connection management.

Deep typing of arguments in stored procedures not handled.

Overloaded stored procedures not handled.

Only common PL/SQL types handled.

<a name="licence"/>
# Licence

* Free and unrestricted.
* Use at your own risk.

<a name="best_practises"/>
# Best Practices

* Subclass the executor to get the correct data source and handle exceptions.
* Use simple types.
* Use typed cursors.
* Where specific transactional or security annotations are required, use the "abstract" configuration setting, and extend the generated classes and add your required annocations.
* Where this tool does not handle your PLSQL, dont try to work around, just use plain old JDBC.
* Commit the generated source into your project source.

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
* Generate code to expose PL/SQL as Web Services
* Generate code to expose PL/SQL as REST Services with Javascript helpers.
* Improve type support.
* Document type support.
* Configuration for different names for java versions of packages and procedures, ie underscore removal, camel case.
* Constructor for arguments with parameter.