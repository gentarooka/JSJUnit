package jsjunit;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

public class FileNameUtilsTest {
	
	@Test
	public void test() {
		System.out.println(FilenameUtils.wildcardMatch("src/main/simple.jsp", "**/*.jsp"));
		System.out.println(FilenameUtils.wildcardMatch("simple.jsp", "**/*.jsp"));
	}

}
