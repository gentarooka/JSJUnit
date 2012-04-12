package jsjunit.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

final public class WorkspaceManager {

	private final File runner;
	private final File qunit;
	private final File dir;

	public WorkspaceManager(String path, String name, boolean clean) {
		dir = new File(path, name);

		if (clean) {
			clean(dir);
		}

		if (!dir.exists() && !dir.mkdirs()) {
			throw new IllegalStateException("can not create workspace : "
					+ dir.getAbsolutePath());
		}

		try {
			copyLibraries(dir);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		runner = new File(dir, QUNIT_RUNNER_NAME);
		if (!runner.exists()) {
			throw new IllegalStateException("can not fine runner");
		}

		qunit = new File(dir, QUNIT_NAME);
		if (!qunit.exists()) {
			throw new IllegalStateException("can not fine qunit");
		}
	}

	public File setUpWebApp(String base, String resourceBase, String[] includes) {
		try {
			File dest = setUpWebappFolder(base);
			if (!resourceBase.isEmpty()) {
				copyResources(resourceBase, includes, dest);
			}
			return dest;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private void copyResources(String resourceBase, String[] includes,
			File destDir) throws IOException {
		File srcDir = new File(resourceBase);
		
		FileUtils.copyDirectory(srcDir, destDir, new RecursiveWildcardFileFilter(includes), true);
	}
	
	private static class RecursiveWildcardFileFilter extends WildcardFileFilter {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RecursiveWildcardFileFilter(String[] wildcards) {
			super(wildcards);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean accept(File dir, String name) {
			if(new File(dir, name).isDirectory()) {
				return true;
			}
			return super.accept(dir, name);
		}

		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			}
			return super.accept(file);
		}
		
	}

	private File setUpWebappFolder(String base) throws IOException {
		File src = new File(base);
		if (!src.isDirectory()) {
			throw new IllegalArgumentException("base should be directory");
		}

		File dest = new File(dir, "webapp");
		if (dest.exists()) {
			delete(dest);
		}

		dest.mkdirs();

		FileUtils.copyDirectory(src, dest);
		return dest;
	}

	public File getRunner() {
		return runner;
	}

	public File getQunit() {
		return qunit;
	}

	private static final String QUNIT_RUNNER_NAME = "run-qunit.js";
	private static final String QUNIT_NAME = "qunit.js";

	private void copyLibraries(File dir) throws IOException {
		copy(dir, QUNIT_RUNNER_NAME);
		copy(dir, QUNIT_NAME);
	}

	private void copy(File dir, String name) throws IOException {
		InputStream in = null;
		try {
			in = this.getClass().getResourceAsStream("/" + name);

			if (in == null) {
				throw new IllegalStateException("can not find " + name);
			}

			BufferedInputStream is = new BufferedInputStream(in);

			File dest = new File(dir, name);
			if (!dest.exists()) {
				BufferedOutputStream bos = null;
				try {

					bos = new BufferedOutputStream(new FileOutputStream(dest));

					for (int data = is.read(); data > 0; data = is.read()) {
						bos.write(data);
					}
					bos.flush();
				} finally {
					if (bos != null) {
						bos.close();
					}
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}

	}

	private void clean(File dir) {
		if (dir.exists()) {
			delete(dir);
		}
	}

	private void delete(File f) {
		if (!f.exists()) {
			return;
		}

		if (f.isFile()) {
			f.delete();
		}

		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File file : files) {
				delete(file);
			}
			f.delete();
		}
	}

}
