package jsjunit.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public final class PhantomJS {

	private final String phantomJsPath;
	
	private static class ProcessDestroyer extends TimerTask {

		private Process p;

		public ProcessDestroyer(Process p) {
			this.p = p;
		}

		@Override
		public void run() {
			p.destroy(); 
		}
	}

	private final Process process;

	private final Thread outThread;
	private final Thread errThread;

	public PhantomJS(OutputStream out, OutputStream err, String... args)
			throws IOException {
		String phantomJsPath = System.getProperty("phantomjs.path", "");
		
		if (phantomJsPath.isEmpty()) {
			this.phantomJsPath = phantomJsPath;
		} else if (phantomJsPath.endsWith("/")){
			this.phantomJsPath = phantomJsPath;
		} else {
			this.phantomJsPath = phantomJsPath + "/";
		}
		
		
		List<String> commands = new ArrayList<String>();
		commands.add(this.phantomJsPath + "phantomjs");
		for (String arg : args) {
			commands.add(arg);
		}
		
		ProcessBuilder processBuilder = new ProcessBuilder(
				commands.toArray(new String[commands.size()]));

		process = processBuilder.start();
		
		TimerTask task = new ProcessDestroyer(process);
		Timer timer = new Timer();
		timer.schedule(task, TimeUnit.SECONDS.toMillis(10));

		outThread = new Thread(new Pipe(new BufferedReader(
				new InputStreamReader(process.getInputStream())),
				new BufferedWriter(new OutputStreamWriter(out))));
		
		errThread = new Thread(new Pipe(new BufferedReader(
				new InputStreamReader(process.getErrorStream())),
				new BufferedWriter(new OutputStreamWriter(err))));

		outThread.start();
		errThread.start();
	}

	public void destroy() {
		while (true) {
			try {
				process.waitFor();
				outThread.join();
				errThread.join();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static class Pipe implements Runnable {

		public Pipe(BufferedReader reader, BufferedWriter writer) {
			this.reader = reader;
			this.writer = writer;
		}

		private final BufferedReader reader;
		private final BufferedWriter writer;

		@Override
		public void run() {
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					writer.write(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}

	}

}
