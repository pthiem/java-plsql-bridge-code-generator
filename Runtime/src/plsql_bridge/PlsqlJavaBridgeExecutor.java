package plsql_bridge;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class PlsqlJavaBridgeExecutor {

    private Log logger = LogFactory.getFactory().getInstance(getClass());
    
    static PlsqlJavaBridgeExecutor instance;

    static Properties props = new Properties();

    protected PlsqlJavaBridgeExecutor() {
    }
    
    
    public static PlsqlJavaBridgeExecutor getInstance() throws Exception {
    
        // Try to get config
        // Allow user to create own subclass
        if (instance == null) {
            InputStream stream = PlsqlJavaBridgeExecutor.class.getClassLoader().getResourceAsStream("plsql_bridge.properties");
            if (stream != null) {
                props.load(stream);
                if (props.get("PlsqlJavaBridgeExecutorImplClass") != null) {
                    instance = (PlsqlJavaBridgeExecutor) Class.forName((String) props.get("PlsqlJavaBridgeExecutorImplClass")).newInstance();
                }
            }
        }
        
        // Otherwise use default implementation
        if (instance == null) {
        
            instance = new PlsqlJavaBridgeExecutor();
        }
        
        return instance;
        
    }
    
    protected DataSource getDataSource() throws Exception {

        String jdbcURL = (String) props.get("jdbcURL");
        String jdbcUser = (String) props.get("jdbcUser");
        String jdbcPassword = (String) props.get("jdbcPassword");

        OracleDataSource ds = new OracleDataSource();
        ds.setURL(jdbcURL);
        ds.setUser(jdbcUser);
        ds.setPassword(jdbcPassword);    
        
        return ds;
        
    }
    
    protected Connection getConnection() throws Exception {
    
        Connection conn = getDataSource().getConnection(); 

        return conn;
    }
    
    protected void close(Connection conn, CallableStatement stmt, ResultSet rs) {
      try {
        if(rs != null) rs.close();
      } catch(Exception e) {
        logger.debug("Error while closing the resultset: ", e);
      }
      try {
        if(stmt != null) stmt.close();
      } catch(Exception e) {
        logger.debug("Error while closing the statement: ", e);
      }
      try {
        if(conn != null) conn.close();
      } catch(Exception e) {
        logger.debug("Error while closing the connection : ", e);
      }
    }

    private static Map xmlDocumentCache;
    private static Map xmlArgumentCache;
    
    private XPath getXPath(XPath xpath) {
        if (xpath != null) {
            return xpath;
        }
        
        // First time calling procedure, initialise xpath expression 
        XPathFactory xFactory = XPathFactory.newInstance(); // Create a XPathFactory
        return xFactory.newXPath(); // Create a XPath object
        
    }

    public void execute( String packageName, String procedureName, Object arguments) throws Exception {
    
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        
        try { 
        
            if (xmlDocumentCache == null) { xmlDocumentCache = new HashMap(); };
            if (xmlArgumentCache == null) { xmlArgumentCache = new HashMap(); };

            
            Document doc = (Document) xmlDocumentCache.get(packageName);
            if (doc == null) {
            InputStream stream = this.getClass().getResourceAsStream("/plsql_bridge/" + packageName + "/package_metadata.xml");
            
            // Standard of reading a XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
                
            builder = factory.newDocumentBuilder();
            doc = builder.parse(stream);        
                stream.close();
            
                xmlDocumentCache.put(packageName, doc);
            }

            XPath xpath = null; // Create a XPath object
            XPathExpression expr = null;
            
            conn = getConnection();
            
            String cacheKey = "procedureMetaData_"+packageName+"_"+procedureName;
            Node procedureMetaData = (Node) xmlArgumentCache.get(cacheKey);
            if (procedureMetaData == null) {
            
                xpath = getXPath(xpath);
            expr = xpath.compile("//PROCEDURE[PROCEDURE_NAME='" + procedureName.toUpperCase() + "']");
                procedureMetaData = (Node) expr.evaluate(doc, XPathConstants.NODE);
                xmlArgumentCache.put(cacheKey, procedureMetaData);
            }
            
            cacheKey = "argCount_"+packageName+"_"+procedureName;
            
            Double argCount = (Double) xmlArgumentCache.get(cacheKey);
            if (argCount == null) {
                xpath = getXPath(xpath);
            expr = xpath.compile("count(./ARGUMENTS/ARGUMENTS_ROW)");
                argCount = (Double) expr.evaluate(procedureMetaData, XPathConstants.NUMBER);
                xmlArgumentCache.put(cacheKey, argCount);
            }
            
            // prepare call, get count of parameters, build statement.
            String plsqlStatementSQL = "";
            for (int i = 0 ; i < argCount ; i++) {
                plsqlStatementSQL += ",?";
            }
            plsqlStatementSQL = "{call " + packageName + "." + procedureName + "(" + plsqlStatementSQL.substring(1) +")}";
            
            logger.debug(plsqlStatementSQL);
                        
            stmt = conn.prepareCall(plsqlStatementSQL);

            // iterate over arguments to prepare statement.            
            cacheKey = "nodes_input_"+packageName+"_"+procedureName;
            NodeList nodes = (NodeList) xmlArgumentCache.get(cacheKey);
            if (nodes == null) {
                nodes = (NodeList) xpath.compile("./ARGUMENTS/ARGUMENTS_ROW").evaluate(procedureMetaData, XPathConstants.NODESET);
                xmlArgumentCache.put(cacheKey, nodes);
            }
            
            for (int i = 0 ; i < nodes.getLength() ; i++) {
            
                Node node = nodes.item(i);
            
                cacheKey = "nodes_input_inOut_"+packageName+"_"+procedureName+"_"+i;
                String inOut = (String) xmlArgumentCache.get(cacheKey);
                if (inOut == null) {
                    xpath = getXPath(xpath);
                    inOut = (String) xpath.compile("./IN_OUT").evaluate(node, XPathConstants.STRING);
                    xmlArgumentCache.put(cacheKey, inOut);
                }
                
                cacheKey = "nodes_input_javaGetMethodName_"+packageName+"_"+procedureName+"_"+i;
                String javaGetMethodName = (String) xmlArgumentCache.get(cacheKey);
                if (javaGetMethodName == null) {
                    xpath = getXPath(xpath);
                    javaGetMethodName = "get" + (String) xpath.compile("./ARGUMENT_FIRSTCAP").evaluate(node, XPathConstants.STRING);
                    xmlArgumentCache.put(cacheKey, javaGetMethodName);
                }
                
                
                cacheKey = "nodes_input_oraType_"+packageName+"_"+procedureName+"_"+i;
                String oraType = (String) xmlArgumentCache.get(cacheKey);
                if (oraType == null) {
                    xpath = getXPath(xpath);
                    oraType = (String) xpath.compile("./DATA_TYPE").evaluate(node, XPathConstants.STRING);
                    xmlArgumentCache.put(cacheKey, oraType);
                }
                
                
                cacheKey = "nodes_input_oraFieldName_"+packageName+"_"+procedureName+"_"+i;
                String oraFieldName = (String) xmlArgumentCache.get(cacheKey);
                if (oraFieldName == null) {
                    xpath = getXPath(xpath);
                    oraFieldName = (String) xpath.compile("./ARGUMENT_NAME").evaluate(node, XPathConstants.STRING);
                    xmlArgumentCache.put(cacheKey, oraFieldName);
                }
                
                //String oraTypeScale = (String) xpath.compile("./DATA_SCALE").evaluate(node, XPathConstants.STRING);
                
                cacheKey = "nodes_input_javaFieldName_"+packageName+"_"+procedureName+"_"+i;
                String javaFieldName = (String) xmlArgumentCache.get(cacheKey);
                if (javaFieldName == null) {
                    xpath = getXPath(xpath);
                    javaFieldName = (String) xpath.compile("./ARGUMENT_LOWER").evaluate(node, XPathConstants.STRING);
                    xmlArgumentCache.put(cacheKey, javaFieldName);
                }
                
                Class javaFieldType = arguments.getClass().getField(javaFieldName).getType();


                //
                // Some checks for types that are not handled by the runtime
                //
                if (oraType.equals("PL/SQL RECORD")) {
                
                    // Not supported by jdbc. JDBC cannot do pl/sql record types.
                    throw new Exception ("Type PL/SQL RECORD for " + packageName + "." + procedureName + "." + oraFieldName + " is not supported by JDBC.");
                    
                }  else if (oraType.equals("TABLE")) {
                
                    // Not supported by jdbc. JDBC cannot do pl/sql record types.
                    throw new Exception ("Type TABLE and ARRAYS for " + packageName + "." + procedureName + "." + oraFieldName + " is not supported by the PLSQL BRIDGE.");
                    
                }
                
                if (inOut.equals("IN") || inOut.equals("IN/OUT")) {
                
                    Object value = arguments.getClass().getMethod(javaGetMethodName).invoke(arguments, (Object[]) null);
                    
                    if (javaFieldType.equals(Long.class)) {
                    
                        stmt.setObject(i+1, value, OracleTypes.NUMBER); // Set object, in case of null
                        
                    } else if (javaFieldType.equals(String.class)) {
                    
                         stmt.setString(i+1, (String) value);
                    
                    } else if (javaFieldType.equals(Double.class)) {
                    
                        stmt.setObject(i+1, value, OracleTypes.NUMBER); // Set object, in case of null

                    } else if (javaFieldType.equals(java.util.Date.class)) {
                    
                        Timestamp timestamp = (value == null ? null : new java.sql.Timestamp(((java.util.Date) value).getTime()));
                        stmt.setTimestamp(i+1, timestamp); // Works with time component
                        
                    } else if (javaFieldType.equals(byte[].class)) {
                    
                        // TODO set blob, not tested
                        if (value == null) {
                            stmt.setNull(i+1, OracleTypes.BLOB);
                        } else {
                            stmt.setBinaryStream(i+1, new ByteArrayInputStream((byte[])value), ((byte[]) value).length);
                        }
                        
                    } else if (javaFieldType.equals(java.util.List.class)) {
                        
                        // Should not happen, lists are output types only
                        throw new Exception ("Java field type " + javaFieldType.getName() + " for "+ packageName + "." + procedureName + "." + oraFieldName + " is not supported by the PLSQL BRIDGE for *input*.");
                        
                    } else {
                         
                        throw new Exception ("Java field type " + javaFieldType.getName() + " for "+ packageName + "." + procedureName + "." + oraFieldName + " is not supported by the PLSQL BRIDGE for *input*.");
                        
                    }
                    
                }
                
                if (inOut.equals("OUT") || inOut.equals("IN/OUT")) {

                    if (javaFieldType.equals(Long.class)) {
                    
                        stmt.registerOutParameter(i+1, OracleTypes.NUMBER);
                        
                    } else if (javaFieldType.equals(String.class)) {
                    
                        stmt.registerOutParameter(i+1, OracleTypes.VARCHAR);
                    
                    } else if (javaFieldType.equals(Double.class)) {
                    
                        stmt.registerOutParameter(i+1, OracleTypes.NUMBER);

                    } else if (javaFieldType.equals(java.util.Date.class)) {
                    
                        stmt.registerOutParameter(i+1, OracleTypes.TIMESTAMP);
                        
                    } else if (javaFieldType.equals(byte[].class)) {
                    
                         stmt.registerOutParameter(i+1, OracleTypes.BLOB);
                        
                    } else if (javaFieldType.equals(java.util.List.class)) {
                        
                         stmt.registerOutParameter(i+1, OracleTypes.CURSOR);
                        
                    } else {
                         
                        throw new Exception ("Java field type " + javaFieldType.getName() + " for "+ packageName + "." + procedureName + "." + oraFieldName + " is not supported by the PLSQL BRIDGE for *output*.");
                        
                    }

                }
                
            }
            
            // execute
            stmt.execute();
            
            for (int i = 0 ; i < nodes.getLength() ; i++) {
            
                Node node = nodes.item(i);
            
                // inOut
                cacheKey = "nodes_output_inOut_"+packageName+"_"+procedureName+"_"+i;
                String inOut = (String) xmlArgumentCache.get(cacheKey);
                if (inOut == null) {
                    xpath = getXPath(xpath);
                    inOut = (String) xpath.compile("./IN_OUT").evaluate(node, XPathConstants.STRING);
                    xmlArgumentCache.put(cacheKey, inOut);
                }
                
                // javaSetMethodName
                cacheKey = "nodes_output_javaSetMethodName_"+packageName+"_"+procedureName+"_"+i;
                String javaSetMethodName = (String) xmlArgumentCache.get(cacheKey);
                if (javaSetMethodName == null) {
                    xpath = getXPath(xpath);
                    javaSetMethodName = "set" + (String) xpath.compile("./ARGUMENT_FIRSTCAP").evaluate(node, XPathConstants.STRING);
                    xmlArgumentCache.put(cacheKey, javaSetMethodName);
                }

                // javaSetMethodName
                cacheKey = "nodes_output_javaFieldName_"+packageName+"_"+procedureName+"_"+i;
                String javaFieldName = (String) xmlArgumentCache.get(cacheKey);
                if (javaFieldName == null) {
                    xpath = getXPath(xpath);
                    javaFieldName = (String) xpath.compile("./ARGUMENT_LOWER").evaluate(node, XPathConstants.STRING);
                    xmlArgumentCache.put(cacheKey, javaFieldName);
                }
                
                // javaFieldTypedName
                cacheKey = "nodes_output_javaFieldTypedName_"+packageName+"_"+procedureName+"_"+i;
                String javaFieldTypedName = (String) xmlArgumentCache.get(cacheKey);
                if (javaFieldTypedName == null) {
                    xpath = getXPath(xpath);
                    javaFieldTypedName = (String) xpath.compile("./ARGUMENT_FIRSTCAP").evaluate(node, XPathConstants.STRING);
                    xmlArgumentCache.put(cacheKey, javaFieldTypedName);
                }
                
                //String oraType = (String) xpath.compile("./DATA_TYPE").evaluate(node, XPathConstants.STRING);
                //String oraFieldName = (String) xpath.compile("./ARGUMENT_NAME").evaluate(node, XPathConstants.STRING);

                // isTypedRecord                
                cacheKey = "nodes_output_isTypedRecord_"+packageName+"_"+procedureName+"_"+i;
                Boolean isTypedRecord = (Boolean) xmlArgumentCache.get(cacheKey);
                if (isTypedRecord == null) {
                    xpath = getXPath(xpath);
                    isTypedRecord = (Boolean) xpath.compile("count(./ARGUMENTS/ARGUMENTS_ROW[DATA_TYPE='PL/SQL RECORD' or DATA_TYPE='TABLE']) > 0").evaluate(node, XPathConstants.BOOLEAN);
                    xmlArgumentCache.put(cacheKey, isTypedRecord);
                }


                Class javaFieldType = arguments.getClass().getField(javaFieldName).getType();
                
                if (inOut.equals("OUT") || inOut.equals("IN/OUT")) {
                
                    Object value = stmt.getObject(i+1);
                
                    if (value == null) {
                        
                        arguments.getClass().getMethod(javaSetMethodName, new Class[] {javaFieldType} ).invoke(arguments, new Object[] { value });  
                        
                    }
                
                    if (value != null) {
                        
                        if (javaFieldType.equals(Long.class)) {
                        
                            value = new Long( ((Number) value).longValue() );

                            arguments.getClass().getMethod(javaSetMethodName, new Class[] {Long.class} ).invoke(arguments, new Object[] { value });  
                            
                        } else if (javaFieldType.equals(String.class)) {
                        
                            // no change required
                            
                            arguments.getClass().getMethod(javaSetMethodName, new Class[] {String.class} ).invoke(arguments, new Object[] { value });   
                        
                        } else if (javaFieldType.equals(Double.class)) {
                        
                            value = new Double( ((Number) value).doubleValue() );
                        
                            arguments.getClass().getMethod(javaSetMethodName, new Class[] {Double.class} ).invoke(arguments, new Object[] { value });  

                        } else if (javaFieldType.equals(java.util.Date.class)) {
                        
                            // get timestamp as stmt.getObject may get date only component.
                            value = stmt.getTimestamp(i+1);
                            
                            arguments.getClass().getMethod(javaSetMethodName, new Class[] {java.util.Date.class} ).invoke(arguments, new Object[] { value });    
                            
                        } else if (javaFieldType.equals(byte[].class)) {
                        
                            // Blob, not tested for big ones.
                            BLOB blob = (BLOB) value;
                            int length = (int) blob.length();
                            value = blob.getBytes(1, length);
                            
                            arguments.getClass().getMethod(javaSetMethodName, new Class[] {byte[].class} ).invoke(arguments, new Object[] { value });    
                            
                        } else if (javaFieldType.equals(java.util.List.class)) {
                            
                            rs = (ResultSet) stmt.getObject(i+1);
                            
                            List resultList = new ArrayList();
                            
                            while (rs.next()) { 
                            
                                // Untyped results, use map
                            
                                Map map = new HashMap();
                                
                                ResultSetMetaData rsmd = rs.getMetaData();
                                
                                for(int colIndex=1; colIndex<=rsmd.getColumnCount(); colIndex++) {
                                
                                    Object value2 = rs.getObject(colIndex);
                                    
                                    if (value2 != null) {
                                    
                                        if (value2 instanceof java.util.Date) {
                                            
                                            // interprest all dates as timestamps, return date and time components. the rs.getObject method returns on the date component.
                                            value2 = rs.getTimestamp(colIndex);
                                            
                                        } else if (value2 instanceof Number) {
                                        
                                            // No change required, could be BigDecimal, Floats, Double, (types of Number)
                                            // will be converted to type via BeanUtils.copyProperties for typed resultsets
                                            // Will be left unchanged for untyped resultsets, client to handle / convert from Number type.

                                        } else if (value2 instanceof String) {

                                            // No special handling required
                                            
                                        } else if (value2 instanceof CLOB) {

                                            value2 = rs.getString(colIndex);
                                            
                                        } else if (value2 instanceof BLOB) {
                                        
                                            BLOB blob = (BLOB) value2;
                                            int length = (int) blob.length();
                                            value2 = blob.getBytes(1, length);
                                                
                                        } else {
                                            
                                            logger.debug("Unknown type for untyped result set for column " + rsmd.getColumnName(colIndex) + " " + value2.getClass().getName() );
                                            
                                        }
                                        
                                    }
                                
                                    map.put(rsmd.getColumnName(colIndex).toLowerCase() ,value2);
                                    
                                }
                                
                                if (!isTypedRecord) {
                                        
                                    // Not typed, return map
                                    
                                    resultList.add(map);
                                    
                                } else {
                                    
                                    // Typed results
                                    
                                    Class aClass = this.getClass().getClassLoader().loadClass("plsql_bridge." + packageName + "." + procedureName.substring(0,1).toUpperCase() + procedureName.substring(1) + "_arguments$" + javaFieldTypedName);
                                    
                                    Object typedRecord = aClass.newInstance();
                                    
                                    BeanUtils.copyProperties(typedRecord, map); // Use bean utils to copy properties (easier than doing myself).
                                     
                                    resultList.add(typedRecord);
                                    
                                }
                                
                            }
                            
                            rs.close();                            
                        
                            arguments.getClass().getMethod(javaSetMethodName, new Class[] {List.class} ).invoke(arguments, new Object[] { resultList });    
                            
                        } else {
                            
                            throw new Exception("Unknown output parameter type");
                        }
                        
                    }
                                        
                }
                
            }
            
        } finally { 
        
          close(conn, stmt, rs);
          
        }
    
    }
    
    
}
