package plsql_bridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

import java.net.URL;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;

import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class PlsqlJavaBridgeGenerator {

    static String configTargetPlatform = "default";
    static String configAbstract = "false";

    public PlsqlJavaBridgeGenerator() {
    }

    private static String readFile( String file ) throws Exception {
        BufferedReader reader = new BufferedReader( new FileReader (file));
        String line  = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while( ( line = reader.readLine() ) != null ) {
            // remove single line comments
            if (line.indexOf("--") >= 0) {
                line = line.substring(0, line.indexOf("--"));
            }
            stringBuilder.append( line );
            // Do not put carriage returns as breaks prepared statments. Just put a space between lines
            stringBuilder.append( " " );
        }
        return stringBuilder.toString();
    }
    
    private static void addConfigXslParameters(Transformer transformer) throws Exception {
        transformer.setParameter("targetPlatform", configTargetPlatform);
        transformer.setParameter("abstract", configAbstract);
    }

    public static void main(String[] args) throws Exception {

        // Arguments 
        //   args[0]: home directory of generator
        //   args[1]: location of generator configuration file
        //   args[2]: schema of package to generate for
        //   args[3]: package name

        //
        // Configuration
        //
        
        String generatorHome = args[0];
        String configFilename = args[1];
        Properties config = new Properties();
        FileInputStream in = new FileInputStream(configFilename);
        config.load(in);
        in.close();
        
        String jdbcURL = config.getProperty("jdbcURL");
        String jdbcUser = config.getProperty("jdbcUser");
        String jdbcPassword = config.getProperty("jdbcPassword");
        
        String resultDir = config.getProperty("resultDir"); 

        // Optional Config
        String templateDir = generatorHome + "/src/templates";
        if (config.getProperty("templateDir") != null) {
            templateDir = config.getProperty("templateDir");
        }
        
        String generateXmlSql = generatorHome + "/src/sql/bridge_generate_xml.sql";
        if (config.getProperty("generateXmlSql") != null) {
            generateXmlSql = config.getProperty("generateXmlSql");
        }
        
        if (config.getProperty("targetPlatform") != null) {
            configTargetPlatform  = config.getProperty("targetPlatform");
        }

        if (config.getProperty("abstract") != null) {
            configAbstract  = config.getProperty("abstract");
        }
        
        // Target Directories
        String targetOwner = args[2].toLowerCase();
        String targetPackage = args[3].toLowerCase(); 
        
        //
        // Variables
        //
        
        resultDir += "/plsql_bridge/" + targetPackage.toLowerCase();
        
        String xmlResult =  resultDir + "/package_metadata.xml";
        
        // Package Interface
        String packageInterfaceXSL = templateDir + "/package_interface.xsl"; 
        String packageInterfaceResult = resultDir + "/" + targetPackage.substring(0,1).toUpperCase() + targetPackage.substring(1).toLowerCase() + ".java"; 

        // Package Class
        String packageClassXSL = templateDir + "/package_class.xsl"; 
        String packageClassResult = resultDir + "/" + targetPackage.substring(0,1).toUpperCase() + targetPackage.substring(1).toLowerCase() + "_impl" + (configAbstract.equals("true") ? "_abstract" : "") + ".java";

        // Arguments
        String argumentsXSL = templateDir + "/arguments_class.xsl"; 

        if (1==1) {
        
            new File(resultDir).mkdirs();
        
            // Generate XML 
            
            // Get connection
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection conn = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
        
            //
            // Generate XML document
            //
            
            String sql = readFile(generateXmlSql);
            
            OracleCallableStatement stmt = (OracleCallableStatement) conn.prepareCall(sql);
            stmt.setString(1, targetOwner);
            stmt.setString(2, targetPackage);
            stmt.registerOutParameter(3, OracleTypes.CLOB);
            
            stmt.execute();
            Clob xmlResultClob = stmt.getClob(3);
            String xmlResultString = "";
            BufferedReader clobReader = new BufferedReader(xmlResultClob.getCharacterStream());
            xmlResultString += clobReader.readLine(); // readies clob 
            while (clobReader.ready()) {
                xmlResultString += clobReader.readLine();
                xmlResultString += "\n";
            }
            //System.out.println(xmlResultString);
            clobReader.close();
            stmt.close();
            conn.close();
            
            System.out.println("Generating " + xmlResult);
            
            FileWriter w = new FileWriter(xmlResult);
            w.write(xmlResultString);
            w.close();
        
        }
        
        //
        // Generate java files
        //
        
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        
        // Package Interface
        System.out.println("Generating " + packageInterfaceResult);
         
        transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource( packageInterfaceXSL));
        addConfigXslParameters(transformer);
        transformer.transform(new javax.xml.transform.stream.StreamSource(xmlResult), new javax.xml.transform.stream.StreamResult(new FileOutputStream(packageInterfaceResult)));

        // Package Class
        System.out.println("Generating " + packageClassResult);
        
        transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(packageClassXSL));
        addConfigXslParameters(transformer);
        transformer.transform(new javax.xml.transform.stream.StreamSource(xmlResult), new javax.xml.transform.stream.StreamResult(new FileOutputStream(packageClassResult)));
        
        //
        // Procedures
        //
        
        // Standard of reading a XML file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        builder = factory.newDocumentBuilder();
        doc = builder.parse(new File(xmlResult));        
        
        XPathFactory xFactory = XPathFactory.newInstance(); // Create a XPathFactory
        XPath xpath = xFactory.newXPath(); // Create a XPath object
        
        NodeList nodes  = (NodeList) xpath.compile("//PROCEDURE").evaluate(doc, XPathConstants.NODESET);
        
        for (int i = 0 ; i < nodes.getLength() ; i++) {

            Node node = nodes.item(i);            
            
            String procedureName = (String) xpath.compile("./PROCEDURE_NAME").evaluate(node, XPathConstants.STRING);
            String procedureNameInitcap = (String) xpath.compile("./PROCEDURE_NAME_FIRSTCAP").evaluate(node, XPathConstants.STRING);
            
            String argumentsResult = resultDir + "/" + procedureNameInitcap + "_arguments.java";
            
            // Looping required                                  
            // Params
            System.out.println("Generating " + argumentsResult);
            
            transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(argumentsXSL));
            transformer.clearParameters();
            addConfigXslParameters(transformer);
            transformer.setParameter("targetProcedureName", procedureName);
            
            transformer.transform(new javax.xml.transform.stream.StreamSource(xmlResult), new javax.xml.transform.stream.StreamResult(new FileOutputStream(argumentsResult)));

        }
    
    }
    
}
