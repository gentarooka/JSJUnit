package jsjunit.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jsjunit.service.PhantomJS;

import org.junit.Test;

public class PhantomJSTest {
	
	private static final String CR = System.getProperty("line.separator");
	@Test
	public void hello() throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		PhantomJS js = new PhantomJS(output, System.err, "./src/test/js/hello.js");
		
		js.destroy();
		
		String str = new String(output.toByteArray());
		
		assertThat(str, is("hello world" + CR));
	}
	
	@Test
	public void helloTwice() throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		PhantomJS js = new PhantomJS(output, System.err, "./src/test/js/hello_twice.js");
		
		js.destroy();
		
		String str = new String(output.toByteArray());
		
		assertThat(str, is("hello world"+CR+"hello world"+CR));
	}

}
