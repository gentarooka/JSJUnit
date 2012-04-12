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

		@JsonProperty("details")
		private List<QUnitTestDetail> details;

		public List<QUnitTestDetail> getDetails() {
			return details;
		}

		public void setDetails(List<QUnitTestDetail> details) {
			this.details = details;
		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class QUnitTestDetail {
			@JsonProperty("actual")
			private String actual;
			@JsonProperty("expected")
			private String expected;
			@JsonProperty("message")
			private String message;
			@JsonProperty("result")
			private boolean result;
			@JsonProperty("source")
			private String source;
			
			public String getActual() {
				return actual;
			}
			public String getExpected() {
				return expected;
			}
			public String getMessage() {
				return message;
			}
			public boolean isResult() {
				return result;
			}
			public String getSource() {
				return source;
			}
			
		}

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
