package jsjunit.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RunnerService {
	
	private File runner;
	
	public RunnerService(File runner) {
		this.runner = runner;
	}

	public void runChild(URL url, String... js) throws IOException {
		runChild(url, runner.getAbsolutePath(), js);
	}

	private void runChild(URL url, String runner, String... js) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream err = new ByteArrayOutputStream();

		
		List<String> args = new ArrayList<String>();
		args.add(runner);
		args.add(url.toString());
		for (String jsPath : js) {
			args.add(new File(jsPath).getAbsolutePath());
		}

		PhantomJS phantom = null;
		try {
			phantom = new PhantomJS(out, err, args.toArray(new String[args.size()]));
		} finally {
			if (phantom != null) {
				phantom.destroy();
			}
		}

		// FIXME
		System.out.print(new String(out.toByteArray()));
	}
	
}
