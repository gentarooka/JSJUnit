package jsjunit.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jsjunit.qunit.QUnitResult;
import jsjunit.qunit.QUnitResult.QUnitTestResult;

import org.junit.Test;

public class ResultServiceTest {

	@Test
	public void test() {
		QUnitResult result = new ResultService()
				.createResult(
						"{\"failed\":1,\"passed\":2,\"total\":3,\"runtime\":18," +
						"\"children\":[" +
						"{\"name\":\"test case does not exist\",\"failed\":1,\"passed\":0,\"total\":1}," +
						"{\"name\":\"test case does not exist\",\"failed\":0,\"passed\":2,\"total\":2}" +
						"]}",
						QUnitResult.class);
		
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
}
