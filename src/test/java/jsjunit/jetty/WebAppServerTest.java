package jsjunit.jetty;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;



public class WebAppServerTest {
	
	@Test
	public void testIndexHtml() throws Exception {
		WebAppServer server = new WebAppServer(8234, "/test","./src/test/webapp");
		
		server.start();
		
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod("http://localhost:8234/test/index.html");
		
		int status = client.executeMethod(get);
		
		assertThat(status, is(200));
		
		byte[] body = get.getResponseBody();
		assertThat(new String(body), is("<html><body>Hello</body></html>"));
		
		server.stop();
	}
	
	@Test
	public void testIndexJsp() throws Exception {
		WebAppServer server = new WebAppServer(8234, "/test","./src/test/webapp");
		
		server.start();
		
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod("http://localhost:8234/test/index.jsp");
		
		int status = client.executeMethod(get);
		
		byte[] body = get.getResponseBody();
		
		assertThat(status, is(200));
		assertThat(new String(body).replaceAll(System.getProperty("line.separator"), ""), is("<html><body>/test</body></html>"));
		
		server.stop();
	}

}
