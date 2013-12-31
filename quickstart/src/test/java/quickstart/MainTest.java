package quickstart;

import org.junit.Test;

/*
 * Simple class to allow for running main program from maven goal test.
 */
public class MainTest {

	@Test
	public void mainTest() throws Exception {
		Main.main(null);
	}

}
