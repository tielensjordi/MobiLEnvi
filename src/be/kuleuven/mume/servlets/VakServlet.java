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
	
	public VakServlet(){
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		if(VakServlet.regex.containsKey("name"))
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
			String name = FieldVerifier.getParam(VakServlet.regex, req,resp, "name");
			String hashTag = FieldVerifier.getParam(VakServlet.regex, req, resp, "hashtag");
			String vakId = null;
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Vak vak = null;
			
			if(q.equals("update")|| q.equals("del"))
			{
				vakId = FieldVerifier.getParam(VakServlet.regex, req,resp,"vakid");
				if(vakId == null)
					return;
				
				Key key = KeyFactory.stringToKey(vakId);
				vak = pm.getObjectById(Vak.class,key);
			}
			else
				vak = new Vak();
			
			vak.setName(name);
			vak.setHashTag(hashTag);
			
			//Store new Vak to the db
			if(q.equals("add"))
				pm.makePersistent(vak);
			//delete vak from db
			if(q.equals("del"))
				pm.deletePersistent(vak);
			
			//updates will go automatically.
			pm.close();
			resp.getWriter().write("Success name:"+name);
		}
		catch(FormatException e){
			resp.getWriter().write(e.getMessage());
			resp.setStatus(400);
		}
	}
}
