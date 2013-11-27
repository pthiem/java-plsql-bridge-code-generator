create or replace package core.pkg_plsql_bridge_test
as

  subtype t_number_ten_two is number(10,2) ;

  -- Declare custom type
  -- same as table plsql_bridge_test 
  type t_record is record (
    fld_number_null         number,
    fld_number_value        number,
    fld_number_float        number(10,2),
    fld_number_float_null   number(10,2),
    fld_numeric_null        numeric,
    fld_numeric_value       numeric,
    fld_numeric_float_null  numeric(10,2),
    fld_numeric_float_value numeric(10,2),
    fld_float               float,
    fld_float_null          float,
    fld_double              double precision,
    fld_double_null         double precision,
    fld_varchar2            varchar2(100),
    fld_varchar2_null       varchar2(100),
    fld_date                date,
    fld_date_null           date,
    fld_char                char(1),
    fld_char_null           char(1),
    fld_clob                clob,
    fld_clob_null           clob,
    fld_blob                blob,
    fld_blob_null           blob
    );

  -- Declare cursor of custom type
  type cur_recordtype is ref cursor return t_record;
  
  -- Delare type just like a person record.
  type cur_rowtype is ref cursor return plsql_bridge_test%rowtype;
  
  procedure p_get_cursor_untyped (
    a_number in number, 
    acur_uptyped out sys_refcursor -- cursor of untyped data
    );

  procedure p_get_cursor_recordtype (
    a_number in number, 
    acur_recordtype out cur_recordtype -- cursor of record type 
    );

  procedure p_get_cursor_rowtype (
    a_number in number, 
    acur_rowtype out cur_rowtype -- cursor of record rowtype type 
    );
    

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
    );

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
    );
    
  procedure p_test_number_in_out (
    fld_number_in_null_out_null in out number,
    fld_number_in_null_out_value in out number,
    fld_number_in_value_out_null in out number,
    fld_number_in_value_out_value in out number,
    result                  out varchar2
    );
    
  procedure p_test_number_simple (
    a_in_null in number,
    a_in_value in number,
    a_out_null out number,
    a_in_null_out_null in out number,
    a_in_value_out_null in out number,
    a_in_null_out_value in out number,
    a_in_value_out_value in out number);

  procedure p_test_number_decimal_simple (
    a_in_null in rto_annual_fee.amt_paid%type ,
    a_in_value in rto_annual_fee.amt_paid%type,
    a_out_null out rto_annual_fee.amt_paid%type,
    a_in_null_out_null in out rto_annual_fee.amt_paid%type,
    a_in_value_out_null in out rto_annual_fee.amt_paid%type,
    a_in_null_out_value in out rto_annual_fee.amt_paid%type,
    a_in_value_out_value in out rto_annual_fee.amt_paid%type);

  procedure p_test_numeric_simple (
    a_in_null in numeric,
    a_in_value in numeric,
    a_out_null out numeric,
    a_in_null_out_null in out numeric,
    a_in_value_out_null in out numeric,
    a_in_null_out_value in out numeric,
    a_in_value_out_value in out numeric);

  procedure p_test_varchar2_simple (
    a_in_null in varchar2,
    a_in_value in varchar2,
    a_out_null out varchar2,
    a_out_value out varchar2,
    a_in_null_out_null in out varchar2,
    a_in_value_out_null in out varchar2,
    a_in_null_out_value in out varchar2,
    a_in_value_out_value in out varchar2);

  procedure p_test_date_simple (
    a_in_null in date,
    a_in_value in date,
    a_out_null out date,
    a_out_value out date,
    a_in_null_out_null in out date,
    a_in_value_out_null in out date,
    a_in_null_out_value in out date,
    a_in_value_out_value in out date);

  procedure p_test_float_simple (
    a_in_null in float,
    a_in_value in float,
    a_out_null out float,
    a_out_value out float,
    a_in_null_out_null in out float,
    a_in_value_out_null in out float,
    a_in_null_out_value in out float,
    a_in_value_out_value in out float);

  procedure p_test_clob_simple (
    a_in_null in clob,
    a_in_value in clob,
    a_out_null out clob,
    a_out_value out clob,
    a_in_null_out_null in out clob,
    a_in_value_out_null in out clob,
    a_in_null_out_value in out clob,
    a_in_value_out_value in out clob);

  procedure p_test_blob_simple (
    a_in_null in blob,
    a_in_value in blob,
    a_out_null out blob,
    a_out_value out blob,
    a_in_null_out_null in out blob,
    a_in_value_out_null in out blob,
    a_in_null_out_value in out blob,
    a_in_value_out_value in out blob);
  
end;