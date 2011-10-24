package be.kuleuven.mume.servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import be.kuleuven.mume.PMF;
import be.kuleuven.mume.shared.FieldVerifier;
import be.kuleuven.mume.shared.FormatException;
import be.kuleuven.mume.shared.Persoon;
import be.kuleuven.mume.shared.Vak;
import be.kuleuven.mume.shared.Vraag;

public class VraagServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5522536807910452864L;
	private static final HashMap<String, String> regex = new HashMap<String, String>();
<<<<<<< HEAD
	
	public VraagServlet(){
		VraagServlet.regex.put("q", "add|reply|get");
		VraagServlet.regex.put("text","[^\\n]+");
		VraagServlet.regex.put("vakid","\\p{Graph}+");
		VraagServlet.regex.put("replytoid", "\\p{Graph}+");
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Persoon p = Persoon.getCurrentPersoon(req, resp, true);
		if(p==null)
			return;
		
		try{
			String q = FieldVerifier.getParam(VraagServlet.regex, req, resp, "q");
			String text = FieldVerifier.getParam(VraagServlet.regex, req, resp, "text");
			String vakId = FieldVerifier.getParam(VraagServlet.regex, req, resp, "vakid");
			Vraag vraag;
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			
			Key vakkey = KeyFactory.stringToKey(vakId);
			Vak vak = pm.getObjectById(Vak.class, vakkey);
			
			if(q.equals("add")||q.equals("respond"))
			{
				vraag = new Vraag();
				vraag.setFromUser(p);
				vraag.setVak(vak);
				vraag.setText(text);
				
				if(q.equals("respond"))
				{
					String replyToId = FieldVerifier.getParam(VraagServlet.regex, req, resp, "replytoid");
					Key key = KeyFactory.stringToKey(replyToId);
					Vraag replyTo = pm.getObjectById(Vraag.class, key);
					vraag.setReplyTo(replyTo);
				}
			}
			
=======

	public VraagServlet() {
		VraagServlet.regex.put("q", "add|respond|get");
		VraagServlet.regex.put("text", "[^\\n]+");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		try {
			String q = FieldVerifier.getParam(VraagServlet.regex, req, resp,
					"q");
>>>>>>> mobilenvi/master
			resp.getWriter().write("q=" + q);
		} catch (FormatException e) {
			resp.getWriter().write(e.getMessage());
			resp.setStatus(400);
		}
	}
}
