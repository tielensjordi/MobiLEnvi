package be.kuleuven.mume.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class VakServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3291252979009718051L;
	private static final HashMap<String, String> regex = new HashMap<String, String>();

	public VakServlet() {
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		if (VakServlet.regex.containsKey("name"))
			log.log(Level.WARNING, "Regex already contains ");

		VakServlet.regex.put("hashtag", "()|([\\p{Alnum}]{2,500}?)");
		VakServlet.regex.put("name", "[\\p{Alnum}\\s]{5,500}?");
		VakServlet.regex.put("q", "add|update|del");
		VakServlet.regex.put("vakid", "\\p{Graph}+");
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Persoon p = Persoon.getCurrentPersoon(req, resp, true);
		if(p==null)
			return;
		
		try{
			String q = FieldVerifier.getParam(VakServlet.regex, req,resp,"q");
			
			if(q.equals("add"))
				addVak(req, resp);
			else if(q.equals("update"))
				updateVak(req, resp);
			else if(q.equals("del"))
				deleteVak(req, resp);
			
		} catch (FormatException e) {
			resp.getWriter().write(e.getMessage());
			resp.setStatus(400);
		}
	}

	private void deleteVak(HttpServletRequest req, HttpServletResponse resp) throws FormatException, IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		String vakId = FieldVerifier.getParam(VakServlet.regex, req, resp, "vakid");

		Key key = KeyFactory.stringToKey(vakId);
		Vak vak = pm.getObjectById(Vak.class, key);
		
		//delete vak from db
		pm.deletePersistent(vak);
		
		pm.close();
		
		resp.getWriter().write("Deleted succesfully");
	}

	private void updateVak(HttpServletRequest req, HttpServletResponse resp) throws FormatException, IOException {
		String vakIdStr = FieldVerifier.getParam(VakServlet.regex, req, resp, "vakid");
		String name = FieldVerifier.getParam(VakServlet.regex, req, resp, "name");
		String hashTag = FieldVerifier.getParam(VakServlet.regex, req, resp, "hashtag");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Key key = KeyFactory.stringToKey(vakIdStr);
		Vak vak = pm.getObjectById(Vak.class, key);
		
		vak.setName(name);
		vak.setHashTag(hashTag);
		
		
		pm.close();
		
		resp.getWriter().write("Updated succesfully" + vak.getVakId().toString());
	}

	private void addVak(HttpServletRequest req, HttpServletResponse resp) throws FormatException, IOException {
		String name = FieldVerifier.getParam(VakServlet.regex, req, resp, "name");
		String hashTag = FieldVerifier.getParam(VakServlet.regex, req, resp, "hashtag");

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Vak vak = new Vak();

		vak.setName(name);
		vak.setHashTag(hashTag);

		//Store new Vak to the db
		pm.makePersistent(vak);

		pm.close();

		resp.getWriter().write("Added succesfully"+ KeyFactory.keyToString(vak.getVakId()));
	}
}
