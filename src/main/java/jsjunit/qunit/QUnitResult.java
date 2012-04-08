package jsjunit.qunit;

import java.util.List;

public class QUnitResult {
	private int failed;
	private int passed;
	private int total;
	private int runtime;
	private List<QUnitTestResult> children;
	
	public static class QUnitTestResult {
		private String module;
		private String name;
		private int failed;
		private int passed;
		private int total;
		
		public String getModule() {
			return module;
		}
		public String getName() {
			return name;
		}
		public int getFailed() {
			return failed;
		}
		public int getPassed() {
			return passed;
		}
		public int getTotal() {
			return total;
		}
	}

	public int getFailed() {
		return failed;
	}

	public int getPassed() {
		return passed;
	}

	public int getTotal() {
		return total;
	}

	public int getRuntime() {
		return runtime;
	}

	public List<QUnitTestResult> getChildren() {
		return children;
	}
	
}
