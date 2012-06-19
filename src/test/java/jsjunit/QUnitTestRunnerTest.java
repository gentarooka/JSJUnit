package jsjunit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jsjunit.qunit.QUnitTestRunner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

public class QUnitTestRunnerTest {
	private static final String CR = System.getProperty("line.separator");

	@RunWith(QUnitTestRunner.class)
	@Workspace(workspace = "./target/qunit-test-runner", clean = true)
	@TestPage(url = "http://localhost:8234/test/index.jsp", 
		tests = {
			@TestJS("./src/test/js/helloTest.js"),
			@TestJS({"./src/test/js/helloTest.js","./src/test/js/helloTest.js"}),
			@TestJS({"./src/test/js/helloSuccessTest.js"})
		}
	)
	@Server(port = 8234, webapp =  
			@WebApp(contextPath="/test", base = "./src/test/webapp", resourceBase = "./src/test/resources/samplemain", include = { "**/*.jsp", "**/*.js", "*.jsp", "*.js" }) 
	)
	public static class TestClass {

		@BeforeClass
		public static void setUpClass() {
			System.out.println("beforeClass");
		}
		
		@AfterClass
		public static void tearDownClass() {
			System.out.println("afterClass");
		}

	}

	@Test
	public void normal() {
		Result result = JUnitCore.runClasses(TestClass.class);
		assertThat(result.getFailureCount(), is(2));
		
		String expected0 = 
		"TEST FAILED : 5/6"+CR+
		"[Module B] some other test (1/2)"+CR+
		"    failing test: expected 'false' but actual 'true'"+ CR +
		"[Module A] second test within module (1/1)"+CR+
		"[Module A] first test within module (1/1)"+CR+
		"a basic test example (2/2)"+CR;

		
		assertThat(result.getFailures().get(0).getMessage(), is(expected0));
		
		String expected1 = 
				"TEST FAILED : 10/12"+CR+
				"[Module B] some other test (1/2)"+CR+
				"    failing test: expected 'false' but actual 'true'"+CR+
				"[Module A] second test within module (1/1)"+CR+
				"[Module A] first test within module (1/1)"+CR+
				"[Module B] a basic test example (2/2)"+CR+
				"[Module B] some other test (1/2)"+CR+
				"    failing test: expected 'false' but actual 'true'"+CR+
				"[Module A] second test within module (1/1)"+CR+
				"[Module A] first test within module (1/1)"+CR+
				"a basic test example (2/2)"+CR;

		assertThat(result.getFailures().get(1).getMessage(), is(expected1));
		assertThat(result.getRunCount(), is(3));
	}
	
	@RunWith(QUnitTestRunner.class)
	@Workspace(workspace = "./target/qunit-test-runner", clean = true)
	@TestPage(url = "http://localhost:8234/test/index.jsp", 
		tests = {
			@TestJS("./src/test/js/helloEmptyTest.js")
		}
	)
	@Server(port = 8234, webapp =  
			@WebApp(contextPath="/test", base = "./src/test/webapp", resourceBase = "./src/test/resources/samplemain", include = { "**/*.jsp", "**/*.js", "*.jsp", "*.js" }) 
	)
	public static class TestEmptyClass {}
	
	@Test
	public void emptyJS() {
		Result result = JUnitCore.runClasses(TestEmptyClass.class);
		assertThat(result.getFailureCount(), is(0));
		assertThat(result.getRunCount(), is(1));
	}
	
	@RunWith(QUnitTestRunner.class)
	@Workspace(workspace = "./target/qunit-test-runner", clean = true)
	@TestPage(url = "http://localhost:8234/test/menu/menu.jsp", 
		tests = {
			@TestJS("./src/test/js/menu/menuTest.js"),
			@TestJS("./src/test/js/menu/menuTest.js")
		}
	)
	@Server(port = 8234, webapp =  
			@WebApp(contextPath="/test", base = "./src/test/webapp", resourceBase = "./src/test/resources/samplemain", include = { "**/*.jsp", "**/*.js", "*.jsp", "*.js" }) 
	)
	public static class TestJstlJSPClass {}
	
	@Test
	public void jstlJSP() {
		Result result = JUnitCore.runClasses(TestJstlJSPClass.class);
		assertThat(result.getFailureCount(), is(0));
		assertThat(result.getRunCount(), is(2));
	}
	
	@RunWith(QUnitTestRunner.class)
	@Workspace(workspace = "./target/qunit-test-runner", clean = true)
	@TestPage(url = "http://localhost:8234/test/menu/menu.jsp", 
		tests = {
			@TestJS("./src/test/js/menu/menuTest.js"),
			@TestJS("./src/test/js/menu/menuNotExist.js"),
			@TestJS({"./src/test/js/menu/menuTest.js", "./src/test/js/menu/menuNotExist.js"})
		}
	)
	@Server(port = 8234, webapp =  
			@WebApp(contextPath="/test", base = "./src/test/webapp", resourceBase = "./src/test/resources/samplemain", include = { "**/*.jsp", "**/*.js", "*.jsp", "*.js" }) 
	)
	public static class TestNotExistClass {}
	
	@Test
	public void notExist() {
		Result result = JUnitCore.runClasses(TestNotExistClass.class);
		assertThat(result.getFailureCount(), is(2));
		assertThat(result.getRunCount(), is(3));
		
		String expected = "TEST FAILED : 0/1" + CR +
		"test case does not exist (0/1)"+ CR +
		"    test case does not exist: expected 'null' but actual 'null'" + CR;

		assertThat(result.getFailures().get(0).getMessage(), is(expected));
		assertThat(result.getFailures().get(1).getMessage(), is(expected));
	}

	
	@RunWith(QUnitTestRunner.class)
	@Workspace(workspace = "./target/qunit-test-runner", clean = true)
	@TestPage(url = "http://localhost:8234/test/qunit.html", 
		tests = {
			@TestJS(value={"./src/test/js/error.js", "./src/test/js/helloSuccessTest.js"},total=6),
			@TestJS(value={"./src/test/js/error.js", "./src/test/js/helloSuccessTest.js"},total=4)
		}
	)
	@Server(port = 8234, webapp =  
			@WebApp(contextPath="/test", base = "./src/test/webapp", resourceBase = "./src/test/resources/samplemain", include = { "**/*.jsp", "**/*.js", "*.jsp", "*.js" }) 
	)
	public static class TestErrorClass {}
	
	@Test
	public void errorTest() {
		Result result = JUnitCore.runClasses(TestErrorClass.class);
		assertThat(result.getFailureCount(), is(1));
		assertThat(result.getRunCount(), is(2));
	}


}
