package jsjunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TestJS {
	/**
	 * <p>path to JavaScript files which will be injected into HTML.
	 * @see http://code.google.com/p/phantomjs/wiki/Interface#injectJs(filename)
	 */
	String[] value();

	/**
	 * <p>expected count of test. Default value is -1 (no expectation).
	 */
	int total() default -1;
}
