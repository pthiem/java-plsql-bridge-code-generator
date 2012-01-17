create or replace PACKAGE BODY core.pkg_plsql_bridge_test AS

  -- Misc notes
      
  -- Select into package defined type cursor
  -- So typing semi works, but 
  --    1. select statement must have correctly labelled columns (pl/sql compiler does not detect the mismatch between select statement column names and type column names),
  --    2. setting up the locally defined type doesn't add much.
  --    3. If you mess up your column names, results go into wrong spots.
  --    4. PLSQL syntax does enforce having the right number of columns.
  --    4. PLSQL syntax does enforce having either the column names correct, or the column order.
  --    5. If columns named correctly, order does not matter.
  --    6. Could program bridge to either assume column order correct, or column names correct.


  procedure p_get_cursor_untyped (
    a_number in number, 
    acur_uptyped out sys_refcursor -- cursor of untyped data
    )
  is
  begin
    open acur_uptyped for select * from plsql_bridge_test;
  end;

  procedure p_get_cursor_recordtype (
    a_number in number, 
    acur_recordtype out cur_recordtype -- cursor of record type 
    )
  is
  begin
    open acur_recordtype for select * from plsql_bridge_test;
  end;

  procedure p_get_cursor_rowtype (
    a_number in number, 
    acur_rowtype out cur_rowtype -- cursor of record rowtype type 
    )
  is
  begin
    open acur_rowtype for select * from plsql_bridge_test;
  end;
    
  procedure p_test_types_in_simple (
    fld_number_null         number,
    fld_number_value        number,
    fld_number_float        rto_annual_fee.amt_paid%type,
    fld_number_float_null   rto_annual_fee.amt_paid%type,
    fld_numeric_null        numeric,
    fld_numeric_value       numeric,
    fld_float               float,
    fld_float_null          float,
    fld_double              double precision,
    fld_double_null         double precision,
    fld_varchar2            varchar2,
    fld_varchar2_null       varchar2,
    fld_date                date,
    fld_date_null           date,
    fld_char                char,
    fld_char_null           char,
    fld_subtype             t_number_ten_two,
    result                  out varchar2
    )
  is
  begin
  
    result := 
      fld_number_null || ',' || fld_number_value || ',' || fld_number_float || ',' || 
      fld_number_float_null || ',' || fld_numeric_null || ',' || fld_numeric_value || ',' || 
      fld_float || ',' || fld_float_null || ',' || fld_double || ',' || 
      fld_double_null || ',' || fld_varchar2  || ',' || fld_varchar2_null || ',' || 
      fld_date || ',' || fld_date_null || ',' || fld_char || ',' || 
      fld_char_null || ',' || fld_subtype;
      
  end;

  procedure p_test_types_out_simple (
    fld_number_null         out number,
    fld_number_value        out number,
    fld_number_float        out rto_annual_fee.amt_paid%type,
    fld_number_float_null   out rto_annual_fee.amt_paid%type,
    fld_numeric_null        out numeric,
    fld_numeric_value       out numeric,
    fld_float               out float,
    fld_float_null          out float,
    fld_double              out double precision,
    fld_double_null         out double precision,
    fld_varchar2            out varchar2,
    fld_varchar2_null       out varchar2,
    fld_date                out date,
    fld_date_null           out date,
    fld_char                out char,
    fld_char_null           out char,
    fld_subtype             out t_number_ten_two
    )
  is
  begin
  
    fld_number_value := 343;
    fld_number_float := 5444.23;
    fld_numeric_value := 343;
    fld_float := 998.22;
    fld_double := 343.345;
    fld_varchar2 := 'asdfa wer vb';
    fld_date := sysdate;
    fld_char := 'Y';
  
  end;
  
  procedure p_test_number_in_out (
    fld_number_in_null_out_null in out number,
    fld_number_in_null_out_value in out number,
    fld_number_in_value_out_null in out number,
    fld_number_in_value_out_value in out number,
    result                  out varchar2
    )
  is
  begin
  
    result := 
      fld_number_in_null_out_null || ',' || fld_number_in_null_out_value || ',' || 
      fld_number_in_value_out_null || ',' || fld_number_in_value_out_value;

    fld_number_in_null_out_null := null;
    fld_number_in_null_out_value := 1001;
    fld_number_in_value_out_null := null; 
    fld_number_in_value_out_value := 2002;
         
  end;
  
    
  procedure p_test_number_simple (
    a_in_null in number,
    a_in_value in number,
    a_out_null out number,
    a_in_null_out_null in out number,
    a_in_value_out_null in out number,
    a_in_null_out_value in out number,
    a_in_value_out_value in out number
    )
  is
  begin
    null;
  end;
    

  procedure p_test_number_decimal_simple (
    a_in_null in rto_annual_fee.amt_paid%type ,
    a_in_value in rto_annual_fee.amt_paid%type,
    a_out_null out rto_annual_fee.amt_paid%type,
    a_in_null_out_null in out rto_annual_fee.amt_paid%type,
    a_in_value_out_null in out rto_annual_fee.amt_paid%type,
    a_in_null_out_value in out rto_annual_fee.amt_paid%type,
    a_in_value_out_value in out rto_annual_fee.amt_paid%type
    )
  is
  begin
    null;
  end;
    
  procedure p_test_numeric_simple (
    a_in_null in numeric,
    a_in_value in numeric,
    a_out_null out numeric,
    a_in_null_out_null in out numeric,
    a_in_value_out_null in out numeric,
    a_in_null_out_value in out numeric,
    a_in_value_out_value in out numeric
    )
  is
  begin
    null;
  end;
    

  procedure p_test_varchar2_simple (
    a_in_null in varchar2,
    a_in_value in varchar2,
    a_out_null out varchar2,
    a_out_value out varchar2,
    a_in_null_out_null in out varchar2,
    a_in_value_out_null in out varchar2,
    a_in_null_out_value in out varchar2,
    a_in_value_out_value in out varchar2
    )
  is
  begin
    null;
  end;
    

  procedure p_test_date_simple (
    a_in_null in date,
    a_in_value in date,
    a_out_null out date,
    a_out_value out date,
    a_in_null_out_null in out date,
    a_in_value_out_null in out date,
    a_in_null_out_value in out date,
    a_in_value_out_value in out date
    )
  is
  begin
    null;
  end;
    

  procedure p_test_float_simple (
    a_in_null in float,
    a_in_value in float,
    a_out_null out float,
    a_out_value out float,
    a_in_null_out_null in out float,
    a_in_value_out_null in out float,
    a_in_null_out_value in out float,
    a_in_value_out_value in out float
    )
  is
  begin
    null;
  end;
    

  procedure p_test_clob_simple (
    a_in_null in clob,
    a_in_value in clob,
    a_out_null out clob,
    a_out_value out clob,
    a_in_null_out_null in out clob,
    a_in_value_out_null in out clob,
    a_in_null_out_value in out clob,
    a_in_value_out_value in out clob
    )
  is
  begin
    null;
  end;
    

  procedure p_test_blob_simple (
    a_in_null in blob,
    a_in_value in blob,
    a_out_null out blob,
    a_out_value out blob,
    a_in_null_out_null in out blob,
    a_in_value_out_null in out blob,
    a_in_null_out_value in out blob,
    a_in_value_out_value in out blob
    )
  is
  begin
    null;
  end;
  
end;  