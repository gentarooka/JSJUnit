package jsjunit.jetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResultServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		BufferedReader reader = new BufferedReader(req.getReader());

		StringBuilder build = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			build.append(line).append("\n");
		}

		String result = build.toString();
		synchronized (resultQue) {
			resultQue.push(result);
			resultQue.notify();
		}
	}

	private static LinkedList<String> resultQue = new LinkedList<String>();

	public static String getReult() {
		String result = null;
		synchronized (resultQue) {
			
			while(resultQue.size() == 0) {
				try {
					resultQue.wait(1000);
				} catch (InterruptedException e) {
					
				}
			}
			
			result = resultQue.removeFirst();

			if (resultQue.size() > 0) {
				throw new IllegalStateException();
			}
		}

		return result;
	}

}
