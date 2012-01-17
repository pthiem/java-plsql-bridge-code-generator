



package test;

import java.util.Date;
import java.util.Map;

import plsql_bridge.pkg_plsql_bridge_test.P_get_cursor_recordtype_arguments;
import plsql_bridge.pkg_plsql_bridge_test.P_get_cursor_rowtype_arguments;
import plsql_bridge.pkg_plsql_bridge_test.P_get_cursor_untyped_arguments;
import plsql_bridge.pkg_plsql_bridge_test.P_test_number_in_out_arguments;
import plsql_bridge.pkg_plsql_bridge_test.P_test_types_in_simple_arguments;
import plsql_bridge.pkg_plsql_bridge_test.P_test_types_out_simple_arguments;
import plsql_bridge.pkg_plsql_bridge_test.Pkg_plsql_bridge_test;
import plsql_bridge.pkg_plsql_bridge_test.Pkg_plsql_bridge_test_impl;


public class MainDemo {
    public MainDemo() {
    }
    
    public static Pkg_plsql_bridge_test getPlsqlPackageInstance() throws Exception {
    
        Pkg_plsql_bridge_test pkg_plsql_bridge_test = new Pkg_plsql_bridge_test_impl();
        
        return pkg_plsql_bridge_test ;
    }


    public static void main(String[] args) throws Exception {
        
        Pkg_plsql_bridge_test pkg_plsql_bridge_test = getPlsqlPackageInstance();
        
        if ( 1==1 ) {
        
            System.out.println("Cursor typed with recordtype.");
            
            P_get_cursor_recordtype_arguments arguments = new P_get_cursor_recordtype_arguments();
            
            arguments.setA_number(20L);

            pkg_plsql_bridge_test.p_get_cursor_recordtype(arguments);
            
            for (P_get_cursor_recordtype_arguments.Acur_recordtype o: arguments.getAcur_recordtype() ) {
                System.out.println("1:" + o.getFld_number_value() + "," + o.getFld_varchar2());
            }
            
        }        

        if ( 1==1 ) {
        
            System.out.println("Cursor typed with rowtype.");
            
            P_get_cursor_rowtype_arguments arguments = new P_get_cursor_rowtype_arguments();
            
            arguments.setA_number(20L);

            pkg_plsql_bridge_test.p_get_cursor_rowtype(arguments);
            
            for (P_get_cursor_rowtype_arguments.Acur_rowtype o: arguments.getAcur_rowtype() ) {
                System.out.println("2:" + o.getFld_number_value() + "," + o.getFld_varchar2());
            }
            
        }        


        if ( 1==1 ) {
        
            System.out.println("Cursor, untyped.");
            
            P_get_cursor_untyped_arguments arguments = new P_get_cursor_untyped_arguments();
            
            arguments.setA_number(20L);

            pkg_plsql_bridge_test.p_get_cursor_untyped(arguments);
            
            for (Map map: arguments.getAcur_uptyped() ) {
                System.out.println("3:" + map);
            }
            
        }
        
        if ( 1==1 ) {
        
            System.out.println("Test, simple inputs.");
            
            P_test_types_in_simple_arguments arguments = new P_test_types_in_simple_arguments();
            
            arguments.setFld_char("A");
            arguments.setFld_date(new Date());
            arguments.setFld_double(55.34);
            arguments.setFld_float(102.233);
            arguments.setFld_number_float(343.34);
            arguments.setFld_number_value(12L);
            arguments.setFld_numeric_value(343L);
            arguments.setFld_varchar2("Hello");
    
            pkg_plsql_bridge_test.p_test_types_in_simple(arguments);
            
            System.out.println("4:" + arguments.getResult());
            
        }       
        
        if ( 1==1 ) {
        
            System.out.println("Test, simple outputs.");
            
            P_test_types_out_simple_arguments arguments = new P_test_types_out_simple_arguments();
            
            pkg_plsql_bridge_test.p_test_types_out_simple(arguments);
            
            System.out.println("5:" + 
                arguments.getFld_varchar2() + "," +
                arguments.getFld_date() + "," +
                arguments.getFld_double() + "," +
                arguments.getFld_float() + "," +
                arguments.getFld_number_float() + "," +
                arguments.getFld_number_value() + "," +
                arguments.getFld_numeric_value() + "," +
                arguments.getFld_char()
                );
            
        }       
        

        if ( 1==1 ) {
            System.out.println("Test, in out fields.");
            
            P_test_number_in_out_arguments arguments = new P_test_number_in_out_arguments();
            
            arguments.setFld_number_in_value_out_null(901L);
            arguments.setFld_number_in_value_out_value(902L);
            
            pkg_plsql_bridge_test.p_test_number_in_out(arguments);
            
            System.out.println("6:" + 
                arguments.getFld_number_in_null_out_null() + "," +
                arguments.getFld_number_in_null_out_value() + "," +
                arguments.getFld_number_in_value_out_null() + "," +
                arguments.getFld_number_in_value_out_value()
                );
            
            System.out.println("6:" + arguments.getResult());
        }           
        
    }
    
}
