package be.kuleuven.mume.servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.kuleuven.mume.PMF;
import be.kuleuven.mume.shared.Persoon;
import be.kuleuven.mume.shared.Vak;

public class VakkenServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9165621779682288688L;
	private static final HashMap<String, String> regex = new HashMap<String, String>();

	public VakkenServlet() {
		VakkenServlet.regex.put("q", "view");
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Persoon p = Persoon.getCurrentPersoon(pm, req, resp, true);
		if(p==null)
			return;
			
		Extent<Vak> query = pm.getExtent(Vak.class);
		for (Vak vak : query) {
			resp.getWriter().write(vak.toString() + "\n");
		}
	}
}
