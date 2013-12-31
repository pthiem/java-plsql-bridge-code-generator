package plsql_bridge;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/*
 * Test generation of output.
 * Note: 
 * 	Use quickstart to initialize the database
 *  Edit CodeGenerator.properties with correct values for the database.
 *  This test expects to be run as schema "core".
 *  
 */
public class PlsqlJavaBridgeGeneratorTest {

	@Test
	public void test() throws Exception {
		PlsqlJavaBridgeGenerator.main(new String[] {
				"src/test/config/CodeGenerator.properties", "core",
				"pkg_plsql_bridge_test" });

		for (String fileName : new String[] { "Pkg_plsql_bridge_test.java",
				"P_get_cursor_recordtype_arguments.java",
				"P_get_cursor_rowtype_arguments.java",
				"P_get_cursor_untyped_arguments.java",
				"P_test_blob_simple_arguments.java",
				"P_test_clob_simple_arguments.java",
				"P_test_date_simple_arguments.java",
				"P_test_float_simple_arguments.java",
				"P_test_number_decimal_simple_arguments.java",
				"P_test_number_in_out_arguments.java",
				"P_test_number_simple_arguments.java",
				"P_test_numeric_simple_arguments.java",
				"P_test_types_in_simple_arguments.java",
				"P_test_types_out_simple_arguments.java",
				"P_test_varchar2_simple_arguments.java" }) {
			System.out.println("Comparing: " + fileName);
			assertTrue("The files differ!", FileUtils.contentEquals(new File(
					"../runtime/src/test/java/plsql_bridge/pkg_plsql_bridge_test/"
							+ fileName), new File(
					"src/test/expected-results/java/plsql_bridge/pkg_plsql_bridge_test/"
							+ fileName)));
		}

		for (String fileName : new String[] { "package_metadata.xml" }) {
			System.out.println("Comparing: " + fileName);
			assertTrue("The files differ!", FileUtils.contentEquals(new File(
					"../runtime/src/test/resources/plsql_bridge/pkg_plsql_bridge_test/"
							+ fileName), new File(
					"src/test/expected-results/resources/plsql_bridge/pkg_plsql_bridge_test/"
							+ fileName)));
		}

	}

}
