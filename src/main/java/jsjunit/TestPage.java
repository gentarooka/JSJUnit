package jsjunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TestPage {
	/**
	 * <p>URL of page which you want to test. You have to specify full URL with scheme name, host name and port number.
	 * <p>example: <code>&quot;http://localhost:8234/test/index.html&quot;</code>
	 * @see {@link Server#webapp()}
	 * @see {@link WebApp}
	 */
	String url();
	TestJS[] tests();
}
