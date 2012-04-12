package jsjunit.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jsjunit.qunit.QUnitResult;
import jsjunit.qunit.QUnitResult.QUnitTestResult;
import jsjunit.qunit.QUnitResult.QUnitTestResult.QUnitTestDetail;

import org.junit.Test;

public class ResultServiceTest {

	@Test
	public void test() {
		QUnitResult result = new ResultService()
				.createResult(
						"{\"failed\":1,\"passed\":2,\"total\":3,\"runtime\":18,"
								+ "\"children\":["
								+ "{\"name\":\"test case does not exist\",\"failed\":1,\"passed\":0,\"total\":1},"
								+ "{\"name\":\"test case does not exist\",\"failed\":0,\"passed\":2,\"total\":2}"
								+ "]}", QUnitResult.class);

		assertThat(result.getFailed(), is(1));
		assertThat(result.getPassed(), is(2));
		assertThat(result.getTotal(), is(3));
		assertThat(result.getRuntime(), is(18));
		assertThat(result.getChildren().size(), is(2));

		List<QUnitTestResult> children = result.getChildren();

		QUnitTestResult childA = children.get(0);

		assertThat(childA.getName(), is("test case does not exist"));
		assertThat(childA.getFailed(), is(1));
		assertThat(childA.getPassed(), is(0));

		QUnitTestResult childB = children.get(1);

		assertThat(childB.getName(), is("test case does not exist"));
		assertThat(childB.getFailed(), is(0));
		assertThat(childB.getPassed(), is(2));

	}

	@Test
	public void detail() {

		String base = "{\"failed\":0,\"passed\":1,\"total\":1,\"runtime\":15," +
				"\"children\":[{\"name\":\"get menu\",\"failed\":0,\"passed\":1,\"total\":1," +
				"\"details\":[" +
				"{\"result\":true,\"message\":\"We expect value to be TEST MENU\",\"actual\":\"TEST MENU\",\"expected\":\"TEST MENU\"}" +
				"]}]}";
		
		QUnitResult result = new ResultService()
				.createResult(base , QUnitResult.class);

		assertThat(result.getFailed(), is(0));
		assertThat(result.getPassed(), is(1));
		assertThat(result.getTotal(), is(1));
		assertThat(result.getRuntime(), is(15));
		assertThat(result.getChildren().size(), is(1));

		List<QUnitTestResult> children = result.getChildren();

		QUnitTestResult childA = children.get(0);

		assertThat(childA.getName(), is("get menu"));
		assertThat(childA.getFailed(), is(0));
		assertThat(childA.getPassed(), is(1));
		
		List<QUnitTestDetail> details = childA.getDetails();
		assertThat(details.size(), is(1));
		
		QUnitTestDetail detail = details.get(0);
		
		assertThat(detail.isResult(), is(true));
		assertThat(detail.getMessage(), is("We expect value to be TEST MENU"));
		assertThat(detail.getActual(), is("TEST MENU"));
		assertThat(detail.getExpected(), is("TEST MENU"));
		
		

	}
}
