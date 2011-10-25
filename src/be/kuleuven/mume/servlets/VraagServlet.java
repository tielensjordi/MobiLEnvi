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
import be.kuleuven.mume.shared.Antwoord;
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
	
	public VraagServlet(){
		VraagServlet.regex.put("q", "add|reply|get");
		VraagServlet.regex.put("text","[^\\n]+");
		VraagServlet.regex.put("vakid","\\p{Graph}+");
		VraagServlet.regex.put("replytoid", "\\p{Graph}+");
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Persoon p = Persoon.getCurrentPersoon(pm, req, resp, true);
		if(p==null)
			return;
		
		try{
			String q = FieldVerifier.getParam(VraagServlet.regex, req, resp, "q");
			
			if(q.equals("add"))
				addVraag(p, pm, req, resp);
			else if(q.equals("reply"))
				addReply(p, pm, req, resp);
			else if(q.equals("get"))
				getVragen(pm, req, resp);
			
		} catch (FormatException e) {
			resp.getWriter().write(e.getMessage());
			resp.setStatus(400);
		}
	}
	private void addReply(Persoon p, PersistenceManager pm,
			HttpServletRequest req, HttpServletResponse resp) throws FormatException, IOException {
		
		String text = FieldVerifier.getParam(VraagServlet.regex, req, resp, "text");
		String replyToId = FieldVerifier.getParam(VraagServlet.regex, req, resp, "replytoid");
		Key key = KeyFactory.stringToKey(replyToId);
		Vraag vraag = pm.getObjectById(Vraag.class, key);
		
		Antwoord a = new Antwoord(p,text,vraag);
		
		pm.close();
		
		resp.getWriter().write("Reply added" + a.toString());
	}
	private void getVragen(PersistenceManager pm, HttpServletRequest req,
			HttpServletResponse resp) throws IOException, FormatException {
		
		String vakId = FieldVerifier.getParam(VraagServlet.regex, req, resp, "vakid");
		Key key = KeyFactory.stringToKey(vakId);
		
		Vak vak = pm.getObjectById(Vak.class, key);
		
		StringBuilder str = new StringBuilder();
		for (Vraag v : vak.getVragen()) {
			str.append(v.toString());
			str.append("\n");
		}
		pm.close();
		
		resp.getWriter().write(str.toString());
	}
	private void addVraag(Persoon p, PersistenceManager pm, HttpServletRequest req,
			HttpServletResponse resp) throws FormatException, IOException {
		String text = FieldVerifier.getParam(VraagServlet.regex, req, resp, "text");
		String vakId = FieldVerifier.getParam(VraagServlet.regex, req, resp, "vakid");
		
		Key vakkey = KeyFactory.stringToKey(vakId);
		Vak vak = pm.getObjectById(Vak.class, vakkey);
		
		Vraag vraag = new Vraag(p, vak, text);
		
		pm.makePersistent(vraag);
		pm.close();
		
		resp.getWriter().write("Vraag stored successfully, id:" + KeyFactory.keyToString(vraag.getId()));
	}
}
