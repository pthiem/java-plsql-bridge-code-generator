package plsql_bridge.pkg_plsql_bridge_test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/*
 * Simple class to allow for running tests from maven test.
 */
public class TestPkg_plsql_bridge_test {

	Pkg_plsql_bridge_test pkg_plsql_bridge_test = new Pkg_plsql_bridge_test();

	@Test
	public void p_get_cursor_recordtype() throws Exception {

		System.out.println("Cursor typed with recordtype.");

		P_get_cursor_recordtype_arguments arguments = new P_get_cursor_recordtype_arguments();

		arguments.setA_number(20L);

		pkg_plsql_bridge_test.p_get_cursor_recordtype(arguments);

		for (P_get_cursor_recordtype_arguments.Acur_recordtype o : arguments
				.getAcur_recordtype()) {
			System.out.println("1:" + o.getFld_number_value() + ","
					+ o.getFld_varchar2());
			Assert.assertEquals(Long.valueOf(2123), o.getFld_number_value());
			Assert.assertEquals("asfsdf23 afdafr erwe rwerwr er w",
					o.getFld_varchar2());
		}

	}

	@Test
	public void p_get_cursor_rowtype() throws Exception {

		System.out.println("Cursor typed with rowtype.");

		P_get_cursor_rowtype_arguments arguments = new P_get_cursor_rowtype_arguments();

		arguments.setA_number(20L);

		pkg_plsql_bridge_test.p_get_cursor_rowtype(arguments);

		for (P_get_cursor_rowtype_arguments.Acur_rowtype o : arguments
				.getAcur_rowtype()) {
			System.out.println("2:" + o.getFld_number_value() + ","
					+ o.getFld_varchar2());
			Assert.assertEquals(Long.valueOf(2123), o.getFld_number_value());
			Assert.assertEquals("asfsdf23 afdafr erwe rwerwr er w",
					o.getFld_varchar2());

		}

	}

	@Test
	public void p_get_cursor_untyped() throws Exception {

		System.out.println("Cursor, untyped.");

		P_get_cursor_untyped_arguments arguments = new P_get_cursor_untyped_arguments();

		arguments.setA_number(20L);

		pkg_plsql_bridge_test.p_get_cursor_untyped(arguments);

		for (Map map : arguments.getAcur_uptyped()) {
			System.out.println("3:" + map);
			String expected = "{fld_float=23.3432342, fld_number_float_null=null, fld_numeric_float_null=null, fld_double_null=null, fld_blob_null=null, fld_date=2011-08-16 17:30:12.0, fld_blob=null, fld_clob_null=null, fld_clob=asfsdf23 afdafr erwe rwerwr er w, fld_char_null=null, fld_numeric_null=null, fld_varchar2=asfsdf23 afdafr erwe rwerwr er w, fld_float_null=null, fld_varchar2_null=null, fld_number_value=2123, fld_date_null=null, fld_char=Y, fld_double=23.3432342, fld_number_float=121.23, fld_numeric_float_value=232.33, fld_number_null=null, fld_numeric_value=123}";
			String actual = map.toString();
			Assert.assertEquals(expected, actual);
		}

	}

	@Test
	public void p_test_types_in_simple() throws Exception {

		System.out.println("Test, simple inputs.");

		P_test_types_in_simple_arguments arguments = new P_test_types_in_simple_arguments();

		arguments.setFld_char("A");
		Calendar cal = new GregorianCalendar();
		cal.set(2012, 9, 24, 10, 50, 55);
		cal.getTime();
		arguments.setFld_date(cal.getTime());
		arguments.setFld_double(55.34);
		arguments.setFld_float(102.233);
		arguments.setFld_number_float(343.34);
		arguments.setFld_number_value(12L);
		arguments.setFld_numeric_value(343L);
		arguments.setFld_varchar2("Hello");

		pkg_plsql_bridge_test.p_test_types_in_simple(arguments);

		String expected = ",12,343.34,,,343,102.233,,55.34,,Hello,,24/OCT/12,,A,,";
		String actual = arguments.getResult();

		System.out.println("4:" + actual);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void p_test_types_out_simple() throws Exception {

		System.out.println("Test, simple outputs.");

		P_test_types_out_simple_arguments arguments = new P_test_types_out_simple_arguments();

		pkg_plsql_bridge_test.p_test_types_out_simple(arguments);

		String expected = "asdfa wer vb,2013-12-31 09:11:43.0,343.345,998.22,5444.23,343,343";
		String actual = arguments.getFld_varchar2() + ","
				+ arguments.getFld_date() + "," + arguments.getFld_double()
				+ "," + arguments.getFld_float() + ","
				+ arguments.getFld_number_float() + ","
				+ arguments.getFld_number_value() + ","
				+ arguments.getFld_numeric_value();

		System.out.println("5:" + actual);

		Assert.assertEquals(expected, actual);

		// Char parameter fields have length of full ~32,766 characters in
		// native PL/SQL and here too. Not handled efficiently by plsql. Not
		// recommended for performance.
		// Handled separately so can be trimmed and commented.
		String expectedChar = "Y";
		String actualChar = arguments.getFld_char().trim();
		System.out.println("5 char field:" + actualChar);

		Assert.assertEquals(expectedChar, actualChar);

	}

	@Test
	public void p_test_number_in_out() throws Exception {

		System.out.println("Test, in out fields.");

		P_test_number_in_out_arguments arguments = new P_test_number_in_out_arguments();

		arguments.setFld_number_in_value_out_null(901L);
		arguments.setFld_number_in_value_out_value(902L);

		pkg_plsql_bridge_test.p_test_number_in_out(arguments);

		String actualInOutParamaters = arguments
				.getFld_number_in_null_out_null()
				+ ","
				+ arguments.getFld_number_in_null_out_value()
				+ ","
				+ arguments.getFld_number_in_value_out_null()
				+ ","
				+ arguments.getFld_number_in_value_out_value();
		String expectedInOutParameters = "null,1001,null,2002";

		System.out.println("6:" + actualInOutParamaters);

		Assert.assertEquals(expectedInOutParameters, actualInOutParamaters);

		String expectedResults = ",,901,902";
		String actualresults = arguments.getResult();

		System.out.println("6:" + actualresults);

		Assert.assertEquals(expectedResults, actualresults);

	}

}
