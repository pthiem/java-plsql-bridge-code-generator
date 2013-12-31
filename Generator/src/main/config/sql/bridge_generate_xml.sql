declare
    --
    -- This is great code.
    --
    -- Used oracle facilities to suck whatever data I want out of the DB and create in xml format.
    --
  xml_cursor SYS_REFCURSOR;
  xml_context DBMS_XMLGEN.ctxHandle;
  xml_clob clob;
  xml_blob blob;
  xml_varchar2 varchar2(30000);


  -- Very useful method.
  function CLOB_TO_BLOB (p_clob CLOB) return BLOB
  as
   l_blob          blob;
   l_dest_offset   integer := 1;
   l_source_offset integer := 1;
   l_lang_context  integer := DBMS_LOB.DEFAULT_LANG_CTX;
   l_warning       integer := DBMS_LOB.WARN_INCONVERTIBLE_CHAR;
  BEGIN
    DBMS_LOB.CREATETEMPORARY(l_blob, TRUE);
    DBMS_LOB.CONVERTTOBLOB
    (
     dest_lob    =>l_blob,
     src_clob    =>p_clob,
     amount      =>DBMS_LOB.LOBMAXSIZE,
     dest_offset =>l_dest_offset,
     src_offset  =>l_source_offset,
     blob_csid   =>DBMS_LOB.DEFAULT_CSID,
     lang_context=>l_lang_context,
     warning     =>l_warning
    );
    return l_blob;
  END;
  
  
begin
  open xml_cursor for
    -- get level 0
    select 
      object_name package_name, 
      procedure_name procedure_name,
      --
      initcap(object_name) package_name_initcap,
      lower(object_name) package_name_lower,
      upper(substr(object_name,1,1)) ||  lower(substr(object_name,2)) package_name_firstcap,
      initcap(procedure_name) procedure_name_initcap,
      lower(procedure_name) procedure_name_lower,
      upper(substr(procedure_name,1,1)) ||  lower(substr(procedure_name,2)) procedure_name_firstcap,
      --
      cursor(select 
        all_arguments.argument_name,
        all_arguments.data_level,
        all_arguments.data_type,
        all_arguments.data_scale,
        all_arguments.in_out,
        --
        initcap(argument_name) argument_initcap,
        lower(argument_name) argument_lower,
        upper(substr(argument_name,1,1)) ||  lower(substr(argument_name,2)) argument_firstcap,
        lower(substr(argument_name,1,1)) ||  initcap(substr(argument_name,2)) argument_firstlow,
        --
        (select data_level 
          from all_arguments aa2
          where 
          aa2.subprogram_id = all_arguments.subprogram_id
          and aa2.object_id = all_arguments.object_id
          and aa2.sequence = all_arguments.sequence +1) next_data_level,
        (select min(aa2.sequence) 
          from all_arguments aa2
          where 
          aa2.subprogram_id = all_arguments.subprogram_id
          and aa2.object_id = all_arguments.object_id
          and aa2.sequence > all_arguments.sequence
          and aa2.data_level in (0,2)
          ) next_seq_level,  
        -- get level 1
        cursor(
          select 
          aa2.argument_name,
          aa2.data_level,
          aa2.data_type,
          aa2.data_scale,
          aa2.in_out,
          --
          initcap(argument_name) argument_initcap,
          lower(argument_name) argument_lower,
          upper(substr(argument_name,1,1)) ||  lower(substr(argument_name,2)) argument_firstcap,
          lower(substr(argument_name,1,1)) ||  initcap(substr(argument_name,2)) argument_firstlow,
          --
          -- get level 2
          cursor(
            select 
            aa3.argument_name,
            aa3.data_level,
            aa3.data_type,
            aa3.data_scale,
            aa3.in_out,
            --
            initcap(argument_name) argument_initcap,
            lower(argument_name) argument_lower,
            upper(substr(argument_name,1,1)) ||  lower(substr(argument_name,2)) argument_firstcap,
            lower(substr(argument_name,1,1)) ||  initcap(substr(argument_name,2)) argument_firstlow
            --
            from all_arguments aa3 
            where aa3.owner = all_procedures.owner
            and aa3.package_name is not null
            and aa3.data_level = 2
            and aa3.sequence > aa2.sequence
            and aa3.subprogram_id = aa2.subprogram_id
            and aa3.object_id = aa2.object_id
            -- Todo 
            -- Sequences is one greater than current
            -- Data level or Sequence + 1 is 2
            /*
            and exists (select data_level 
              from all_arguments aai
              where 
              aai.subprogram_id = aa2.subprogram_id
              and aai.object_id = aa2.object_id
              and aai.sequence = aa2.sequence +1)
            */
            -- sequence is less than next record with level 0 or2
            and aa3.sequence <
              (select nvl(min(aai.sequence),1000) 
                from all_arguments aai
                where 
                aai.subprogram_id = aa2.subprogram_id
                and aai.object_id = aa2.object_id
                and aai.sequence > aa2.sequence
                and aai.data_level in (0,1,3)
                )  
              order by package_name, subprogram_id, object_name, sequence,  position
            ) arguments
            -- end of level 2
          from all_arguments aa2 
          where aa2.owner = all_procedures.owner 
          and aa2.package_name is not null
          and aa2.data_level = 1
          and aa2.sequence > all_arguments.sequence
          and aa2.subprogram_id = all_arguments.subprogram_id
          and aa2.object_id = all_arguments.object_id
          -- data level or sequence + 1 is 1
          and exists (select data_level 
            from all_arguments aai
            where 
            aai.subprogram_id = all_arguments.subprogram_id
            and aai.object_id = all_arguments.object_id
            and aai.sequence = all_arguments.sequence +1)
          -- sequence is less than next record with level 0 or2
          and aa2.sequence <
            (select nvl(min(aai.sequence),1000) 
              from all_arguments aai
              where 
              aai.subprogram_id = all_arguments.subprogram_id
              and aai.object_id = all_arguments.object_id
              and aai.sequence > all_arguments.sequence
              and aai.data_level in (0,2)
              )  
          order by package_name, subprogram_id, object_name, sequence,  position
          ) arguments
         -- end of level 1
        from ALL_ARGUMENTS 
        where
        data_level = 0  
        and all_arguments.package_name = all_procedures.object_name
        and all_arguments.object_name = all_procedures.procedure_name
        order by package_name, subprogram_id, object_name, sequence,  position) arguments
      from all_procedures
      where owner = upper(:1)
      and object_name = upper(:2)
      -- remove functions
      and not exists (select 1 from all_arguments where all_arguments.package_name = all_procedures.object_name and all_arguments.object_name = all_procedures.procedure_name and all_arguments.position = 0)
      and procedure_name is not null
      order by object_name, procedure_name;

  xml_context := dbms_xmlgen.newContext(xml_cursor);
  
  dbms_xmlgen.setRowSetTag(xml_context, 'PROCEDURES');
  
  dbms_xmlgen.setRowTag(xml_context, 'PROCEDURE');
  
  xml_clob  := dbms_xmlgen.getXML(xml_context);
  
  dbms_xmlgen.closeContext(xml_context);
  
  :3 := xml_clob;
  
end;  





