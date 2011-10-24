package be.kuleuven.mume.servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.kuleuven.mume.shared.FieldVerifier;
import be.kuleuven.mume.shared.FormatException;

public class VraagServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5522536807910452864L;
	private static final HashMap<String, String> regex = new HashMap<String, String>();

	public VraagServlet() {
		VraagServlet.regex.put("q", "add|respond|get");
		VraagServlet.regex.put("text", "[^\\n]+");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		try {
			String q = FieldVerifier.getParam(VraagServlet.regex, req, resp,
					"q");
			resp.getWriter().write("q=" + q);
		} catch (FormatException e) {
			resp.getWriter().write(e.getMessage());
			resp.setStatus(400);
		}
	}
}
