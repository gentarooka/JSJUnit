package jsjunit.qunit;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QUnitResult {
	@JsonProperty("failed")
	private int failed;
	@JsonProperty("passed")
	private int passed;
	@JsonProperty("total")
	private int total;
	@JsonProperty("runtime")
	private int runtime;
	private List<QUnitTestResult> children;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class QUnitTestResult {
		@JsonProperty("module")
		private String module;
		@JsonProperty("name")
		private String name;
		@JsonProperty("failed")
		private int failed;
		@JsonProperty("passed")
		private int passed;
		@JsonProperty("total")
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

	public void setChildren(List<QUnitTestResult> children) {
		this.children = children;
	}

}
