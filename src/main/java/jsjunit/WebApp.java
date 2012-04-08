package jsjunit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WebApp {
	String contextPath();
	String base();
	String resourceBase();
	String[] include();
}
