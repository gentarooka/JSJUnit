package jsjunit.service;

import java.io.File;

import jsjunit.Server;
import jsjunit.Workspace;


public class ServiceFactory {
	
	private final RunnerService runnerService;
	private final ServerService serverService;
	
	public ServiceFactory(Class<?> testClass) {
		Workspace workspaceConfig = testClass.getAnnotation(Workspace.class);
		if (workspaceConfig == null) {
			throw new IllegalArgumentException("@" + Workspace.class.getSimpleName()+" does not exist on Test class.");
		}
		
		final WorkspaceManager manager = new WorkspaceManager(workspaceConfig.workspace(), "qunit-test-runner-workspace",
				workspaceConfig.clean());
		
		runnerService = new RunnerService(manager.getRunner());

		Server serverConfig = testClass.getAnnotation(Server.class);
		if (serverConfig == null) {
			throw new IllegalArgumentException("@" + Server.class.getSimpleName() + " does not exist on Test class.");
		}
		
		File webappBase = manager.setUpWebApp(serverConfig.webapp().base(),
				serverConfig.webapp().resourceBase(), serverConfig.webapp()
						.include());
		
		manager.getRunner();
		
		serverService = new ServerService(serverConfig.port(), serverConfig
				.webapp().contextPath(), webappBase);
	}
	
	public RunnerService getRunnerService() {
		return runnerService;
	}
	
	public ServerService getServerService() {
		return serverService;
	}

}
