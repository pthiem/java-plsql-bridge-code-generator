package quickstart;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import plsql_bridge.pkg_plsql_bridge_test.P_get_cursor_recordtype_arguments;
import plsql_bridge.pkg_plsql_bridge_test.P_get_cursor_rowtype_arguments;
import plsql_bridge.pkg_plsql_bridge_test.P_get_cursor_untyped_arguments;
import plsql_bridge.pkg_plsql_bridge_test.P_test_number_in_out_arguments;
import plsql_bridge.pkg_plsql_bridge_test.P_test_types_in_simple_arguments;
import plsql_bridge.pkg_plsql_bridge_test.P_test_types_out_simple_arguments;
import plsql_bridge.pkg_plsql_bridge_test.Pkg_plsql_bridge_test;

/**
 * Can be run either as a servlet or as a command line programv (and via a JUnit harness).
 */
@WebServlet(urlPatterns = { "/" })
public class Main extends HttpServlet {

	PrintStream out;
	String newLineSuffix = "<br />";

	@EJB
	Pkg_plsql_bridge_test pkg_plsql_bridge_test;

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.pkg_plsql_bridge_test = new Pkg_plsql_bridge_test();
		main.out = System.out;
		main.newLineSuffix = "";
		main.runAll();
	}

	public void println(String s) {
		out.println(s + newLineSuffix);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		out = new PrintStream(response.getOutputStream());
		println("<html><body>");
		try {
			runAll();
		} catch (Exception e) {
			throw new ServletException(e);
		}
		println("</body></html>");
		out.close();

	}

	public void runAll() throws Exception {
		p_get_cursor_recordtype();
		p_get_cursor_rowtype();
		p_get_cursor_untyped();
		p_test_types_in_simple();
		p_test_types_out_simple();
		p_test_number_in_out();
	}

	public void p_get_cursor_recordtype() throws Exception {

		println("Cursor typed with recordtype.");

		P_get_cursor_recordtype_arguments arguments = new P_get_cursor_recordtype_arguments();

		arguments.setA_number(20L);

		pkg_plsql_bridge_test.p_get_cursor_recordtype(arguments);

		for (P_get_cursor_recordtype_arguments.Acur_recordtype o : arguments
				.getAcur_recordtype()) {
			println("1:" + o.getFld_number_value() + "," + o.getFld_varchar2());
		}

	}

	public void p_get_cursor_rowtype() throws Exception {

		println("Cursor typed with rowtype.");

		P_get_cursor_rowtype_arguments arguments = new P_get_cursor_rowtype_arguments();

		arguments.setA_number(20L);

		pkg_plsql_bridge_test.p_get_cursor_rowtype(arguments);

		for (P_get_cursor_rowtype_arguments.Acur_rowtype o : arguments
				.getAcur_rowtype()) {
			println("2:" + o.getFld_number_value() + "," + o.getFld_varchar2());
		}

	}

	public void p_get_cursor_untyped() throws Exception {

		println("Cursor, untyped.");

		P_get_cursor_untyped_arguments arguments = new P_get_cursor_untyped_arguments();

		arguments.setA_number(20L);

		pkg_plsql_bridge_test.p_get_cursor_untyped(arguments);

		for (Map map : arguments.getAcur_uptyped()) {
			println("3:" + map);
		}

	}

	public void p_test_types_in_simple() throws Exception {

		println("Test, simple inputs.");

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

		println("4:" + arguments.getResult());

	}

	public void p_test_types_out_simple() throws Exception {

		println("Test, simple outputs.");

		P_test_types_out_simple_arguments arguments = new P_test_types_out_simple_arguments();

		pkg_plsql_bridge_test.p_test_types_out_simple(arguments);

		println("5:" + arguments.getFld_varchar2() + ","
				+ arguments.getFld_date() + "," + arguments.getFld_double()
				+ "," + arguments.getFld_float() + ","
				+ arguments.getFld_number_float() + ","
				+ arguments.getFld_number_value() + ","
				+ arguments.getFld_numeric_value() + ","
				+ arguments.getFld_char().trim());

	}

	public void p_test_number_in_out() throws Exception {

		println("Test, in out fields.");

		P_test_number_in_out_arguments arguments = new P_test_number_in_out_arguments();

		arguments.setFld_number_in_value_out_null(901L);
		arguments.setFld_number_in_value_out_value(902L);

		pkg_plsql_bridge_test.p_test_number_in_out(arguments);

		println("6:" + arguments.getFld_number_in_null_out_null() + ","
				+ arguments.getFld_number_in_null_out_value() + ","
				+ arguments.getFld_number_in_value_out_null() + ","
				+ arguments.getFld_number_in_value_out_value());

		println("6:" + arguments.getResult());

	}

}
