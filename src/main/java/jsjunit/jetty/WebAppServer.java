/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package jsjunit.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * A server responsible for obtaining results from an execution.
 */
public final class WebAppServer {
	/**
	 * The Jetty server instance.
	 */
	private final Server webServer;

	public WebAppServer(int port, String contextPath, String webapp) {
		this.webServer = new Server(port);

		WebAppContext context = new WebAppContext(webapp, contextPath);
		
		HandlerList handlers = new HandlerList();
		handlers.addHandler(context);
		
		
		ServletContextHandler resultHandler = new ServletContextHandler(handlers, "/jsjunit");
		resultHandler.addServlet(ResultServlet.class, "/result");
		
		this.webServer.setHandler(handlers);
	}

	private boolean started = false;

	/**
	 * Start the server.
	 * 
	 * @throws Exception
	 *             if something goes wrong.
	 */
	public void start() throws Exception {
		if (started) {
			return;
		}

		webServer.start();
		started = true;
	}

	/**
	 * Stop the server.
	 * 
	 * @throws Exception
	 *             if something goes wrong.
	 */
	public void stop() throws Exception {
		if (!started) {
			return;
		}
		
		webServer.stop();
		started = false;
	}

}
