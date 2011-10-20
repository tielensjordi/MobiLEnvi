	package be.kuleuven.mume.social.twitter;

	import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

	import twitter4j.Twitter;
	import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class Callback extends HttpServlet {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5844389600588939068L;
		
		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			
	    	resp.setContentType("text/plain");
	    	try {
	    		/*if(req.getSession().getAttribute("aToken") != null)
	    		{
	    			log.log(Level.WARNING, "Callback: AutorisationToken already set.");
	    			return;
	    		}*/
	    		
	    		Twitter twitter = new TwitterFactory().getInstance();
	    		
	    		//store twitter user in session
	    	    twitter.setOAuthConsumer("Sx53PNSwLsq3ifCn7ylbBw", "JiPdcDv7d206jpeKCZIgZOmqIMZbidSM9REGfq44");
	
		        
		        // Get the temp object out of the datastore, and get the old RequestToken
		        // Yes, it MUST be the old RequestToken. Or at least, have all the same parameters
		        // as the old RequestToken.
	    	    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    	    String oauth_token = req.getParameter("oauth_token");
		        Key k = KeyFactory.createKey("OAuthTemp", oauth_token);
				Entity temp = datastore.get(k);
				datastore.delete(k);
		        RequestToken requestToken = (RequestToken) convertToObject((Blob)temp.getProperty("rtoken"));
		        
		        String verifier = req.getParameter("oauth_verifier");
		        
		        
		        if(requestToken == null)
		        {
		        	log.log(Level.SEVERE,"Callback: No requestToken attribute not found!;");
		        	return;
		        }
		        
	            AccessToken token = twitter.getOAuthAccessToken(requestToken, verifier);
	            
	            if(twitter.verifyCredentials().isVerified())
	            	resp.getWriter().println("Success!");
	            
	            
	            	resp.getWriter().println("");
		        
	            	//@TODO: Save token to persistent db
	            /*k = KeyFactory.createKey(kind, id);
	            Entity aToken = new Entity(k)
	            datastore.put(entity)*/
	            
	            log.log(Level.INFO, "Callback received");
	        } 
	    	catch (TwitterException e) {
	        	log.log(Level.SEVERE, "TwitterExep: " + e.getErrorMessage());
	        }
	        catch (EntityNotFoundException e) {
	        	log.log(Level.SEVERE, "Could not find the rToken in the database");
	        }
	        catch ( Exception e) {
	        	log.log(Level.SEVERE, e.getMessage());
	        }
	    	
	}
		
		private Object convertToObject(Blob b) throws IOException, ClassNotFoundException {
			ByteArrayInputStream bis = new ByteArrayInputStream(b.getBytes());
			ObjectInput in = new ObjectInputStream(bis);
			Object o = in.readObject();

			bis.close();
			in.close();
			
			return o;
		}

}
