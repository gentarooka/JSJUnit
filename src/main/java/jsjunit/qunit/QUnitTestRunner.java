package jsjunit.qunit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import jsjunit.TestJS;
import jsjunit.TestPage;
import jsjunit.qunit.QUnitResult.QUnitTestResult;
import jsjunit.qunit.QUnitResult.QUnitTestResult.QUnitTestDetail;
import jsjunit.service.ResultService;
import jsjunit.service.RunnerService;
import jsjunit.service.ServerService;
import jsjunit.service.ServiceFactory;

import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class QUnitTestRunner extends ParentRunner<TestJS> {

	private final RunnerService runnerService;
	private final ServerService serverService;
	private final URL url;

	public QUnitTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		
		ServiceFactory factory = new ServiceFactory(testClass);
		
		runnerService = factory.getRunnerService();
		serverService = factory.getServerService();

		TestPage testPage = getTestPage();
		url = createURL(testPage.url());
	}
	
	private TestPage getTestPage() {
		return getTestClass().getJavaClass().getAnnotation(TestPage.class);
	}

	@Override
	protected String getName() {
		TestPage testPage = getTestPage();
		return createURL(testPage.url()).toString();
	}
	
	@Override
	protected Statement classBlock(final RunNotifier notifier) {
		Statement statement= childrenInvoker(notifier);
		statement = beforeTests(statement); //2
		statement= withBeforeClasses(statement); //1
		statement = afterTests(statement); //3
		statement= withAfterClasses(statement); //4
		return statement;
	}

	private Statement afterTests(final Statement statement) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				try {
					// Evaluate all that comes before this point.
					statement.evaluate();
				} finally {
					serverService.stopServer();
				}
			}
		};
	}

	private Statement beforeTests(final Statement statement) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				serverService.startServer();
				// Evaluate the remaining statements.
				statement.evaluate();
			}
		};
	}

	@Override
	protected List<TestJS> getChildren() {
		TestPage webPages = getTestPage();
		return Arrays.asList(webPages.tests());
	}

	@Override
	protected Description describeChild(TestJS child) {
		StringBuilder sb = new StringBuilder();
		for (String js : child.value()) {
			if (sb.length() == 0) {
				sb.append(js);
			} else {
				sb.append(",");
				sb.append(js);
			}
		}

		return Description.createTestDescription(getTestClass().getJavaClass(),
				" [" + sb.toString() + "]");
	}

	@Override
	protected void runChild(TestJS child, RunNotifier notifier) {
		EachTestNotifier eachNotifier = new EachTestNotifier(notifier,
				describeChild(child));

		try {
			eachNotifier.fireTestStarted();

			runnerService.runChild(url, child.value());
			String result = serverService.getReuslt();

			QUnitResult testResult = new ResultService().createResult(result,
					QUnitResult.class);

			if (testResult.getFailed() > 0) {
				JSTestFailure failure = new JSTestFailure(describeChild(child),
						url, buildMessage(testResult));
				notifier.fireTestFailure(failure);
			}
		} catch (Exception e) {
			eachNotifier.addFailure(e);
		} finally {
			eachNotifier.fireTestFinished();
		}
	}

	private static final String CR = System.getProperty("line.separator");

	private String buildMessage(QUnitResult result) {
		StringBuilder sb = new StringBuilder();

		sb.append("TEST FAILED : " + result.getPassed() + "/"
				+ result.getTotal() + CR);
		for (QUnitTestResult tr : result.getChildren()) {
			if (tr.getModule() != null) {
				sb.append("[");
				sb.append(tr.getModule());
				sb.append("] ");
			}
			sb.append(tr.getName() + " (" + tr.getPassed() + "/"
					+ tr.getTotal() + ")" + CR);
			
			for (QUnitTestDetail detail : tr.getDetails()) {
				if (!detail.isResult()) {
					sb.append("    " + detail.getMessage());
					sb.append(": expected '" + detail.getExpected() +"' but actual '"+  detail.getActual() +"'"+ CR);
				}
			}
		}

		return sb.toString();
	}

	/**
	 * A JavaScript execution failure object.
	 */
	private static class JSTestFailure extends Failure {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private final URL url;

		public JSTestFailure(Description description, URL url, String message) {
			super(description, new RuntimeException(message));
			this.url = url;
		}

		@Override
		public String getTestHeader() {
			return url.toString();
		}

		@Override
		public String getTrace() {
			// The stack means nothing here.
			return getMessage();
		}

		@Override
		public String toString() {
			return getTestHeader();
		}

	}

	private URL createURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
