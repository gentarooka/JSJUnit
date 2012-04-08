package jsjunit.service;

import java.io.File;

import jsjunit.jetty.ResultServlet;
import jsjunit.jetty.WebAppServer;

public class ServerService {

	private final WebAppServer server;

	public ServerService(int port, String contextPath, File webAppBase) {
		this.server = new WebAppServer(port, contextPath, webAppBase.getAbsolutePath());
	}

	public void startServer() {
		try {
			server.start();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

	}
	
	public String getReuslt() {
		String result = ResultServlet.getReult();
		return result;
	}

	public void stopServer() {
		try {
			server.stop();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
