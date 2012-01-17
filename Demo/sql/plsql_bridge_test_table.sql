create user core identified by core;

CREATE TABLE core.plsql_bridge_test
(
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

SET DEFINE OFF;
Insert into plsql_bridge_test
   (fld_number_null, fld_number_value, fld_number_float, fld_number_float_null, fld_numeric_null, 
    fld_numeric_value, fld_numeric_float_null, fld_numeric_float_value, fld_float, fld_float_null, 
    fld_double, fld_double_null, fld_varchar2, fld_varchar2_null, fld_date, 
    fld_date_null, fld_char, fld_char_null, fld_clob, fld_clob_null, 
    fld_blob, fld_blob_null)
 Values
   (NULL, 2123, 121.23, NULL, NULL, 
    123, NULL, 232.33, 23.3432342, NULL, 
    23.3432342, NULL, 'asfsdf23 afdafr erwe rwerwr er w', NULL, TO_DATE('08/16/2011 17:30:12', 'MM/DD/YYYY HH24:MI:SS'), 
    NULL, 'Y', NULL, 'asfsdf23 afdafr erwe rwerwr er w', NULL, 
    NULL, NULL);
 
Insert into plsql_bridge_test
   (fld_number_null, fld_number_value, fld_number_float, fld_number_float_null, fld_numeric_null, 
    fld_numeric_value, fld_numeric_float_null, fld_numeric_float_value, fld_float, fld_float_null, 
    fld_double, fld_double_null, fld_varchar2, fld_varchar2_null, fld_date, 
    fld_date_null, fld_char, fld_char_null, fld_clob, fld_clob_null, 
    fld_blob, fld_blob_null)
 Values
   (NULL, 2123, 121.23, NULL, NULL, 
    123, NULL, 232.33, 23.3432342, NULL, 
    23.3432342, NULL, 'asfsdf23 afdafr erwe rwerwr er w', NULL, TO_DATE('08/16/2011 17:30:12', 'MM/DD/YYYY HH24:MI:SS'), 
    NULL, 'Y', NULL, 'asfsdf23 afdafr erwe rwerwr er w', NULL, 
    NULL, NULL);
    
 COMMIT;
