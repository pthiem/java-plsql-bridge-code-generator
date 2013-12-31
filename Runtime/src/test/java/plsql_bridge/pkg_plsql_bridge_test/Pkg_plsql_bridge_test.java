

// Generated source, do not edit, but you can extend.

package plsql_bridge.pkg_plsql_bridge_test;

// Imports

import javax.ejb.Stateless;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plsql_bridge.PlsqlJavaBridgeExecutor;

// Annotations

@Stateless

public  class Pkg_plsql_bridge_test
{

    private Log logger = LogFactory.getFactory().getInstance(getClass());

    public Pkg_plsql_bridge_test() {
    }  

    

    public void p_get_cursor_recordtype(P_get_cursor_recordtype_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_get_cursor_recordtype", arguments);
        
    }
    
    

    public void p_get_cursor_rowtype(P_get_cursor_rowtype_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_get_cursor_rowtype", arguments);
        
    }
    
    

    public void p_get_cursor_untyped(P_get_cursor_untyped_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_get_cursor_untyped", arguments);
        
    }
    
    

    public void p_test_blob_simple(P_test_blob_simple_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_blob_simple", arguments);
        
    }
    
    

    public void p_test_clob_simple(P_test_clob_simple_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_clob_simple", arguments);
        
    }
    
    

    public void p_test_date_simple(P_test_date_simple_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_date_simple", arguments);
        
    }
    
    

    public void p_test_float_simple(P_test_float_simple_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_float_simple", arguments);
        
    }
    
    

    public void p_test_number_decimal_simple(P_test_number_decimal_simple_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_number_decimal_simple", arguments);
        
    }
    
    

    public void p_test_number_in_out(P_test_number_in_out_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_number_in_out", arguments);
        
    }
    
    

    public void p_test_number_simple(P_test_number_simple_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_number_simple", arguments);
        
    }
    
    

    public void p_test_numeric_simple(P_test_numeric_simple_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_numeric_simple", arguments);
        
    }
    
    

    public void p_test_types_in_simple(P_test_types_in_simple_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_types_in_simple", arguments);
        
    }
    
    

    public void p_test_types_out_simple(P_test_types_out_simple_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_types_out_simple", arguments);
        
    }
    
    

    public void p_test_varchar2_simple(P_test_varchar2_simple_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("pkg_plsql_bridge_test","p_test_varchar2_simple", arguments);
        
    }
    
    
        
}

